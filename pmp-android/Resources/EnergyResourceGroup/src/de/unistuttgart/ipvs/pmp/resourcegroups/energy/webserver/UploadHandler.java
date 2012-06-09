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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.webserver;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
 * This is the handler for uploading data to the webserver
 * 
 * @author Marcus Vetter
 * 
 */
public class UploadHandler {
    
    private Service service;
    private IDBConnector dbc;
    
    
    public UploadHandler(Context context) {
        
        // Create service
        this.service = new Service(Service.DEFAULT_URL, getDeviceID(context));
        
        // Get the db connection
        this.dbc = DBConnector.getInstance(context);
    }
    
    
    public String getDeviceID(Context context) {
        // Get the device id
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID = tManager.getDeviceId();
        MessageDigest digest;
        String deviceIDHashed = "";
        
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(deviceID.getBytes(), 0, deviceID.length());
            deviceIDHashed = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e1) {
        }
        
        return deviceIDHashed != null ? deviceIDHashed : deviceID;
        
    }
    
    
    public boolean upload() {
        List<BatteryEvent> eventList = new ArrayList<BatteryEvent>();
        
        ResultSetUpload rsu = this.dbc.getUploadValues();
        
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
            
            System.out.println("Voltage = " + voltage + " (Data-Type: " + ((Object) voltage).getClass() + ")");
            
            // Add the data to the list
            eventList.add(new BatteryEvent(timestamp, level, voltage, plugged, present, status, temperature));
        }
        
        BatteryEventManager bem = new BatteryEventManager(this.service);
        try {
            bem.commitEvents(eventList);
            
            // Clear the database
            this.dbc.clearDatabase();
            
            return true;
        } catch (InternalDatabaseException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (InvalidEventOrderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
