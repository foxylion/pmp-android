/*
 * Copyright 2012 pmp-android development team
 * Project: EnergyResourceGroup
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset;

/**
 * This is an abstract implementation of a result set for the energy resource
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
        return this.date;
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
        return this.uptime;
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
        return this.uptimeBattery;
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
        return this.durationOfCharging;
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
        return this.ratio;
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
        return this.temperaturePeak;
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
        return this.temperatureAverage;
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
        return this.countOfCharging;
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
        return this.screenOn;
    }
    
    
    /**
     * @param screenOn
     *            the screenOn to set
     */
    public void setScreenOn(String screenOn) {
        this.screenOn = screenOn;
    }
    
}
