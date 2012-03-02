package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

/**
 * 
 * @author andres
 * 
 */
public class Check4Location extends TimerTask {
    
    private MapView mapView;
    private IAbsoluteLocation loc;
    private Controller ctrl;
    private Context context;
    private Handler handler;
    private IBinder binder;
    
    private int showAddress = 0;
    
    
    public Check4Location(MapView mapView, Context context, Handler handler, IBinder binder) {
        this.mapView = mapView;
        this.context = context;
        this.handler = handler;
        this.binder = binder;
        ctrl = new Controller();
    }
    
    
    @Override
    public void run() {
        
        if (binder == null) {
            return;
        }
        
        loc = IAbsoluteLocation.Stub.asInterface(binder);
        
        boolean isFixed = false;
        
        double longitude = 0.0;
        double latitude = 0.0;
        
        String country = "";
        String city = "";
        String address = "";
        
        try {
            try {
                isFixed = loc.isFixed();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                longitude = loc.getLongitude();
                Log.i(this, "Longitude: " + (float) loc.getLongitude());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                latitude = loc.getLatitude();
                Log.i(this, "Latitude: " + (float) loc.getLatitude());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                country = loc.getCountryName();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                city = loc.getLocality();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                address = loc.getAddress();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        final boolean isFixedD = isFixed;
        
        final double longitudeD = longitude;
        final double latitudeD = latitude;
        //        Log.i(this, "Lat: " + (float) latitudeD + ", Lng: " + (float) longitudeD);
        
        final String countryD = country;
        final String cityD = city;
        final String addressD = address;
        
        handler.post(new Runnable() {
            
            public void run() {
                
                if (isFixedD) {
                    MapController controller = mapView.getController();
                    try {
                        ViewModel.getInstance().setMyPosition((float) latitudeD, (float) longitudeD, 0);
                        /**
                         * send server updated latitude and longitude
                         */
                        switch (ctrl.userUpdatePos(Model.getInstance().getSid(), (float) loc.getLatitude(),
                                (float) loc.getLongitude())) {
                            case Constants.STATUS_UPDATED:
                                Log.i(this, "SEND LOCATION, STATUS UPDATED");
                                break;
                            case Constants.STATUS_UPTODATE:
                                Toast.makeText(context, "Status up to date", Toast.LENGTH_SHORT).show();
                                break;
                            case Constants.STATUS_ERROR:
                                Toast.makeText(context, "Error Update position", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        
                        controller.animateTo(new GeoPoint((int) (loc.getLatitude() * 1E6),
                                (int) (loc.getLongitude() * 1E6)));
                        controller.setZoom(17);
                        
                        // display address only once
                        if (showAddress == 0)
                            Toast.makeText(context, countryD + ", " + cityD + ", " + addressD, Toast.LENGTH_SHORT)
                                    .show();
                        showAddress++;
                        
                        // Start Check4Queries Class to check for queries
                        Check4Queries c4q = new Check4Queries();
                        ViewModel.getInstance().getQueryTimer().schedule(c4q, 300, 10000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    
                }
                
            }
        });
    }
    
}
