/*
 * Copyright 2011 pmp-android development team
 * Project: SimpleApp
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
package de.unistuttgart.ipvs.pmp.apps.locationtestapp.gui;

import java.util.Timer;
import java.util.TimerTask;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.locationtestapp.R;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

public class LocationAppActivity extends MapActivity {
    
    private static final String RG_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
    private static final String R_NAME = "absoluteLocation";
    
    private static final PMPResourceIdentifier R_ID = PMPResourceIdentifier.make(RG_NAME, R_NAME);
    
    public Handler handler;
    
    private TimerTask timerTask = new DefaultTimerTask();
    private Timer timer = null;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.handler = new Handler();
        
        PMP.get(getApplication());
        
        setContentView(R.layout.main);
        
        addListener();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        PMP.get().getResource(R_ID, new PMPRequestResourceHandler() {
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                resourceCached();
            }
            
            
            @Override
            public void onBindingFailed() {
                Toast.makeText(LocationAppActivity.this, "Binding Resource failed", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
        
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        
        if (binder == null) {
            return;
        }
        
        stopContinousLookup();
        
        IAbsoluteLocation loc = IAbsoluteLocation.Stub.asInterface(binder);
        try {
            loc.endLocationLookup();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    
    private void resourceCached() {
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        IAbsoluteLocation loc = IAbsoluteLocation.Stub.asInterface(binder);
        try {
            loc.startLocationLookup(1000, 10.0F);
            Toast.makeText(this, "Location Resource loaded.", Toast.LENGTH_SHORT).show();
            
            startContinousLookup();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
    };
    
    
    private void startContinousLookup() {
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }
    
    
    private void stopContinousLookup() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    
    
    private void addListener() {
        
    }
    
    private class DefaultTimerTask extends TimerTask {
        
        @Override
        public void run() {
            IBinder binder = PMP.get().getResourceFromCache(R_ID);
            
            if (binder == null) {
                return;
            }
            
            IAbsoluteLocation loc = IAbsoluteLocation.Stub.asInterface(binder);
            
            boolean isActive = false;
            boolean isAvailable = false;
            boolean isFixed = false;
            
            double longitude = 0.0;
            double latitude = 0.0;
            float speed = 0.0F;
            float accuracy = 0.0F;
            
            try {
                try {
                    isActive = loc.isActive();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                try {
                    isAvailable = loc.isGpsEnabled();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
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
                    speed = loc.getSpeed();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                try {
                    accuracy = loc.getAccuracy();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            
            final boolean isActiveD = isActive;
            final boolean isAvailableD = isAvailable;
            final boolean isFixedD = isFixed;
            
            final double longitudeD = longitude;
            final double latitudeD = latitude;
            final float speedD = speed;
            final float accuracyD = accuracy;
            
            handler.post(new Runnable() {
                
                public void run() {
                    
                    if (isFixedD) {
                        MapController controller = ((MapView) findViewById(R.id.MapView)).getController();
                        controller.animateTo(new GeoPoint(new Double(longitudeD * Math.pow(10, 6)).intValue(),
                                new Double(longitudeD * Math.pow(10, 6)).intValue()));
                    }
                    
                    ((TextView) findViewById(R.id.TextView_Information)).setText(Html.fromHtml("<html>"
                            + "<b>Longitude:</b> " + longitudeD + "<br/>" + "<b>Longitude:</b> " + latitudeD + "<br/>"
                            + "<b>Speed:</b> " + speedD + "<br/>" + "<b>Accuracy:</b> " + accuracyD + "</html>"));
                    
                    ((ToggleButton) findViewById(R.id.ToggleButton_Active)).setChecked(isActiveD);
                    ((ToggleButton) findViewById(R.id.ToggleButton_Avaiable)).setChecked(isAvailableD);
                    ((ToggleButton) findViewById(R.id.ToggleButton_Fixed)).setChecked(isFixedD);
                }
            });
        }
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
