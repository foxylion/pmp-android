package de.unistuttgart.ipvs.pmp.resourcegroups.energy.event;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class BatteryEvent extends AbstractEvent {
    
    /**
     * Attributes of the battery event
     */
    private int level;
    private String health;
    private String status;
    private String plugged;
    private boolean present;
    private String technology;
    private float temperature;
    private int voltage;
    
    
    /**
     * Constructor to set the attributes
     */
    public BatteryEvent(int id, long timestamp, int level, String health, String status, String plugged,
            boolean present, String technology, float temperature, int voltage) {
        this.id = id;
        this.timestamp = timestamp;
        this.level = level;
        this.health = health;
        this.status = status;
        this.plugged = plugged;
        this.present = present;
        this.technology = technology;
        this.temperature = temperature;
        this.voltage = voltage;
    }
    
    
    /**
     * Constructor without attributes
     */
    public BatteryEvent() {
    }
    
    
    /**
     * @return the level
     */
    public int getLevel() {
        return this.level;
    }
    
    
    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
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
     * @return the present
     */
    public boolean isPresent() {
        return this.present;
    }
    
    
    /**
     * @param present
     *            the present to set
     */
    public void setPresent(boolean present) {
        this.present = present;
    }
    
    
    /**
     * @return the technology
     */
    public String getTechnology() {
        return this.technology;
    }
    
    
    /**
     * @param technology
     *            the technology to set
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }
    
    
    /**
     * @return the temperature
     */
    public float getTemperature() {
        return this.temperature;
    }
    
    
    /**
     * @param temperature
     *            the temperature to set
     */
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    
    
    /**
     * @return the voltage
     */
    public int getVoltage() {
        return this.voltage;
    }
    
    
    /**
     * @param voltage
     *            the voltage to set
     */
    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }
    
}
