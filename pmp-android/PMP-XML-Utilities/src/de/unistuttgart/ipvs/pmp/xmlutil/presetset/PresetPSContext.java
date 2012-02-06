package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetPSContext {
    
    /**
     * The type
     */
    private String type;
    
    /**
     * The condition
     */
    private String condition;
    
    /**
     * The override value
     */
    private String overrideValue;
    
    
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
    
    
    /**
     * Get the type
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    
    /**
     * Set the type
     * 
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    
    /**
     * Get the condition
     * 
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }
    
    
    /**
     * Set the condition
     * 
     * @param condition
     *            the condition to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    
    /**
     * Get the override value
     * 
     * @return the overrideValue
     */
    public String getOverrideValue() {
        return overrideValue;
    }
    
    
    /**
     * Set the override value
     * 
     * @param overrideValue
     *            the overrideValue to set
     */
    public void setOverrideValue(String overrideValue) {
        this.overrideValue = overrideValue;
    }
    
}
