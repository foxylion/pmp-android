package de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver;

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
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum;

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
    private EventEnum event;
    
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
     *            {@link EventEnum}
     */
    public CityLocationListener(Context context, LocationManager locManager, long timestamp, EventEnum event,
            String device) {
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
        EventEnum state = EventEnum.OFF;
        
        ConnectivityManager connManager = (ConnectivityManager) this.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        
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
        if (addresses != null && addresses.size() > 0) {
            city = addresses.get(0).getLocality();
        }
        locManager.removeUpdates(this);
        
        if (device.equals(DBConstants.DEVICE_BT)) {
            DBConnector.getInstance(context).storeBTEvent(this.timestamp, this.event, city);
        }
        if (device.equals(DBConstants.DEVICE_WIFI)) {
            DBConnector.getInstance(context).storeWifiEvent(this.timestamp, this.event, city, state);
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
