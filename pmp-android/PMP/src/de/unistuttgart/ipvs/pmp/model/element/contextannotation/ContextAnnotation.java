package de.unistuttgart.ipvs.pmp.model.element.contextannotation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.util.FileLog;

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
    
    /**
     * State before the last state, primarily needed for logging purposes
     */
    protected boolean lastState;
    
    
    /* organizational */
    
    public ContextAnnotation(IPreset preset, IPrivacySetting privacySetting) {
        super(preset.getIdentifier() + PersistenceConstants.PACKAGE_SEPARATOR + PersistenceConstants.PACKAGE_SEPARATOR
                + privacySetting.getIdentifier());
        this.preset = preset;
        this.privacySetting = privacySetting;
        this.lastState = false;
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
    public String getHumanReadableContextCondition() {
        checkCached();
        return this.context.makeHumanReadable(this.condition);
    }
    
    
    @Override
    public String getOverridePrivacySettingValue() {
        checkCached();
        return this.overrideValue;
    }
    
    
    @Override
    public boolean isActive() {
        checkCached();
        boolean newState = this.context.getLastState(this.condition);
        
        if (newState != this.lastState) {
            FileLog.get().logWithForward(this, null, FileLog.GRANULARITY_CONTEXT_CHANGES, Level.FINE,
                    "Context Annotation '%s' is now %s.", this.condition, newState ? "active" : "deactivated");
            
            this.lastState = newState;
        }
        return newState;
    }
    
    
    @Override
    public String getCurrentPrivacySettingValue() {
        checkCached();
        return isActive() ? getOverridePrivacySettingValue() : null;
    }
    
    
    @Override
    public List<IContextAnnotation> getConflictingContextAnnotations(IPreset preset) {
        boolean hasSameAppsAssigned = false;
        for (IApp app : this.preset.getAssignedApps()) {
            if (preset.isAppAssigned(app)) {
                hasSameAppsAssigned = true;
                break;
            }
        }
        
        if (!hasSameAppsAssigned) {
            return new ArrayList<IContextAnnotation>();
        }
        
        return preset.getContextAnnotations(this.privacySetting);
    }
    
    
    @Override
    public boolean isPrivacySettingConflicting(IPreset preset) {
        boolean hasSameAppsAssigned = false;
        for (IApp app : this.preset.getAssignedApps()) {
            if (preset.isAppAssigned(app)) {
                hasSameAppsAssigned = true;
                break;
            }
        }
        
        if (!hasSameAppsAssigned) {
            return false;
        }
        
        String grantedByPreset = preset.getGrantedPrivacySettingValue(this.privacySetting);
        try {
            return this.privacySetting.permits(this.overrideValue, grantedByPreset);
        } catch (PrivacySettingValueException e) {
            Log.e(this, "Invalid value while checking for CA/PS conflicts: ", e);
            return false;
        }
    }
    
}
