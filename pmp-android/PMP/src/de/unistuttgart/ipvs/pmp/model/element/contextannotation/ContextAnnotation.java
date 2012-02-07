package de.unistuttgart.ipvs.pmp.model.element.contextannotation;

import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

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
    
}
