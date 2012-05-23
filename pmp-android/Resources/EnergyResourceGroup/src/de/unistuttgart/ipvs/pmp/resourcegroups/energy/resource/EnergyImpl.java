package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import android.content.Context;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.infoapp.graphs.UrlBuilder;
import de.unistuttgart.ipvs.pmp.infoapp.graphs.UrlBuilder.Views;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.aidl.IEnergy;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.IDBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSBatteryTemperatureEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSDeviceBatteryChargingUptimeEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSDeviceDatesEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetCurrentValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetLastBootValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetTotalValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.webserver.UploadHandler;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyImpl extends IEnergy.Stub {
    
    private PSValidator psv;
    
    private Context context;
    
    private IDBConnector dbc;
    
    
    public EnergyImpl(ResourceGroup rg, String appIdentifier) {
        this.psv = new PSValidator(rg, appIdentifier);
        this.context = rg.getContext(appIdentifier);
        this.dbc = DBConnector.getInstance(this.context);
    }
    
    
    public String getCurrentLevel() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_LEVEL, "true");
        
        return getResultSetCV().getLevel();
    }
    
    
    public String getCurrentHealth() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_HEALTH, "true");
        
        return getResultSetCV().getHealth();
    }
    
    
    public String getCurrentStatus() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_STATUS, "true");
        
        return getResultSetCV().getStatus();
    }
    
    
    public String getCurrentPlugged() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_PLUGGED, "true");
        
        return getResultSetCV().getPlugged();
    }
    
    
    public String getCurrentStatusTime() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_STATUS_TIME, "true");
        
        return getResultSetCV().getStatusTime();
    }
    
    
    public String getCurrentTemperature() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.CURRENT);
        
        return getResultSetCV().getTemperature();
    }
    
    
    public String getLastBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        
        return getResultSetLBV().getDate();
    }
    
    
    public String getLastBootUptime() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        
        return getResultSetLBV().getUptime();
    }
    
    
    public String getLastBootUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return getResultSetLBV().getUptimeBattery();
    }
    
    
    public String getLastBootDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.LAST_BOOT);
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return getResultSetLBV().getDurationOfCharging();
    }
    
    
    public String getLastBootRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return getResultSetLBV().getRatio();
    }
    
    
    public String getLastBootTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return getResultSetLBV().getTemperaturePeak();
    }
    
    
    public String getLastBootTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return getResultSetLBV().getTemperatureAverage();
    }
    
    
    public String getLastBootCountOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_COUNT, "true");
        
        return getResultSetLBV().getCountOfCharging();
    }
    
    
    public String getLastBootScreenOn() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_SCREEN, "true");
        
        return getResultSetLBV().getScreenOn();
    }
    
    
    public String getTotalBootDate() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        
        return getResultSetTV().getDate();
    }
    
    
    public String getTotalUptime() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        
        return getResultSetTV().getUptime();
    }
    
    
    public String getTotalUptimeBattery() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return getResultSetTV().getUptimeBattery();
    }
    
    
    public String getTotalDurationOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(PSDeviceDatesEnum.ALL);
        this.psv.validate(PSDeviceBatteryChargingUptimeEnum.ALL);
        
        return getResultSetTV().getDurationOfCharging();
    }
    
    
    public String getTotalRatio() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_RATIO, "true");
        
        return getResultSetTV().getRatio();
    }
    
    
    public String getTotalTemperaturePeak() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return getResultSetTV().getTemperaturePeak();
    }
    
    
    public String getTotalTemperatureAverage() throws RemoteException {
        // Check permission
        this.psv.validate(PSBatteryTemperatureEnum.ALL);
        
        return getResultSetTV().getTemperatureAverage();
    }
    
    
    public String getTotalCountOfCharging() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_BATTERY_CHARGING_COUNT, "true");
        
        return getResultSetTV().getCountOfCharging();
    }
    
    
    public String getTotalScreenOn() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_DEVICE_SCREEN, "true");
        
        return getResultSetTV().getScreenOn();
    }
    
    
    public String uploadData() throws RemoteException {
        // Check permission
        this.psv.validate(EnergyConstants.PS_UPLOAD_DATA, "true");
        
        UploadHandler uh = new UploadHandler(this.context);
        if (uh.upload()) {
            UrlBuilder urlB = new UrlBuilder(UrlBuilder.DEFAULT_URL, uh.getDeviceID(this.context));
            urlB.setView(Views.STATIC);
            return urlB.getBatteryGraphUrl();
        } else {
            return null;
        }
        
    }
    
    
    private ResultSetCurrentValues getResultSetCV() {
        return dbc.getCurrentValues();
    }
    
    
    private ResultSetLastBootValues getResultSetLBV() {
        return dbc.getLastBootValues();
    }
    
    
    private ResultSetTotalValues getResultSetTV() {
        return dbc.getTotalValues();
    }
    
}
