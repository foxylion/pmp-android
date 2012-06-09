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
