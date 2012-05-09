/*
 * Copyright 2012 pmp-android development team
 * Project: ConnectionResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum;

/**
 * @author Thorsten
 * 
 */
public class TestActivity extends Activity {
    
    private int signal = 0;
    Context context;
    
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        context = this;
        this.setContentView(R.layout.main);
        DBConnector.getInstance(this).open();
        DBConnector.getInstance(this).storeWifiEvent(100, EventEnum.ON, "bla");
        DBConnector.getInstance(this).storeWifiEvent(110, EventEnum.OFF, "bla");
        DBConnector.getInstance(this).storeWifiEvent(120, EventEnum.ON, "bla");
        DBConnector.getInstance(this).storeWifiEvent(150, EventEnum.OFF, "bla");
        DBConnector.getInstance(this).storeWifiEvent(160, EventEnum.ON, "bla");
        DBConnector.getInstance(this).storeWifiEvent(170, EventEnum.OFF, "bla");
        
        DBConnector.getInstance(this).storeWifiEvent(170, EventEnum.OFF, null);
        DBConnector.getInstance(this).storeWifiEvent(170, EventEnum.OFF, null);
        
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setSpeedRequired(false);
        
        String bestProvider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(bestProvider, 2000, 1, new LocationListener() {
            
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                
            }
            
            
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                
            }
            
            
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                
            }
            
            
            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                
            }
        });
        
        System.out.println("Best Provider " + bestProvider);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        
        if (location != null) {
            Toast.makeText(this,
                    "Long: " + String.valueOf(location.getLongitude() + " Lat: " + location.getLatitude()),
                    Toast.LENGTH_LONG).show();
            System.out.println("Long: " + String.valueOf(location.getLongitude() + " Lat: " + location.getLatitude()));
        } else {
            Toast.makeText(this, "Provider null", Toast.LENGTH_LONG).show();
        }
        
    }
    
    
    public void showToast() {
        Toast.makeText(this, String.valueOf(signal), Toast.LENGTH_SHORT).show();
    }
    
}
