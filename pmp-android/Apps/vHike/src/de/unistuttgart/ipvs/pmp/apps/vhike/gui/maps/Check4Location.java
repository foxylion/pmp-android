/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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

import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
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
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * 
 * @author andres
 * 
 */
public class Check4Location extends TimerTask {
    
    private MapView mapView;
    private IAbsoluteLocation loc;
    private Controller ctrl;
    private Context mContext;
    private Handler handler;
    private int whichHitcher;
    private ProgressDialog pDialog;
    private int showAddress = 0;
    
    
    public Check4Location(IvHikeWebservice ws, IAbsoluteLocation loc, MapView mapView, Context context,
            Handler handler, int whichHitcher) {
        this.mapView = mapView;
        this.mContext = context;
        this.handler = handler;
        this.ctrl = new Controller(ws);
        this.loc = loc;
        this.whichHitcher = whichHitcher;
        
        this.pDialog = ProgressDialog.show(context, "Location", "Getting location...");
        this.pDialog.setOnCancelListener(new OnCancelListener() {
            
            @Override
            public void onCancel(DialogInterface dialog) {
                ((Activity) Check4Location.this.mContext).finish();
            }
        });
        this.pDialog.setCancelable(true);
    }
    
    
    @Override
    public void run() {
        Log.i(this, "Location in");
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
                    
                    if (latitudeD == 0.0 && longitudeD == 0.0) {
                        Log.i(this, "Lat/Lon 0.0");
                        if (!Check4Location.this.pDialog.isShowing()) {
                            Check4Location.this.pDialog.show();
                            Log.i(this, "Dialog Show");
                        }
                        ViewModel.getInstance().denyStartSearch4Query();
                    } else {
                        Log.i(this, "Lat/Lon not 0.0");
                        if (Check4Location.this.pDialog.isShowing()) {
                            Check4Location.this.pDialog.dismiss();
                            Log.i(this, "Dialog Dismiss");
                        }
                        ViewModel.getInstance().setStartSearch4Query();
                    }
                    
                    try {
                        if ((latitudeD != 0.0) && (longitudeD != 0.0)) {
                            ViewModel.getInstance().setMyPosition((float) latitudeD, (float) longitudeD,
                                    Check4Location.this.whichHitcher);
                            
                            /**
                             * send server updated latitude and longitude
                             */
                            switch (Check4Location.this.ctrl.userUpdatePos(Model.getInstance().getSid(),
                                    (float) Check4Location.this.loc.getLatitude(),
                                    (float) Check4Location.this.loc.getLongitude())) {
                                case Constants.STATUS_UPDATED:
                                    //location send
                                    break;
                                case Constants.STATUS_UPTODATE:
                                    //Up to date
                                    break;
                                case Constants.STATUS_ERROR:
                                    //Error
                                    break;
                            }
                            //                        Log.i(this, "Latitude: " + Check4Location.this.loc.getLatitude() * 1E6 + ", " + "Longtitude: "
                            //                                + Check4Location.this.loc.getLongitude() * 1E6);
                            
                            controller.animateTo(new GeoPoint(
                                    (int) (Check4Location.this.loc.getLatitude() * 1E6),
                                    (int) (Check4Location.this.loc.getLongitude() * 1E6)));
                            controller.setZoom(14);
                            
                            // display address only once
                            if (Check4Location.this.showAddress == 0) {
                                Toast.makeText(Check4Location.this.mContext,
                                        countryD + ", " + cityD + ", " + addressD,
                                        Toast.LENGTH_SHORT).show();
                            }
                            Check4Location.this.showAddress++;
                            
                            if (ViewModel.getInstance().locationIsCanceled()) {
                                cancel();
                                Log.i(this, "Canceled location");
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    
                }
                
            }
        });
    }
}
