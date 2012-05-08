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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum;

/**
 * {@link BroadcastReceiver} to get the bluetooth events
 * 
 * @author Thorsten Berberich
 * 
 */
public class BluetoothReceiver extends BroadcastReceiver {
    
    /**
     * The timestamp
     */
    private long time;
    
    /**
     * The evnt
     */
    private EventEnum event;
    
    /**
     * The context
     */
    private Context context;
    
    /**
     * The location manager
     */
    private LocationManager locManager;
    
    
    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        time = new Date().getTime();
        DBConnector.getInstance(context).open();
        
        // Get state or not available 10 = off
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 10);
        
        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                event = EventEnum.OFF;
                DBConnector.getInstance(context).storeBTEvent(time, event, null);
                DBConnector.getInstance(context).close();
                break;
            case BluetoothAdapter.STATE_ON:
                event = EventEnum.ON;
                storeEvent();
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
    private void storeEvent() {
        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new CityLocationListener());
        } else if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new CityLocationListener());
        } else {
            DBConnector.getInstance(context).storeBTEvent(time, event, null);
            DBConnector.getInstance(context).close();
        }
    }
    
    /**
     * Stores the event with the location at the database
     * 
     * @author Thorsten Berberich
     * 
     */
    class CityLocationListener implements LocationListener {
        
        /* (non-Javadoc)
         * @see android.location.LocationListener#onLocationChanged(android.location.Location)
         */
        @Override
        public void onLocationChanged(Location location) {
            // New geocoder to get the city
            Geocoder gc = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String city = null;
            
            // Try to get the city
            if (addresses.size() > 0 && addresses != null) {
                city = addresses.get(0).getLocality();
            }
            locManager.removeUpdates(this);
            DBConnector.getInstance(context).storeBTEvent(time, event, city);
            DBConnector.getInstance(context).close();
        }
        
        
        /* (non-Javadoc)
         * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
         */
        @Override
        public void onProviderDisabled(String provider) {
        }
        
        
        /* (non-Javadoc)
         * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
         */
        @Override
        public void onProviderEnabled(String provider) {
        }
        
        
        /* (non-Javadoc)
         * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        
    }
}
