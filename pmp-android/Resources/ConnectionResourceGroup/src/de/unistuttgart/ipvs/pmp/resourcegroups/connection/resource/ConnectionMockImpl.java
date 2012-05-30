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

import android.content.Context;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.ConnectionConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection;

/**
 * Cloaked imlementation of the IConnection interface
 * 
 * @author Thorsten Berberich
 * 
 */
public class ConnectionMockImpl extends IConnection.Stub {
    
    /**
     * {@link PermissionValidator}
     */
    private PermissionValidator validator;
    
    
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
    public ConnectionMockImpl(ResourceGroup rg, String appIdentifier) {
        this.validator = new PermissionValidator(rg, appIdentifier);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionStatus()
     */
    @Override
    public boolean getWifiConnectionStatus() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_WIFI_STATUS, "true");
        
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionLastTwentyFourHours()
     */
    @Override
    public long getWifiConnectionLastTwentyFourHours() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_WIFI_STATUS, "true");
        
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionLastMonth()
     */
    @Override
    public long getWifiConnectionLastMonth() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_WIFI_STATUS, "true");
        
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConfigureddWifiNetworks()
     */
    @Override
    public List<String> getConfigureddWifiNetworks() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_CONFIGURED_NETWORKS, "true");
        
        return new ArrayList<String>();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConnectedWifiCities()
     */
    @Override
    public List<String> getConnectedWifiCities() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_WIFI_CONNECTED_CITIES, "true");
        
        return new ArrayList<String>();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBluetoothStatus()
     */
    @Override
    public boolean getBluetoothStatus() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_BLUETOOTH_STATUS, "true");
        
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getPairedBluetoothDevices()
     */
    @Override
    public List<String> getPairedBluetoothDevices() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_BLUETOOTH_DEVICES, "true");
        
        return new ArrayList<String>();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBTConnectionLastTwentyFourHours()
     */
    @Override
    public long getBTConnectionLastTwentyFourHours() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_BLUETOOTH_STATUS, "true");
        
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBTConnectionLastMonth()
     */
    @Override
    public long getBTConnectionLastMonth() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_BLUETOOTH_STATUS, "true");
        
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConnectedBTCities()
     */
    @Override
    public List<String> getConnectedBTCities() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_BT_CONNECTED_CITIES, "true");
        
        return new ArrayList<String>();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getDataConnectionStatus()
     */
    @Override
    public boolean getDataConnectionStatus() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_DATA_STATUS, "true");
        
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getProvider()
     */
    @Override
    public String getProvider() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        return "Cloak Provider";
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getRoamingStatus()
     */
    @Override
    public boolean getRoamingStatus() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getAirplaneModeLastTwentyFourHours()
     */
    @Override
    public long getAirplaneModeLastTwentyFourHours() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getAirplaneModeLastMonth()
     */
    @Override
    public long getAirplaneModeLastMonth() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_CELL_STATUS, "true");
        
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#uploadData()
     */
    @Override
    public String uploadData() throws RemoteException {
        // Check the privacy setting
        this.validator.validate(ConnectionConstants.PS_UPLOAD_DATA, "true");
        
        return "Error";
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getNetworkType()
     */
    @Override
    public String getNetworkType() throws RemoteException {
        return "unknown";
    }
}
