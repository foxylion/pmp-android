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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.Events;

/**
 * Stores events when the state of the wifi is changed
 * 
 * @author Thorsten Berberich
 * 
 */
public class WifiStateReceiver extends BroadcastReceiver {
    
    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Timestampe of the event
        long time = new Date().getTime();
        
        // Get the wifi state or unknown if an error happens        
        int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        
        // Store the events according to the state
        switch (extraWifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                
                // Store the connection as off because the wifi is off
                DBConnector.getInstance(context).storeWifiEvent(time, Events.OFF, null, Events.OFF);
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                
                // Store also as off, because the other receiver will be called too and store that the wifi is on
                DBConnector.getInstance(context).storeWifiEvent(time, Events.OFF, null, Events.ON);
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                break;
        }
    }
}
