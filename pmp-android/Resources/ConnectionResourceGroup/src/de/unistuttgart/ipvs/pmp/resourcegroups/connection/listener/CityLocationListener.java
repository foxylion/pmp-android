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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.listener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.Events;

/**
 * Stores the event with the location at the database
 * 
 * @author Thorsten Berberich
 * 
 */
public class CityLocationListener implements LocationListener {
    
    /**
     * Timestamp of the event
     */
    private long timestamp;
    
    /**
     * Event
     */
    private Events event;
    
    /**
     * LocationManager
     */
    private LocationManager locManager;
    
    /**
     * {@link Context}
     */
    private Context context;
    
    /**
     * Source of the event
     */
    private String device;
    
    
    /**
     * Create a new {@link CityLocationListener}
     * 
     * @param timestamp
     *            timestamp of the event to store
     * @param event
     *            {@link Events}
     */
    public CityLocationListener(Context context, LocationManager locManager, long timestamp, Events event, String device) {
        this.context = context;
        this.locManager = locManager;
        this.timestamp = timestamp;
        this.event = event;
        this.device = device;
    }
    
    
    /* (non-Javadoc)
     * @see android.location.LocationListener#onLocationChanged(android.location.Location)
     */
    @Override
    public void onLocationChanged(Location location) {
        Events state = Events.OFF;
        
        // Get the connectivity manager
        ConnectivityManager connManager = (ConnectivityManager) this.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        
        // Check if the state of the wifi
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi == null) {
                state = Events.OFF;
            } else {
                if (mWifi.isConnected()) {
                    state = Events.ON;
                }
            }
        }
        
        // New geocoder to get the city
        Geocoder gc = new Geocoder(this.context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city = null;
        
        // Try to get the city
        if (addresses != null && addresses.size() > 0) {
            city = addresses.get(0).getLocality();
        }
        this.locManager.removeUpdates(this);
        
        if (this.device.equals(DBConstants.DEVICE_BT)) {
            DBConnector.getInstance(this.context).storeBTEvent(this.timestamp, this.event, city);
        }
        if (this.device.equals(DBConstants.DEVICE_WIFI)) {
            DBConnector.getInstance(this.context).storeWifiEvent(this.timestamp, this.event, city, state);
        }
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
