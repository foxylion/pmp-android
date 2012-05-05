package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
public class EnergyImplMock extends IEnergy.Stub {
    
    private static final int DATE_OF_2012_05_04_IN_SEC = 1336129383;
    
    private PSValidator psv;
    
    
    public EnergyImplMock(ResourceGroup rg) {
        this.psv = new PSValidator(rg);
    }
    
    
    public String getCurrentLevel() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_STATUS, "true");
        
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
    
    
    public String getCurrentCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_STATUS, "true");
        
        if (new Random().nextBoolean()) {
            return "Yes";
        } else {
            return "No";
        }
    }
    
    
    public String getCurrentChargingSource() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_SOURCE, "true");
        
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
    
    
    public String getCurrentChargingTime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_TIME, "true");
        
        return String.valueOf(new Random().nextInt());
    }
    
    
    public String getCurrentTemperature() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.CURRENT);
        
        return String.valueOf(new Random().nextInt(20) + 20) + "." + String.valueOf(new Random().nextInt(10) + "°C");
    }
    
    
    public String getLastBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        
        return generateDate(new Random().nextInt(DATE_OF_2012_05_04_IN_SEC));
    }
    
    
    public String getLastBootUptime() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getLastBootUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getLastBootDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
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
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return String.valueOf(new Random().nextInt(20) + 20) + "." + String.valueOf(new Random().nextInt(10) + "°C");
    }
    
    
    public String getLastBootTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
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
        this.psv.validate(PSDeviceDatesEnum.ALL);
        
        return generateDate(new Random().nextInt(DATE_OF_2012_05_04_IN_SEC));
    }
    
    
    public String getTotalUptime() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getTotalUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return String.valueOf(new Random().nextInt(20)) + "h " + String.valueOf(new Random().nextInt(59)) + "m "
                + String.valueOf(new Random().nextInt(59)) + "s";
    }
    
    
    public String getTotalDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
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
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return String.valueOf(new Random().nextInt(20) + 20) + "." + String.valueOf(new Random().nextInt(10) + "°C");
    }
    
    
    public String getTotalTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
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
