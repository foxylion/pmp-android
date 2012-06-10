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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
    
    private static final int DATE_OF_2012_05_04_IN_SEC = 1336129383;
    
    private PSValidator psv;
    
    
    public EnergyImplCloak(ResourceGroup rg, String appIdentifier) {
        this.psv = new PSValidator(rg, appIdentifier);
    }
    
    
    public String getCurrentLevel() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_LEVEL, "true");
        
        return String.valueOf(new Random().nextInt(101)) + " %";
    }
    
    
    public String getCurrentHealth() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_HEALTH, "true");
        
        switch (new Random().nextInt(6)) {
            case 0:
                return EnergyConstants.HEALTH_UNKNOWN;
            case 1:
                return EnergyConstants.HEALTH_DEAD;
            case 2:
                return EnergyConstants.HEALTH_GOOD;
            case 3:
                return EnergyConstants.HEALTH_OVER_VOLTAGE;
            case 4:
                return EnergyConstants.HEALTH_UNSPECIFIED_FAILURE;
            case 5:
                return EnergyConstants.HEALTH_OVERHEAT;
            default:
                return EnergyConstants.HEALTH_UNKNOWN;
        }
        
    }
    
    
    public String getCurrentStatus() throws RemoteException {
        
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_STATUS, "true");
        
        switch (new Random().nextInt(5)) {
            case 0:
                return EnergyConstants.STATUS_CHARGING;
            case 1:
                return EnergyConstants.STATUS_DISCHARGING;
            case 2:
                return EnergyConstants.STATUS_FULL;
            case 3:
                return EnergyConstants.STATUS_NOT_CHARGING;
            case 4:
                return EnergyConstants.STATUS_UNKNOWN;
            default:
                return EnergyConstants.STATUS_UNKNOWN;
        }
    }
    
    
    public String getCurrentPlugged() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_PLUGGED, "true");
        
        switch (new Random().nextInt(3)) {
            case 0:
                return EnergyConstants.PLUGGED_NOT_PLUGGED;
            case 1:
                return EnergyConstants.PLUGGED_AC;
            case 2:
                return EnergyConstants.PLUGGED_USB;
            default:
                return EnergyConstants.PLUGGED_NOT_PLUGGED;
        }
    }
    
    
    public String getCurrentStatusTime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_STATUS_TIME, "true");
        
        return String.valueOf(new Random().nextInt());
    }
    
    
    public String getCurrentTemperature() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return String.valueOf(new Random().nextInt(20) + 20) + "." + String.valueOf(new Random().nextInt(10) + "°C");
    }
    
    
    public String getLastBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        
        return generateDate(new Random().nextInt(DATE_OF_2012_05_04_IN_SEC));
    }
    
    
    public String getLastBootUptime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getLastBootUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, "true");
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getLastBootDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        this.psv.validate(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, "true");
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getLastBootRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return String.valueOf(new Random().nextInt(10)) + ":" + String.valueOf(new Random().nextInt(10) + 10);
    }
    
    
    public String getLastBootTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return String.valueOf(new Random().nextInt(20) + 20) + "." + String.valueOf(new Random().nextInt(10) + "°C");
    }
    
    
    public String getLastBootTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return String.valueOf(new Random().nextInt(20) + 20) + "." + String.valueOf(new Random().nextInt(10) + "°C");
    }
    
    
    public String getLastBootCountOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_COUNT, "true");
        
        return String.valueOf(new Random().nextInt(20));
    }
    
    
    public String getLastBootScreenOn() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_SCREEN, "true");
        
        return String.valueOf(new Random().nextInt(50));
    }
    
    
    public String getTotalBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        
        return generateDate(new Random().nextInt(DATE_OF_2012_05_04_IN_SEC));
    }
    
    
    public String getTotalUptime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getTotalUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, "true");
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getTotalDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_DATES, "true");
        this.psv.validate(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, "true");
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getTotalRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return String.valueOf(new Random().nextInt(10)) + ":" + String.valueOf(new Random().nextInt(10) + 10);
    }
    
    
    public String getTotalTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return String.valueOf(new Random().nextInt(20) + 20) + "." + String.valueOf(new Random().nextInt(10) + "°C");
    }
    
    
    public String getTotalTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_TEMPERATURE, "true");
        
        return String.valueOf(new Random().nextInt(20) + 20) + "." + String.valueOf(new Random().nextInt(10) + "°C");
    }
    
    
    public String getTotalCountOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_COUNT, "true");
        
        return String.valueOf(new Random().nextInt(1000));
    }
    
    
    public String getTotalScreenOn() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_SCREEN, "true");
        
        return String.valueOf(new Random().nextInt(100000));
    }
    
    
    public String uploadData() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_UPLOAD_DATA, "true");
        
        return "http://u.r.l/getStatistics";
    }
    
    
    /**
     * Generate a date
     * 
     * @param secondsSince1970
     * @return the string representation
     */
    private String generateDate(int secondsSince1970) {
        Date date = new Date(secondsSince1970 * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS z");
        return sdf.format(date);
    }
}
