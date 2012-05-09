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

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.locationtestapp.R;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

public class LocationAppActivity extends MapActivity {
    
    private static final String RG_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
    private static final String R_NAME = "absoluteLocationResource";
    
    private static final PMPResourceIdentifier R_ID = PMPResourceIdentifier.make(RG_NAME, R_NAME);
    
    public Handler handler;
    
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
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
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
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    
    
    private void resourceCached() {
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        
        if (binder == null) {
            
            this.handler.post(new Runnable() {
                
                public void run() {
                    Toast.makeText(LocationAppActivity.this,
                            "PMP said something like 'resource group does not exists'.", Toast.LENGTH_SHORT).show();
                }
            });
            
            return;
        }
        
        IAbsoluteLocation loc = IAbsoluteLocation.Stub.asInterface(binder);
        try {
            loc.startLocationLookup(1000, 10.0F);
            
            this.handler.post(new Runnable() {
                
                public void run() {
                    Toast.makeText(LocationAppActivity.this, "Location Resource loaded.", Toast.LENGTH_SHORT).show();
                }
            });
            
            startContinousLookup();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
            this.handler.post(new Runnable() {
                
                public void run() {
                    Toast.makeText(LocationAppActivity.this, "Please enable the Service Feature.", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
        
    };
    
    
    private void startContinousLookup() {
        this.timer = new Timer();
        this.timer.schedule(new DefaultTimerTask(), 1000, 1000);
    }
    
    
    private void stopContinousLookup() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
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
            
            try {
                
                boolean isActive = false;
                boolean isAvailable = false;
                boolean isFixed = false;
                
                double longitude = 0.0;
                double latitude = 0.0;
                float speed = 0.0F;
                float accuracy = 0.0F;
                
                String country = "";
                String countryCode = "";
                String locality = "";
                String postalCode = "";
                String address = "";
                
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
                    try {
                        country = loc.getCountryName();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    try {
                        countryCode = loc.getCountryCode();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    try {
                        locality = loc.getLocality();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    try {
                        postalCode = loc.getPostalCode();
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
                
                final boolean isActiveD = isActive;
                final boolean isAvailableD = isAvailable;
                final boolean isFixedD = isFixed;
                
                final double longitudeD = longitude;
                final double latitudeD = latitude;
                final float speedD = speed;
                final float accuracyD = accuracy;
                
                final String countryD = country;
                final String countryCodeD = countryCode;
                final String localityD = locality;
                final String postalCodeD = postalCode;
                final String addressD = address;
                
                LocationAppActivity.this.handler.post(new Runnable() {
                    
                    public void run() {
                        
                        if (isFixedD) {
                            MapController controller = ((MapView) findViewById(R.id.MapView)).getController();
                            controller.animateTo(new GeoPoint((int) (latitudeD * 1E6), (int) (longitudeD * 1E6)));
                            controller.setZoom(17);
                        }
                        
                        ((TextView) findViewById(R.id.TextView_Information)).setText(Html.fromHtml("<html>"
                                + "<b>Longitude:</b> "
                                + longitudeD
                                + "<br/>"
                                + "<b>Longitude:</b> "
                                + latitudeD
                                + "<br/>"
                                + "<b>Speed:</b> "
                                + speedD
                                + "<br/>"
                                + "<b>Accuracy:</b> "
                                + accuracyD
                                + "<br>"
                                + "<b>Details to Location:</b><br/>"
                                + countryD
                                + "; "
                                + countryCodeD
                                + "<br/>"
                                + localityD + "; " + postalCodeD + "<br/>" + addressD + "</html>"));
                        
                        ((ToggleButton) findViewById(R.id.ToggleButton_Active)).setChecked(isActiveD);
                        ((ToggleButton) findViewById(R.id.ToggleButton_Avaiable)).setChecked(isAvailableD);
                        ((ToggleButton) findViewById(R.id.ToggleButton_Fixed)).setChecked(isFixedD);
                    }
                });
                
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
