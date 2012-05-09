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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.ConnectionConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.DBConstants;

/**
 * Implements the IConnection aidl file
 * 
 * @author Thorsten Berberich
 * 
 */
public class ConnectionImpl extends IConnection.Stub {
    
    /**
     * Context of the RG
     */
    private Context context;
    
    /**
     * {@link PermissionValidator}
     */
    private PermissionValidator validator;
    
    /**
     * GSM signal strength in asu
     */
    private int signal;
    
    
    /**
     * Constructor to get a context
     * 
     * @param context
     *            {@link Context} of the rg
     * @param rg
     *            {@link ResourceGroup}
     * @param appIdentifier
     *            identifier of the app that wants to do sth.
     * 
     */
    public ConnectionImpl(Context context, ResourceGroup rg, String appIdentifier) {
        this.context = context;
        this.validator = new PermissionValidator(rg, appIdentifier);
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            manager.listen(new SignalPhoneStateListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
        signal = -1;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionStatus()
     */
    @Override
    public boolean getWifiConnectionStatus() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_WIFI_STATUS, "true");
        
        boolean result = false;
        
        ConnectivityManager connManager = (ConnectivityManager) this.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi == null) {
                return false;
            } else {
                result = mWifi.isConnected();
            }
        }
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionLastTwentyFourHours()
     */
    @Override
    public long getWifiConnectionLastTwentyFourHours() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_WIFI_STATUS, "true");
        
        DBConnector.getInstance(this.context).open();
        long result = DBConnector.getInstance(this.context).getTimeDuration(DBConstants.TABLE_WIFI,
                ConnectionConstants.ONE_DAY, 0);
        DBConnector.getInstance(this.context).close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionLastMonth()
     */
    @Override
    public long getWifiConnectionLastMonth() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_WIFI_STATUS, "true");
        
        DBConnector.getInstance(this.context).open();
        long result = DBConnector.getInstance(this.context).getTimeDuration(DBConstants.TABLE_WIFI,
                ConnectionConstants.ONE_MONTH, 0);
        DBConnector.getInstance(this.context).close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConfigureddWifiNetworks()
     */
    @Override
    public List<String> getConfigureddWifiNetworks() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_CONFIGURED_NETWORKS, "true");
        
        List<String> result = new ArrayList<String>();
        
        // Get the wifi manager
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        
        if (wifi != null) {
            List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
            
            // Iterate over all wifi configurations to get the SSIDs
            for (WifiConfiguration config : configs) {
                result.add(config.SSID);
            }
        }
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConnectedWifiCities()
     */
    @Override
    public List<String> getConnectedWifiCities() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_WIFI_CONNECTED_CITIES, "true");
        
        DBConnector.getInstance(this.context).open();
        List<String> result = DBConnector.getInstance(this.context).getConnectedCities(DBConstants.TABLE_WIFI);
        DBConnector.getInstance(this.context).close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBluetoothStatus()
     */
    @Override
    public boolean getBluetoothStatus() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_BLUETOOTH_STATUS, "true");
        
        Boolean result = false;
        
        // Check if the BluetoothAdapter is supported
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            result = BluetoothAdapter.getDefaultAdapter().isEnabled();
        }
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getPairedBluetoothDevices()
     */
    @Override
    public List<String> getPairedBluetoothDevices() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_BLUETOOTH_DEVICES, "true");
        
        List<String> result = new ArrayList<String>();
        
        // Check if the BluetoothAdapter is supported
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            Set<BluetoothDevice> devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            if (devices != null) {
                for (BluetoothDevice device : devices) {
                    result.add(device.getName());
                }
            }
        }
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBTConnectionLastTwentyFourHours()
     */
    @Override
    public long getBTConnectionLastTwentyFourHours() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_BLUETOOTH_STATUS, "true");
        
        DBConnector.getInstance(this.context).open();
        long result = DBConnector.getInstance(this.context).getTimeDuration(DBConstants.TABLE_BT,
                ConnectionConstants.ONE_DAY, 0);
        DBConnector.getInstance(this.context).close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBTConnectionLastMonth()
     */
    @Override
    public long getBTConnectionLastMonth() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_BLUETOOTH_STATUS, "true");
        
        DBConnector.getInstance(this.context).open();
        long result = DBConnector.getInstance(this.context).getTimeDuration(DBConstants.TABLE_BT,
                ConnectionConstants.ONE_MONTH, 0);
        DBConnector.getInstance(this.context).close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConnectedBTCities()
     */
    @Override
    public List<String> getConnectedBTCities() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_BT_CONNECTED_CITIES, "true");
        
        DBConnector.getInstance(this.context).open();
        List<String> result = DBConnector.getInstance(this.context).getConnectedCities(DBConstants.TABLE_BT);
        DBConnector.getInstance(this.context).close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getDataConnectionStatus()
     */
    @Override
    public boolean getDataConnectionStatus() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_DATA_STATUS, "true");
        
        // Get the telephony manager
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            //Get the data state
            int state = manager.getDataState();
            switch (state) {
                case TelephonyManager.DATA_DISCONNECTED:
                    return false;
                case TelephonyManager.DATA_CONNECTED:
                    return true;
                case TelephonyManager.DATA_CONNECTING:
                    return true;
            }
        }
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getProvider()
     */
    @Override
    public String getProvider() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        // Get the telephony manager
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            return manager.getNetworkOperatorName();
        }
        return "-";
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getCellPhoneSignalStrength()
     */
    @Override
    public int getCellPhoneSignalStrength() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        if (signal == -1 || signal == 99) {
            return 99;
        } else {
            return (2 * signal) - 113;
        }
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getRoamingStatus()
     */
    @Override
    public boolean getRoamingStatus() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        boolean result = false;
        
        // Get the telephony manager
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            result = manager.isNetworkRoaming();
        }
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getAirplaneModeLastTwentyFourHours()
     */
    @Override
    public long getAirplaneModeLastTwentyFourHours() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        DBConnector.getInstance(this.context).open();
        long result = DBConnector.getInstance(this.context).getTimeDuration(DBConstants.TABLE_CELL,
                ConnectionConstants.ONE_DAY, 0);
        DBConnector.getInstance(this.context).close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getAirplaneModeLastMonth()
     */
    @Override
    public long getAirplaneModeLastMonth() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        DBConnector.getInstance(this.context).open();
        long result = DBConnector.getInstance(this.context).getTimeDuration(DBConstants.TABLE_CELL,
                ConnectionConstants.ONE_MONTH, 0);
        DBConnector.getInstance(this.context).close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#uploadData()
     */
    @Override
    public String uploadData() throws RemoteException {
        // Check the privacy setting
        validator.validate(ConnectionConstants.PS_UPLOAD_DATA, "true");
        return "";
    }
    
    /**
     * Callback class to get the GSM signal strength
     * 
     * @author Thorsten Berberich
     * 
     */
    class SignalPhoneStateListener extends PhoneStateListener {
        
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            signal = signalStrength.getGsmSignalStrength();
        }
    }
    
}
