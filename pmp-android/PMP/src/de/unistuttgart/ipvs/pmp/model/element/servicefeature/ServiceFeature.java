package de.unistuttgart.ipvs.pmp.model.element.servicefeature;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.PresetController;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingPrivacySettingValue;
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
    protected List<MissingPrivacySettingValue> missingPrivacySettings;
    
    
    /* organizational */
    
    public ServiceFeature(App app, String identifier) {
        super(app.getIdentifier() + PersistenceConstants.PACKAGE_SEPARATOR + identifier);
        this.app = app;
        this.localIdentifier = identifier;
    }
    
    
    @Override
    public String toString() {
        return super.toString()
                + String.format(" [psv = %s, mps = %s]", ModelElement.collapseMapToString(this.privacySettingValues),
                        ModelElement.collapseListToString(this.missingPrivacySettings));
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
        String name = this.app.getAis().getServiceFeatureForIdentifier(getLocalIdentifier())
                .getNameForLocale(Locale.getDefault());
        if (name == null) {
            name = this.app.getAis().getServiceFeatureForIdentifier(getLocalIdentifier())
                    .getNameForLocale(Locale.ENGLISH);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        String description = this.app.getAis().getServiceFeatureForIdentifier(getLocalIdentifier())
                .getDescriptionForLocale(Locale.getDefault());
        if (description == null) {
            description = this.app.getAis().getServiceFeatureForIdentifier(getLocalIdentifier())
                    .getDescriptionForLocale(Locale.ENGLISH);
        }
        return description;
    }
    
    
    @Override
    public IPrivacySetting[] getRequiredPrivacySettings() {
        checkCached();
        Collection<PrivacySetting> result = this.privacySettingValues.keySet();
        return result.toArray(new IPrivacySetting[result.size()]);
    }
    
    
    @Override
    public String getRequiredPrivacySettingValue(IPrivacySetting privacySetting) {
        checkCached();
        Assert.nonNull(privacySetting, ModelMisuseError.class, Assert.ILLEGAL_NULL, "privacySetting", privacySetting);
        return this.privacySettingValues.get(privacySetting);
    }
    
    
    @Override
    public boolean isAvailable() {
        checkCached();
        return this.missingPrivacySettings.size() == 0;
    }
    
    
    @Override
    public MissingPrivacySettingValue[] getMissingPrivacySettings() {
        checkCached();
        return this.missingPrivacySettings.toArray(new MissingPrivacySettingValue[this.missingPrivacySettings.size()]);
    }
    
    
    @Override
    public boolean isActive() {
        checkCached();
        try {
            return PresetController.verifyServiceFeature(this);
            
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
