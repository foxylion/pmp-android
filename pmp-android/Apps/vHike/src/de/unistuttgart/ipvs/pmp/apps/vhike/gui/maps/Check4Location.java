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
        this.ctrl = new Controller();
    }
    
    
    @Override
    public void run() {
        
        if (this.binder == null) {
            return;
        }
        
        this.loc = IAbsoluteLocation.Stub.asInterface(this.binder);
        
        boolean isFixed = false;
        
        double longitude = 0.0;
        double latitude = 0.0;
        
        String country = "";
        String city = "";
        String address = "";
        
        try {
            try {
                isFixed = this.loc.isFixed();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                longitude = this.loc.getLongitude();
                Log.i(this, "Longitude: " + (float) this.loc.getLongitude());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                latitude = this.loc.getLatitude();
                Log.i(this, "Latitude: " + (float) this.loc.getLatitude());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                country = this.loc.getCountryName();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                city = this.loc.getLocality();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                address = this.loc.getAddress();
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
        
        this.handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                if (isFixedD) {
                    MapController controller = Check4Location.this.mapView.getController();
                    try {
                        ViewModel.getInstance().setMyPosition((float) latitudeD, (float) longitudeD, 0);
                        /**
                         * send server updated latitude and longitude
                         */
                        switch (Check4Location.this.ctrl.userUpdatePos(Model.getInstance().getSid(),
                                (float) Check4Location.this.loc.getLatitude(),
                                (float) Check4Location.this.loc.getLongitude())) {
                            case Constants.STATUS_UPDATED:
                                Log.i(this, "SEND LOCATION, STATUS UPDATED");
                                break;
                            case Constants.STATUS_UPTODATE:
                                Toast.makeText(Check4Location.this.context, "Status up to date", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case Constants.STATUS_ERROR:
                                Toast.makeText(Check4Location.this.context, "Error Update position", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                        }
                        
                        controller.animateTo(new GeoPoint((int) (Check4Location.this.loc.getLatitude() * 1E6),
                                (int) (Check4Location.this.loc.getLongitude() * 1E6)));
                        controller.setZoom(17);
                        
                        // display address only once
                        if (Check4Location.this.showAddress == 0) {
                            Toast.makeText(Check4Location.this.context, countryD + ", " + cityD + ", " + addressD,
                                    Toast.LENGTH_SHORT).show();
                        }
                        Check4Location.this.showAddress++;
                        
                        // Start Check4Queries Class to check for queries
                        Check4Queries c4q = new Check4Queries();
                        ViewModel.getInstance().getQueryTimer().schedule(c4q, 10000, 10000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    
                }
                
            }
        });
    }
    
}
