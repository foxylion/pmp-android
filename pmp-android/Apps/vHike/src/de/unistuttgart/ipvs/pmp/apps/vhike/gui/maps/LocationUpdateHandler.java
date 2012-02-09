package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Handles location updates
 * 
 * @author Andre
 * 
 */
public class LocationUpdateHandler implements LocationListener {
    
    private Context context;
    private LocationManager locationManager;
    private MapView mapView;
    private MapController mapController;
    private GeoPoint gPosition;
    private Location location;
    
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
            MapController mapController) {
        this.context = context;
        this.locationManager = locationManager;
        this.mapView = mapView;
        this.mapController = mapController;
        
        ctrl = new Controller();
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPSupdateInterval, GPSmoveInterval,
                (LocationListener) this);
    }
    
    
    public void onLocationChanged(Location location) {
        
        /**
         * draw an overlay for driver or passenger
         */
        mapController = mapView.getController();
        
        int lat = (int) (location.getLatitude() * 1E6);
        int lng = (int) (location.getLongitude() * 1E6);
        gPosition = new GeoPoint(lat, lng);
        // Set my position to ViewModel
        ViewModel.getInstance().setMyPosition((float) location.getLatitude(), (float) location.getLongitude());
        Log.i(this, "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());
        
        /**
         * send server updated latitude and longitude
         */
        switch (ctrl.userUpdatePos(Model.getInstance().getSid(), (float) location.getLatitude(),
                (float) location.getLongitude())) {
            case Constants.STATUS_UPDATED:
                Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
                break;
            case Constants.STATUS_UPTODATE:
                Toast.makeText(context, "Status up to date", Toast.LENGTH_SHORT).show();
                break;
            case Constants.STATUS_ERROR:
                Toast.makeText(context, "Error Update position", Toast.LENGTH_SHORT).show();
                break;
        }
        
        mapController = mapView.getController();
        mapController.setZoom(17);
        mapController.animateTo(gPosition);
        mapController.setCenter(gPosition);
        mapView.invalidate();
        
        showCurrentLocation();
        
    }
    
    
    public void onProviderDisabled(String provider) {
        locationManager.removeUpdates(this);
    }
    
    
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        
    }
    
    
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        
    }
    
    
    /**
     * show current location as a street name
     */
    public void showCurrentLocation() {
        
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        if (location != null) {
            String address = convertPointToLocation(gPosition);
            Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
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
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(point.getLatitudeE6() / 1E6,
                    point.getLongitudeE6() / 1E6, 1);
            
            if (addresses.size() > 0) {
                for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
                    // address += addresses.get(0).getAddressLine(index) + " ";
                    address.append(address.toString() + addresses.get(0).getAddressLine(index) + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return address.toString();
    }
    
}
