package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.Timer; 
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
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.aidl.IAbsoluteLocation;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * 
 * @author andres
 * 
 */
public class Check4Location extends TimerTask {
    
    private static final String RG_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
    private static final String R_NAME = "absoluteLocationResource";
    
    private static final PMPResourceIdentifier R_ID = PMPResourceIdentifier.make(RG_NAME, R_NAME);
    
    private MapView mapView;
    private IAbsoluteLocation loc;
    private Controller ctrl;
    private Context context;
    private Handler handler;
    private Timer queryTimer;
    
    
    public Check4Location(MapView mapView, Context context, Handler handler, Timer queryTimer) {
        this.mapView = mapView;
        this.context = context;
        this.handler = handler;
        this.queryTimer = queryTimer;
        ctrl = new Controller();
    }
    
    
    @Override
    public void run() {
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        Log.i(this, "In Timer");
        
        if (binder == null) {
            Log.i(this, "Binder null");
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
                Log.i(this, "Longitude: " + longitude);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                latitude = loc.getLatitude();
                Log.i(this, "Latitude: " + latitude);
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
            Log.i(this, "Catch");
        }
        
        final boolean isFixedD = isFixed;
        
        final double longitudeD = longitude;
        final double latitudeD = latitude;
        Log.i(this, "Lat: " + latitudeD + ", Lng: " + longitudeD);
        
        final String countryD = country;
        final String cityD = city;
        final String addressD = address;
        
        handler.post(new Runnable() {
            
            public void run() {
                
                Log.i(this, "In handerl");
                
                if (isFixedD) {
                    MapController controller = mapView.getController();
                    try {
                        ViewModel.getInstance().setMyPosition((float) loc.getLatitude(), (float) loc.getLongitude(), 0);
                        /**
                         * send server updated latitude and longitude
                         */
                        switch (ctrl.userUpdatePos(Model.getInstance().getSid(), (float) loc.getLatitude(),
                                (float) loc.getLongitude())) {
                            case Constants.STATUS_UPDATED:
                                //                                Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
                                Log.i(this, "SEND LOCATION, STATUS UPDATED");
                                break;
                            case Constants.STATUS_UPTODATE:
                                Toast.makeText(context, "Status up to date", Toast.LENGTH_SHORT).show();
                                break;
                            case Constants.STATUS_ERROR:
                                Toast.makeText(context, "Error Update position", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        
                        controller.animateTo(new GeoPoint((int) (latitudeD * 1E6), (int) (longitudeD * 1E6)));
                        controller.setZoom(17);
                        
                        Toast.makeText(context, countryD + ", " + cityD + ", " + addressD, Toast.LENGTH_SHORT).show();
                        
                        // Start Check4Queries Class to check for queries
                        Check4Queries c4q = new Check4Queries();
                        queryTimer = new Timer();
                        queryTimer.schedule(c4q, 300, 10000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    
                }
                
            }
        });
    }
    
}
