/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * Handles location updates if location changes
 * WILL BE REPLACED BY Location-ResourceGroup
 * 
 * @author Andre Nguyen
 * 
 */
public class LocationUpdateHandler implements LocationListener {
    
    private Context context;
    private LocationManager locationManager;
    private MapView mapView;
    private MapController mapController;
    private GeoPoint gPosition;
    private Location location;
    private int whichHitcher;
    
    private Controller ctrl;
    
    long GPSupdateInterval; // In milliseconds
    float GPSmoveInterval; // In meters
    
    
    /**
     * 
     * @param context
     * @param locationManager
     * @param mapView
     * @param mapController
     * @param mapOverlay
     * @param gPosition
     */
    public LocationUpdateHandler(Context context, LocationManager locationManager, MapView mapView,
            MapController mapController, int whichHitcher) {
        this.context = context;
        this.locationManager = locationManager;
        this.mapView = mapView;
        this.mapController = mapController;
        this.whichHitcher = whichHitcher;
        
        this.ctrl = new Controller();
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, this.GPSupdateInterval,
                this.GPSmoveInterval, this);
    }
    
    
    @Override
    public void onLocationChanged(Location location) {
        
        /**
         * draw an overlay for driver or passenger
         */
        this.mapController = this.mapView.getController();
        
        int lat = (int) (location.getLatitude() * 1E6);
        int lng = (int) (location.getLongitude() * 1E6);
        this.gPosition = new GeoPoint(lat, lng);
        // Set my position to ViewModel
        ViewModel.getInstance().setMyPosition((float) location.getLatitude(), (float) location.getLongitude(),
                this.whichHitcher);
        Log.i(this, "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());
        
        /**
         * send server updated latitude and longitude
         */
        switch (this.ctrl.userUpdatePos(Model.getInstance().getSid(), (float) location.getLatitude(),
                (float) location.getLongitude())) {
            case Constants.STATUS_UPDATED:
                Toast.makeText(this.context, "Status updated", Toast.LENGTH_SHORT).show();
                break;
            case Constants.STATUS_UPTODATE:
                Toast.makeText(this.context, "Status up to date", Toast.LENGTH_SHORT).show();
                break;
            case Constants.STATUS_ERROR:
                Toast.makeText(this.context, "Error Update position", Toast.LENGTH_SHORT).show();
                break;
        }
        
        this.mapController = this.mapView.getController();
        this.mapController.setZoom(17);
        this.mapController.animateTo(this.gPosition);
        this.mapController.setCenter(this.gPosition);
        this.mapView.invalidate();
        
        showCurrentLocation();
        
    }
    
    
    @Override
    public void onProviderDisabled(String provider) {
        this.locationManager.removeUpdates(this);
    }
    
    
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        
    }
    
    
    /**
     * show current location as a street name
     */
    public void showCurrentLocation() {
        
        this.location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        if (this.location != null) {
            String address = convertPointToLocation(this.gPosition);
            Toast.makeText(this.context, address, Toast.LENGTH_SHORT).show();
        }
        
    }
    
    
    /**
     * convert location point to streetname
     * 
     * @param point
     * @return streetname of current position
     */
    public String convertPointToLocation(GeoPoint point) {
        StringBuffer address = new StringBuffer();
        Geocoder geoCoder = new Geocoder(this.context, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(point.getLatitudeE6() / 1E6,
                    point.getLongitudeE6() / 1E6, 1);
            
            if (addresses.size() > 0) {
                for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++) {
                    // address += addresses.get(0).getAddressLine(index) + " ";
                    address.append(address.toString() + addresses.get(0).getAddressLine(index) + " ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return address.toString();
    }
    
}
