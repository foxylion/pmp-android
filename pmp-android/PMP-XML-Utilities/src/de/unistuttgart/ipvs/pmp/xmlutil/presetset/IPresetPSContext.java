package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

public interface IPresetPSContext {
    
    /**
     * Get the type
     * 
     * @return the type
     */
    public abstract String getType();
    
    
    /**
     * Set the type
     * 
     * @param type
     *            the type to set
     */
    public abstract void setType(String type);
    
    
    /**
     * Get the condition
     * 
     * @return the condition
     */
    public abstract String getCondition();
    
    
    /**
     * Set the condition
     * 
     * @param condition
     *            the condition to set
     */
    public abstract void setCondition(String condition);
    
    
    /**
     * Get the override value
     * 
     * @return the overrideValue
     */
    public abstract String getOverrideValue();
    
    
    /**
     * Set the override value
     * 
     * @param overrideValue
     *            the overrideValue to set
     */
    public abstract void setOverrideValue(String overrideValue);
    
}
