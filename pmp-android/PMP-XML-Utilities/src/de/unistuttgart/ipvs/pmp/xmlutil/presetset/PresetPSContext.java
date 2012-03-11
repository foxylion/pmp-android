package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetPSContext extends IssueLocation implements Serializable, IPresetPSContext {
    
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
     * The flag, if its an empty condition
     */
    private boolean emptyCondition = false;
    
    /**
     * The override value
     */
    private String overrideValue = "";
    
    /**
     * The flag, if its an empty override value
     */
    private boolean emptyOverrideValue = false;
    
    
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
    
    
    @Override
    public String getType() {
        return this.type;
    }
    
    
    @Override
    public void setType(String type) {
        this.type = type;
    }
    
    
    @Override
    public String getCondition() {
        return this.condition;
    }
    
    
    @Override
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    
    @Override
    public String getOverrideValue() {
        return this.overrideValue;
    }
    
    
    @Override
    public void setOverrideValue(String overrideValue) {
        this.overrideValue = overrideValue;
    }
    
    
    @Override
    public boolean isEmptyOverrideValue() {
        return this.emptyOverrideValue;
    }
    
    
    @Override
    public void setEmptyOverrideValue(boolean emptyOverrideValue) {
        this.emptyOverrideValue = emptyOverrideValue;
    }
    
    
    @Override
    public boolean isEmptyCondition() {
        return this.emptyCondition;
    }
    
    
    @Override
    public void setEmptyCondition(boolean emptyCondition) {
        this.emptyCondition = emptyCondition;
    }
    
}
