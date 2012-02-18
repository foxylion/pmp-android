package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetPSContext implements Serializable, IPresetPSContext {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 8503765925521936460L;
    
    /**
     * The type
     */
    private String type = "";
    
    /**
     * The condition
     */
    private String condition = "";
    
    /**
     * The override value
     */
    private String overrideValue = "";
    
    
    /**
     * Constructor to set the attributes
     * 
     * @param type
     *            type
     * @param condition
     *            condition
     * @param overrideValue
     *            overrideValue
     */
    public PresetPSContext(String type, String condition, String overrideValue) {
        this.type = type;
        this.condition = condition;
        this.overrideValue = overrideValue;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext#getType()
     */
    @Override
    public String getType() {
        return this.type;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext#setType(java.lang.String)
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext#getCondition()
     */
    @Override
    public String getCondition() {
        return this.condition;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext#setCondition(java.lang.String)
     */
    @Override
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext#getOverrideValue()
     */
    @Override
    public String getOverrideValue() {
        return this.overrideValue;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext#setOverrideValue(java.lang.String)
     */
    @Override
    public void setOverrideValue(String overrideValue) {
        this.overrideValue = overrideValue;
    }
    
}
