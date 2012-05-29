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

import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver.BluetoothReceiver;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver.CellPhoneReceiver;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver.WifiConnectionReceiver;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver.WifiStateReceiver;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.listener.SignalStrengthListener;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.resource.ConnectionResource;

/**
 * The {@link ResourceGroup}
 * 
 * @author Thorsten Berberich
 * 
 */
public class Connection extends ResourceGroup {
    
    /**
     * Constructor
     * 
     * @param pmpci
     *            {@link IPMPConnectionInterface}
     */
    @SuppressWarnings("deprecation")
    public Connection(IPMPConnectionInterface pmpci) {
        super(ConnectionConstants.RG_PACKAGE_NAME, pmpci);
        
        // Register the signal strength listener
        if (!SignalStrengthListener.getInstance().isRegistered()) {
            SignalStrengthListener.getInstance().register(pmpci.getContext(""));
        }
        
        // Register the resource
        registerResource(ConnectionConstants.RES_CONNECTION, new ConnectionResource());
        
        // Register all privacy settings
        registerPrivacySetting(ConnectionConstants.PS_BLUETOOTH_DEVICES, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_BLUETOOTH_STATUS, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_BT_CONNECTED_CITIES, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_CELL_STATUS, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_CELL_STATUS, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_CONFIGURED_NETWORKS, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_DATA_STATUS, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_UPLOAD_DATA, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_WIFI_CONNECTED_CITIES, new BooleanPrivacySetting());
        registerPrivacySetting(ConnectionConstants.PS_WIFI_STATUS, new BooleanPrivacySetting());
        
        // Instantiate the broadcast receiver
        BluetoothReceiver bt = new BluetoothReceiver();
        CellPhoneReceiver cp = new CellPhoneReceiver();
        WifiConnectionReceiver wifiCon = new WifiConnectionReceiver();
        WifiStateReceiver wifiState = new WifiStateReceiver();
        
        // Add the intent filters
        IntentFilter btFilter = new IntentFilter();
        btFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter cpFilter = new IntentFilter();
        cpFilter.addAction(ConnectionConstants.CELLULAR_INTENT);
        IntentFilter wifiConFilter = new IntentFilter();
        wifiConFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        IntentFilter wifiStateFilter = new IntentFilter();
        wifiStateFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        
        // Register the receivers
        registerReceiver(bt, btFilter);
        registerReceiver(cp, cpFilter);
        registerReceiver(wifiCon, wifiConFilter);
        registerReceiver(wifiState, wifiStateFilter);
    }
    
}
