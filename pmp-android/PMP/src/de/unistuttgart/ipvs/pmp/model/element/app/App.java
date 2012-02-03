package de.unistuttgart.ipvs.pmp.model.element.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.PresetController;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;
import de.unistuttgart.ipvs.pmp.model.ipc.IPCProvider;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;

/**
 * @see IApp
 * @author Tobias Kuhn
 * 
 */
public class App extends ModelElement implements IApp {
    
    /**
     * localized values
     */
    protected AIS ais;
    
    /**
     * internal data & links
     */
    protected Map<String, ServiceFeature> serviceFeatures;
    protected List<Preset> assignedPresets;
    
    
    /* organizational */
    
    public App(String appPackage) {
        super(appPackage);
    }
    
    
    @Override
    public String toString() {
        return super.toString()
                + String.format(" [ais = %s, sf = %s, ap = %s]", this.ais,
                        ModelElement.collapseMapToString(this.serviceFeatures),
                        ModelElement.collapseListToString(this.assignedPresets));
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        String name = this.ais.getNameForLocale(Locale.getDefault());
        if (name == null) {
            name = this.ais.getNameForLocale(Locale.ENGLISH);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        String description = this.ais.getDescriptionForLocale(Locale.getDefault());
        if (description == null) {
            description = this.ais.getDescriptionForLocale(Locale.ENGLISH);
        }
        return description;
    }
    
    
    @Override
    public Drawable getIcon() {
        try {
            return PMPApplication.getContext().getPackageManager().getApplicationIcon(getIdentifier());
        } catch (NameNotFoundException e) {
            return null;
        }
    }
    
    
    @Override
    public IServiceFeature[] getServiceFeatures() {
        checkCached();
        Collection<ServiceFeature> result = this.serviceFeatures.values();
        return result.toArray(new IServiceFeature[result.size()]);
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
        return actives.toArray(new IServiceFeature[actives.size()]);
    }
    
    
    @Override
    public void verifyServiceFeatures() {
        checkCached();
        try {
            Map<ServiceFeature, Boolean> verification = PresetController.verifyServiceFeatures(this,
                    this.serviceFeatures.values());
            
            IPCProvider.getInstance().queue(getIdentifier(), verification);
            
        } catch (PrivacySettingValueException plve) {
            Log.e("Could not check which service features are active.", plve);
        }
    }
    
    
    @Override
    public IPreset[] getAssignedPresets() {
        checkCached();
        return this.assignedPresets.toArray(new IPreset[this.assignedPresets.size()]);
    }
    
    
    @Override
    public String getBestAssignedPrivacySettingValue(IPrivacySetting privacySetting)
            throws PrivacySettingValueException {
        return PresetController.findBestValue(this, privacySetting);
    }
    
    
    /* inter-model communication */
    
    public AIS getAis() {
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
    
    
    public void addPreset(Preset p) {
        checkCached();
        Assert.nonNull(p, new ModelIntegrityError(Assert.ILLEGAL_NULL, "p", p));
        this.assignedPresets.add(p);
        verifyServiceFeatures();
    }
    
}
