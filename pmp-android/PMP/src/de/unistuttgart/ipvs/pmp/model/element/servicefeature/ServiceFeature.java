package de.unistuttgart.ipvs.pmp.model.element.servicefeature;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

/**
 * @see IServiceFeature
 * @author Tobias Kuhn
 * 
 */
public class ServiceFeature extends ModelElement implements IServiceFeature {
    
    /**
     * identifying attributes
     */
    protected App app;
    protected String localIdentifier;
    
    /**
     * internal data & links
     */
    protected Map<PrivacySetting, String> privacySettingValues;
    protected boolean containsUnknownPrivacySettings;
    
    
    /* organizational */
    
    public ServiceFeature(App app, String identifier) {
        super(app.getIdentifier() + PersistenceConstants.PACKAGE_SEPARATOR + identifier);
        this.app = app;
        this.localIdentifier = identifier;
    }
    
    
    @Override
    public String toString() {
        return super.toString()
                + String.format(" [psv = %s, cups = %s]", ModelElement.collapseMapToString(this.privacySettingValues),
                        String.valueOf(this.containsUnknownPrivacySettings));
    }
    
    
    /* interface */
    
    @Override
    public IApp getApp() {
        return this.app;
    }
    
    
    @Override
    public String getLocalIdentifier() {
        return this.localIdentifier;
    }
    
    
    @Override
    public String getName() {
        String name = this.app.getAis().getServiceFeaturesMap().get(getLocalIdentifier()).getNames()
                .get(Locale.getDefault());
        if (name == null) {
            name = this.app.getAis().getServiceFeaturesMap().get(getLocalIdentifier()).getNames().get(Locale.ENGLISH);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        String description = this.app.getAis().getServiceFeaturesMap().get(getLocalIdentifier()).getDescriptions()
                .get(Locale.getDefault());
        if (description == null) {
            description = this.app.getAis().getServiceFeaturesMap().get(getLocalIdentifier()).getDescriptions()
                    .get(Locale.ENGLISH);
        }
        return description;
    }
    
    
    @Override
    public IPrivacySetting[] getRequiredPrivacySettings() {
        checkCached();
        return this.privacySettingValues.keySet().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public String getRequiredPrivacySettingValue(IPrivacySetting privacySetting) {
        checkCached();
        Assert.nonNull(privacySetting, new ModelMisuseError(Assert.ILLEGAL_NULL, "privacySetting", privacySetting));
        return this.privacySettingValues.get(privacySetting);
    }
    
    
    @Override
    public boolean isAvailable() {
        checkCached();
        return !this.containsUnknownPrivacySettings;
    }
    
    
    @Override
    public boolean isActive() {
        checkCached();
        if (this.containsUnknownPrivacySettings) {
            return false;
        }
        
        try {
            Map<IPrivacySetting, String> granted = new HashMap<IPrivacySetting, String>();
            // for all presets
            for (IPreset p : this.app.getAssignedPresets()) {
                if (!p.isAvailable() || p.isDeleted()) {
                    continue;
                }
                
                // all granted privacy settings
                for (IPrivacySetting ps : p.getGrantedPrivacySettings()) {
                    
                    String existing = granted.get(ps);
                    String grantNow = p.getGrantedPrivacySettingValue(ps);
                    
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
            
            // actual check against granted
            for (Entry<PrivacySetting, String> e : this.privacySettingValues.entrySet()) {
                if (!e.getKey().permits(e.getValue(), granted.get(e.getKey()))) {
                    return false;
                }
            }
            
            return true;
            
        } catch (PrivacySettingValueException plve) {
            Log.e("Could not check whether service feature is active.", plve);
            return false;
        }
    }
    
    
    /* inter-model communication */
    
    public Map<PrivacySetting, String> getRequiredPrivacySettingValues() {
        checkCached();
        return this.privacySettingValues;
    }
    
}
