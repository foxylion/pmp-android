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

import java.util.List;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection;

/**
 * Cloaked imlementation of the IConnection interface
 * 
 * @author Thorsten Berberich
 * 
 */
public class ConnectionCloakImpl extends IConnection.Stub {
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionStatus()
     */
    @Override
    public boolean getWifiConnectionStatus() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionLastTwentyFourHours()
     */
    @Override
    public long getWifiConnectionLastTwentyFourHours() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getWifiConnectionLastMonth()
     */
    @Override
    public long getWifiConnectionLastMonth() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConfigureddWifiNetworks()
     */
    @Override
    public List<String> getConfigureddWifiNetworks() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConnectedWifiCities()
     */
    @Override
    public List<String> getConnectedWifiCities() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBluetoothStatus()
     */
    @Override
    public boolean getBluetoothStatus() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getPairedBluetoothDevices()
     */
    @Override
    public List<String> getPairedBluetoothDevices() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBTConnectionLastTwentyFourHours()
     */
    @Override
    public long getBTConnectionLastTwentyFourHours() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getBTConnectionLastMonth()
     */
    @Override
    public long getBTConnectionLastMonth() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getConnectedBTCities()
     */
    @Override
    public List<String> getConnectedBTCities() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getDataConnectionStatus()
     */
    @Override
    public boolean getDataConnectionStatus() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getProvider()
     */
    @Override
    public String getProvider() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getCellPhoneSignalStrength()
     */
    @Override
    public int getCellPhoneSignalStrength() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getRoamingStatus()
     */
    @Override
    public boolean getRoamingStatus() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getAirplaneModeLastTwentyFourHours()
     */
    @Override
    public long getAirplaneModeLastTwentyFourHours() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#getAirplaneModeLastMonth()
     */
    @Override
    public long getAirplaneModeLastMonth() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection#uploadData()
     */
    @Override
    public boolean uploadData() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
}
