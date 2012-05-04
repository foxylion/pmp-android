package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.aidl.IEnergy;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSBatteryTemperatureEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSDeviceBatteryChargingUptimeEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSDeviceDatesEnum;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyImplCloak extends IEnergy.Stub {
    
    private PSValidator psv;
    
    
    public EnergyImplCloak(ResourceGroup rg) {
        this.psv = new PSValidator(rg);
    }
    
    
    public String getCurrentLevel() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_STATUS, "true");
        
        return "0";
    }
    
    
    public String getCurrentHealth() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_HEALTH, "true");
        
        return EnergyConstants.HEALTH_UNKNOWN;
    }
    
    
    public String getCurrentCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_STATUS, "true");
        
        return "No";
    }
    
    
    public String getCurrentChargingSource() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_SOURCE, "true");
        
        return EnergyConstants.PLUGGED_NOT_PLUGGED;
    }
    
    
    public String getCurrentChargingTime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_TIME, "true");
        
        return "0";
    }
    
    
    public String getCurrentTemperature() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.CURRENT);
        
        return "0°C";
    }
    
    
    public String getLastBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        
        return "0";
    }
    
    
    public String getLastBootUptime() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        
        return "0";
    }
    
    
    public String getLastBootUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return "0";
    }
    
    
    public String getLastBootDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return "0";
    }
    
    
    public String getLastBootRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return "0:0";
    }
    
    
    public String getLastBootTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return "0°C";
    }
    
    
    public String getLastBootTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
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
        this.psv.validate(PSDeviceDatesEnum.ALL);
        
        return "0";
    }
    
    
    public String getTotalUptime() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        
        return "0";
    }
    
    
    public String getTotalUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return "0";
    }
    
    
    public String getTotalDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return "0";
    }
    
    
    public String getTotalRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return "0:0";
    }
    
    
    public String getTotalTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return "0°C";
    }
    
    
    public String getTotalTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
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
