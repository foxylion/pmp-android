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
public class EnergyImpl extends IEnergy.Stub {
    
    private PSValidator psv;
    
    
    public EnergyImpl(ResourceGroup rg) {
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
        
        return null;
    }
    
    
    public String getCurrentCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_STATUS, "true");
        
        return "No";
    }
    
    
    public String getCurrentChargingSource() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_SOURCE, "true");
        
        return null;
    }
    
    
    public String getCurrentChargingTime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_TIME, "true");
        
        return null;
    }
    
    
    public String getCurrentTemperature() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.CURRENT);
        
        return null;
    }
    
    
    public String getLastBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        
        return null;
    }
    
    
    public String getLastBootUptime() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        
        return null;
    }
    
    
    public String getLastBootUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return null;
    }
    
    
    public String getLastBootDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return null;
    }
    
    
    public String getLastBootRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return null;
    }
    
    
    public String getLastBootTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return null;
    }
    
    
    public String getLastBootTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return null;
    }
    
    
    public String getLastBootCountOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_COUNT, "true");
        
        return null;
    }
    
    
    public String getLastBootScreenOn() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_SCREEN, "true");
        
        return null;
    }
    
    
    public String getTotalBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        
        return null;
    }
    
    
    public String getTotalUptime() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        
        return null;
    }
    
    
    public String getTotalUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return null;
    }
    
    
    public String getTotalDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return null;
    }
    
    
    public String getTotalRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return null;
    }
    
    
    public String getTotalTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return null;
    }
    
    
    public String getTotalTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return null;
    }
    
    
    public String getTotalCountOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_COUNT, "true");
        
        return null;
    }
    
    
    public String getTotalScreenOn() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_SCREEN, "true");
        
        return null;
    }
    
    
    public String uploadData() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_UPLOAD_DATA, "true");
        
        return null;
    }
    
}
