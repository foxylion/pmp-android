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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum;

/**
 * {@link BroadcastReceiver} for Wifi connection changed events
 * 
 * @author Thorsten Berberich
 */
public class WifiReceiver extends BroadcastReceiver {
    
    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        EventEnum state = EventEnum.OFF;
        
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi == null) {
                state = EventEnum.OFF;
            } else {
                if (mWifi.isConnected()) {
                    state = EventEnum.ON;
                }
            }
        }
        
        long time = new Date().getTime();
        
        // Get the ConnectivityManager
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        
        // Get the network information for the wifi if the ConnectivityManager is not null
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        
        // Check if the wifi is connected or not
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                //Connected
                storeEvent(context, time, EventEnum.ON);
            } else {
                // Not Connected
                DBConnector.getInstance(context).storeWifiEvent(time, EventEnum.OFF, null, state);
            }
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
                    locManager, time, event, DBConstants.DEVICE_WIFI));
        } else if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new CityLocationListener(context,
                    locManager, time, event, DBConstants.DEVICE_WIFI));
        } else {
            DBConnector.getInstance(context).storeBTEvent(time, event, null);
        }
    }
}
