package de.unistuttgart.ipvs.pmp.model.element.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;
import de.unistuttgart.ipvs.pmp.model.ipc.IPCProvider;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevelValueException;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSet;

/**
 * @see IApp
 * @author Tobias Kuhn
 * 
 */
public class App extends ModelElement implements IApp {
    
    /**
     * localized values
     */
    protected AppInformationSet ais;
    
    /**
     * internal data & links
     */
    protected Map<String, ServiceFeature> serviceFeatures;
    protected List<Preset> assignedPresets;
    
    
    /* organizational */
    
    public App(String appPackage) {
        super(appPackage);
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        String name = this.ais.getNames().get(Locale.getDefault());
        if (name == null) {
            name = this.ais.getNames().get(Locale.US);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        String description = this.ais.getDescriptions().get(Locale.getDefault());
        if (description == null) {
            description = this.ais.getDescriptions().get(Locale.US);
        }
        return description;
    }
    
    
    @Override
    public IServiceFeature[] getServiceFeatures() {
        checkCached();
        return this.serviceFeatures.values().toArray(new IServiceFeature[0]);
    }
    
    
    @Override
    public IServiceFeature getServiceFeature(String serviceFeatureIdentifier) {
        checkCached();
        Assert.nonNull(serviceFeatureIdentifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "serviceFeatureIdentifier",
                serviceFeatureIdentifier));
        return this.serviceFeatures.get(serviceFeatureIdentifier);
    }
    
    
    @Override
    public IServiceFeature[] getActiveServiceFeatures() {
        checkCached();
        List<ServiceFeature> actives = new ArrayList<ServiceFeature>();
        for (ServiceFeature sf : this.serviceFeatures.values()) {
            if (sf.isActive()) {
                actives.add(sf);
            }
        }
        return actives.toArray(new IServiceFeature[0]);
    }
    
    
    @Override
    public void verifyServiceFeatures() {
        checkCached();
        try {
            Map<IPrivacySetting, String> granted = new HashMap<IPrivacySetting, String>();
            // for all presets
            for (IPreset p : getAssignedPresets()) {
                if (!p.isAvailable() || p.isDeleted()) {
                    continue;
                }
                
                // all granted privacy settings
                for (IPrivacySetting ps : p.getGrantedPrivacyLevels()) {
                    
                    String existing = granted.get(ps);
                    String grantNow = p.getGrantedPrivacyLevelValue(ps);
                    
                    if (grantNow != null) {
                        if (existing == null) {
                            granted.put(ps, grantNow);
                        } else {
                            if (ps.permits(existing, grantNow)) {
                                // grantNow allows more
                                granted.put(ps, grantNow);
                            } /* else existing allows more, do nothing */
                        }
                    }
                    
                }
            }
            
            Map<ServiceFeature, Boolean> verification = new HashMap<ServiceFeature, Boolean>();
            // actual check against granted
            sfLoop: for (ServiceFeature sf : this.serviceFeatures.values()) {
                verification.put(sf, true);
                
                for (Entry<PrivacySetting, String> e : sf.getRequiredPrivacySettingValues().entrySet()) {
                    if (!e.getKey().permits(e.getValue(), granted.get(e.getKey()))) {
                        verification.put(sf, false);
                        continue sfLoop;
                    }
                }
            }
            
            IPCProvider.getInstance().queue(getIdentifier(), verification);
            
        } catch (PrivacyLevelValueException plve) {
            Log.e("Could not check whether service feature is active.", plve);
        }
    }
    
    
    @Override
    public IPreset[] getAssignedPresets() {
        checkCached();
        return this.assignedPresets.toArray(new IPreset[0]);
    }
    
    
    @Override
    public Drawable getIcon() {
        try {
            return PMPApplication.getContext().getPackageManager().getApplicationIcon(getIdentifier());
        } catch (NameNotFoundException e) {
            return null;
        }
    }
    
    
    /* inter-model communication */
    
    public AppInformationSet getAis() {
        checkCached();
        return this.ais;
    }
    
    
    /**
     * Removes the preset when it gets deleted.
     * 
     * @param p
     */
    public void removePreset(Preset p) {
        checkCached();
        Assert.nonNull(p, new ModelIntegrityError(Assert.ILLEGAL_NULL, "p", p));
        this.assignedPresets.remove(p);
        verifyServiceFeatures();
    }
    
}
