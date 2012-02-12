package de.unistuttgart.ipvs.pmp.model.element.contextannotation;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

/**
 * @see IContextAnnotation
 * @author Tobias Kuhn
 * 
 */
public class ContextAnnotation extends ModelElement implements IContextAnnotation {
    
    /**
     * identifying attributes
     */
    protected IPreset preset;
    protected IPrivacySetting privacySetting;
    
    /**
     * internal data & links
     */
    protected IContext context;
    protected String condition;
    protected String overrideValue;
    
    
    /* organizational */
    
    public ContextAnnotation(IPreset preset, IPrivacySetting privacySetting) {
        super(preset.getIdentifier() + PersistenceConstants.PACKAGE_SEPARATOR + PersistenceConstants.PACKAGE_SEPARATOR
                + privacySetting.getIdentifier());
        this.preset = preset;
        this.privacySetting = privacySetting;
    }
    
    
    @Override
    public String toString() {
        return super.toString()
                + String.format(" [ctx = %s, cond = %s, ovrd = %s]", this.context.getName(), this.condition,
                        this.overrideValue);
    }
    
    
    /* interface */
    
    @Override
    public IPreset getPreset() {
        return this.preset;
    }
    
    
    @Override
    public IPrivacySetting getPrivacySetting() {
        return this.privacySetting;
    }
    
    
    @Override
    public IContext getContext() {
        checkCached();
        return this.context;
    }
    
    
    @Override
    public String getContextCondition() {
        checkCached();
        return this.condition;
    }
    
    
    @Override
    public String getOverridePrivacySettingValue() {
        checkCached();
        return this.overrideValue;
    }
    
    
    @Override
    public boolean isActive() {
        checkCached();
        return this.context.getLastState(this.condition);
    }
    
    
    @Override
    public String getCurrentPrivacySettingValue() {
        checkCached();
        return isActive() ? getOverridePrivacySettingValue() : null;
    }
    
    
    @Override
    public IContextAnnotation[] getConflictingContextAnnotations(IPreset preset) {
        boolean hasSameAppsAssigned = false;
        for (IApp app : this.preset.getAssignedApps()) {
            if (preset.isAppAssigned(app)) {
                hasSameAppsAssigned = true;
                break;
            }
        }
        
        if (!hasSameAppsAssigned) {
            return new IContextAnnotation[0];
        }
        
        return preset.getContextAnnotations(this.privacySetting);
    }
    
    
    @Override
    public IPrivacySetting[] getConflictingPrivacySettings(IPreset preset) {
        boolean hasSameAppsAssigned = false;
        for (IApp app : this.preset.getAssignedApps()) {
            if (preset.isAppAssigned(app)) {
                hasSameAppsAssigned = true;
                break;
            }
        }
        
        if (!hasSameAppsAssigned) {
            return new IPrivacySetting[0];
        }
        
        String grantedByPreset = preset.getGrantedPrivacySettingValue(this.privacySetting);
        try {
            if (this.privacySetting.permits(this.overrideValue, grantedByPreset)) {
                return new IPrivacySetting[] { this.privacySetting };
            } else {
                return new IPrivacySetting[0];
            }
        } catch (PrivacySettingValueException e) {
            Log.e(this, "Invalid value while checking for CA/PS conflicts: ", e);
            return new IPrivacySetting[0];
        }
    }
}
