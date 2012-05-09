/*
 * Copyright 2012 pmp-android development team
 * Project: ConnectionResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver;

import java.util.Date;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum;

/**
 * {@link BroadcastReceiver} to get the bluetooth events
 * 
 * @author Thorsten Berberich
 * 
 */
public class BluetoothReceiver extends BroadcastReceiver {
    
    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        long time = new Date().getTime();
        
        // Get state or not available 10 = off
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 10);
        
        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                DBConnector.getInstance(context).storeBTEvent(time, EventEnum.OFF, null);
                break;
            case BluetoothAdapter.STATE_ON:
                storeEvent(context, time, EventEnum.ON);
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                break;
        }
    }
    
    
    /**
     * Store a event an try to get the location
     */
    private void storeEvent(Context context, long time, EventEnum event) {
        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        
        //Get the GPS Provider
        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        
        // Get the network provider
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
        }
        
        // Get the network information for the wifi if the ConnectivityManager is not null
        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new CityLocationListener(context,
                    locManager, time, event, DBConstants.DEVICE_BT));
        } else if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new CityLocationListener(context,
                    locManager, time, event, DBConstants.DEVICE_BT));
        } else {
            DBConnector.getInstance(context).storeBTEvent(time, event, null);
        }
    }
    
}
