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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.aidl.IEnergy;

/**
 * This is the AIDL implementation for cloak data
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyImplCloak extends IEnergy.Stub {
    
    private PSValidator psv;
    
    
    public EnergyImplCloak(ResourceGroup rg, String appIdentifier) {
        this.psv = new PSValidator(rg, appIdentifier);
    }
    
    
    public String getCurrentLevel() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_LEVEL, "true");
        
        return "0";
    }
    
    
    public String getCurrentHealth() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_HEALTH, "true");
        
        return EnergyConstants.HEALTH_UNKNOWN;
    }
    
    
    public String getCurrentStatus() throws RemoteException {
        return EnergyConstants.STATUS_UNKNOWN;
    }
    
    
    public String getCurrentPlugged() throws RemoteException {
        return EnergyConstants.PLUGGED_NOT_PLUGGED;
    }
    
    
    public String getCurrentStatusTime() throws RemoteException {
        return "0";
    }
    
    
    public String getCurrentTemperature() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return "0°C";
    }
    
    
    public String getLastBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        
        return "0";
    }
    
    
    public String getLastBootUptime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        
        return "0";
    }
    
    
    public String getLastBootUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, "true");
        
        return "0";
    }
    
    
    public String getLastBootDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        this.psv.validate(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, "true");
        
        return "0";
    }
    
    
    public String getLastBootRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return "0:0";
    }
    
    
    public String getLastBootTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return "0°C";
    }
    
    
    public String getLastBootTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return "0°C";
    }
    
    
    public String getLastBootCountOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_COUNT, "true");
        
        return "0";
    }
    
    
    public String getLastBootScreenOn() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_SCREEN, "true");
        
        return "0";
    }
    
    
    public String getTotalBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        
        return "0";
    }
    
    
    public String getTotalUptime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        
        return "0";
    }
    
    
    public String getTotalUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, "true");
        
        return "0";
    }
    
    
    public String getTotalDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        this.psv.validate(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, "true");
        
        return "0";
    }
    
    
    public String getTotalRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return "0:0";
    }
    
    
    public String getTotalTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return "0°C";
    }
    
    
    public String getTotalTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return "0°C";
    }
    
    
    public String getTotalCountOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_COUNT, "true");
        
        return "0";
    }
    
    
    public String getTotalScreenOn() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_SCREEN, "true");
        
        return "0";
    }
    
    
    public String uploadData() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_UPLOAD_DATA, "true");
        
        return "http://u.r.l";
    }
    
}
