package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractResultSet {
    
    /**
     * The attributes
     */
    private String date = "";
    private String uptime = "";
    private String uptimeBattery = "";
    private String durationOfCharging = "";
    private String ratio = "";
    private String temperaturePeak = "";
    private String temperatureAverage = "";
    private String countOfCharging = "";
    private String screenOn = "";
    
    
    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }
    
    
    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
    
    
    /**
     * @return the uptime
     */
    public String getUptime() {
        return uptime;
    }
    
    
    /**
     * @param uptime
     *            the uptime to set
     */
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
    
    
    /**
     * @return the uptimeBattery
     */
    public String getUptimeBattery() {
        return uptimeBattery;
    }
    
    
    /**
     * @param uptimeBattery
     *            the uptimeBattery to set
     */
    public void setUptimeBattery(String uptimeBattery) {
        this.uptimeBattery = uptimeBattery;
    }
    
    
    /**
     * @return the durationOfCharging
     */
    public String getDurationOfCharging() {
        return durationOfCharging;
    }
    
    
    /**
     * @param durationOfCharging
     *            the durationOfCharging to set
     */
    public void setDurationOfCharging(String durationOfCharging) {
        this.durationOfCharging = durationOfCharging;
    }
    
    
    /**
     * @return the ratio
     */
    public String getRatio() {
        return ratio;
    }
    
    
    /**
     * @param ratio
     *            the ratio to set
     */
    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
    
    
    /**
     * @return the temperaturePeak
     */
    public String getTemperaturePeak() {
        return temperaturePeak;
    }
    
    
    /**
     * @param temperaturePeak
     *            the temperaturePeak to set
     */
    public void setTemperaturePeak(String temperaturePeak) {
        this.temperaturePeak = temperaturePeak;
    }
    
    
    /**
     * @return the temperatureAverage
     */
    public String getTemperatureAverage() {
        return temperatureAverage;
    }
    
    
    /**
     * @param temperatureAverage
     *            the temperatureAverage to set
     */
    public void setTemperatureAverage(String temperatureAverage) {
        this.temperatureAverage = temperatureAverage;
    }
    
    
    /**
     * @return the countOfCharging
     */
    public String getCountOfCharging() {
        return countOfCharging;
    }
    
    
    /**
     * @param countOfCharging
     *            the countOfCharging to set
     */
    public void setCountOfCharging(String countOfCharging) {
        this.countOfCharging = countOfCharging;
    }
    
    
    /**
     * @return the screenOn
     */
    public String getScreenOn() {
        return screenOn;
    }
    
    
    /**
     * @param screenOn
     *            the screenOn to set
     */
    public void setScreenOn(String screenOn) {
        this.screenOn = screenOn;
    }
    
}
