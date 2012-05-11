package de.unistuttgart.ipvs.pmp.resourcegroups.energy.webserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.BatteryEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent.Adapter;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent.Status;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventOrderException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.IDBConnector;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class UploadHandler {
    
    private Service service;
    private IDBConnector dbc;
    
    
    public UploadHandler(Context context) {
        // Get the device id
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tManager.getDeviceId();
        
        // Create service
        this.service = new Service(Service.DEFAULT_URL, deviceId);
        
        // Get the db connection
        this.dbc = DBConnector.getInstance(context);
    }
    
    
    public String upload() {
        List<BatteryEvent> eventList = new ArrayList<BatteryEvent>();
        
        ResultSetUpload rsu = dbc.getUploadValues();
        
        for (de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent be : rsu.getBatteryEvents()) {
            
            // Timestamp
            long timestamp = be.getTimestamp();
            
            // Level
            int level = be.getLevel();
            
            // Plugged
            Adapter plugged;
            if (be.getPlugged().equals(EnergyConstants.PLUGGED_AC)) {
                plugged = Adapter.AC;
            } else if (be.getPlugged().equals(EnergyConstants.PLUGGED_USB)) {
                plugged = Adapter.USB;
            } else {
                plugged = Adapter.NOT_PLUGGED;
            }
            
            // Present
            boolean present = be.isPresent();
            
            // Status
            Status status;
            if (be.getStatus().equals(EnergyConstants.STATUS_CHARGING)) {
                status = Status.CHARGING;
            } else if (be.getStatus().equals(EnergyConstants.STATUS_DISCHARGING)) {
                status = Status.DISCHARGING;
            } else if (be.getStatus().equals(EnergyConstants.STATUS_FULL)) {
                status = Status.FULL;
            } else if (be.getStatus().equals(EnergyConstants.STATUS_NOT_CHARGING)) {
                status = Status.NOT_CHARGING;
            } else {
                status = Status.UNKNOWN;
            }
            
            // Temperature
            float temperature = be.getTemperature();
            
            // Voltage
            int voltage = be.getVoltage();
            
            // Add the data to the list
            eventList.add(new BatteryEvent(timestamp, level, voltage, plugged, present, status, temperature));
        }
        
        BatteryEventManager bem = new BatteryEventManager(this.service);
        try {
            bem.commitEvents(eventList);
            
            // TODO: Clear database
            
            // TODO: URL
            return "URL";
        } catch (InternalDatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidEventOrderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
