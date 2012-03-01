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
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

/**
 * 
 * @author andres
 *
 */
public class Check4Location extends TimerTask {
    
    private static final String RG_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
    private static final String R_NAME = "absoluteLocationResource";
    
    private static final PMPResourceIdentifier R_ID = PMPResourceIdentifier.make(RG_NAME, R_NAME);
    
    private Handler handler;
    private MapView mapView;
    private IAbsoluteLocation loc;
    private Controller ctrl;
    private Context context;
    
    
    public Check4Location(Handler handler, MapView mapView, Context context) {
        this.handler = handler;
        this.mapView = mapView;
        this.context = context;
        ctrl = new Controller();
    }
    
    
    @Override
    public void run() {
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        
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
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                latitude = loc.getLatitude();
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
        
        final String countryD = country;
        final String cityD = city;
        final String addressD = address;
        
        handler.post(new Runnable() {
            
            public void run() {
                
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
                        Timer queriesTimer = new Timer();
                        queriesTimer.schedule(c4q, 300, 10000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    
                }
                
            }
        });
    }
    
}
