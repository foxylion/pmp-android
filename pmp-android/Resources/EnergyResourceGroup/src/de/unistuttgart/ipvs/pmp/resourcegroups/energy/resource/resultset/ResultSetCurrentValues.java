package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset;

/**
 * This is the result set for the current values for the energy resource
 * 
 * @author Marcus Vetter
 * 
 */
public class ResultSetCurrentValues {
    
    private String level = "";
    private String health = "";
    private String status = "";
    private String statusTime = "";
    private String plugged = "";
    private String temperature = "";
    
    
    /**
     * @return the level
     */
    public String getLevel() {
        return this.level;
    }
    
    
    /**
     * @param level
     *            the level to set
     */
    public void setLevel(String level) {
        this.level = level;
    }
    
    
    /**
     * @return the health
     */
    public String getHealth() {
        return this.health;
    }
    
    
    /**
     * @param health
     *            the health to set
     */
    public void setHealth(String health) {
        this.health = health;
    }
    
    
    /**
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }
    
    
    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
    /**
     * @return the statusTime
     */
    public String getStatusTime() {
        return this.statusTime;
    }
    
    
    /**
     * @param statusTime
     *            the statusTime to set
     */
    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }
    
    
    /**
     * @return the plugged
     */
    public String getPlugged() {
        return this.plugged;
    }
    
    
    /**
     * @param plugged
     *            the plugged to set
     */
    public void setPlugged(String plugged) {
        this.plugged = plugged;
    }
    
    
    /**
     * @return the temperature
     */
    public String getTemperature() {
        return this.temperature;
    }
    
    
    /**
     * @param temperature
     *            the temperature to set
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    
}
