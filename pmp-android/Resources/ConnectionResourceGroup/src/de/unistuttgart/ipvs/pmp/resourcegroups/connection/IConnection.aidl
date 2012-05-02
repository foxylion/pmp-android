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

import java.util.List;

/**
 * AIDL File for the connection rg
 * 
 * @author Thorsten Berberich
 * 
 */
interface IConnection {
	
	/**
	 * Get the connection status of the wifi
	 * 
	 * @return True iff connected, false otherwise
	 */
	boolean getWifiConnectionStatus();
	
	
	/**
	 * Get the time how long the device was connected to wifi networks the last 24 hours
	 * 
	 * @return time in milliseconds
	 */
	long getWifiConnectionLastTwentyFourHours();
	
	
	/**
	 * Get the time how long the device was connected to wifi networks the last month
	 * 
	 * @return time in milliseconds
	 */
	long getWifiConnectionLastMonth();
	
	
	/**
	 * Get all configured wifi networks
	 * 
	 * @return List with all configured wifi networks
	 */
	List<String> getConfigureddWifiNetworks();
	
	
	/**
	 * Get all cities where the user was connected to a wifi network
	 * 
	 * @return List with all cities where the user was connected
	 */
	List<String> getConnectedWifiCities();
	
	
	/**
	 * Get the connection status of the bluetooth
	 * 
	 * @return True iff connected, false otherwise
	 */
	boolean getBluetoothStatus();
	
	
	/**
	 * Get all paired bluetooth devices
	 * 
	 * @return List with all paired bluetooth devices
	 */
	List<String> getPairedBluetoothDevices();
	
	
	/**
	 * Get the time how long the device was connected to bluetooth devices the last 24 hours
	 * 
	 * @return time in milliseconds
	 */
	long getBTConnectionLastTwentyFourHours();
	
	
	/**
	 * Get the time how long the device was connected to bluetooth devices the last month
	 * 
	 * @return time in milliseconds
	 */
	long getBTConnectionLastMonth();
	
	
	/**
	 * Get all cities where the user was connected to a bluetooth device
	 * 
	 * @return List with all cities where the user was connected
	 */
	List<String> getConnectedBTCities();
	
	
	/**
	 * Get the status of the data connection
	 * 
	 * @return true iff connected, false otherwise
	 */
	boolean getDataConnectionStatus();
	
	
	/**
	 * Get the cell phone provider
	 * 
	 * @return String with provider name
	 */
	String getProvider();
	
	
	/**
	 * Get the signal strength of the cell phone network
	 * 
	 * @return signal strength measured in dBm <b>or 99 </b> if not known or not detectable
	 *         </br>calculation formula: dBm = 2*ASU - 113
	 */
	int getCellPhoneSignalStrength();
	
	
	/**
	 * Get the roaming status
	 * 
	 * @return true iff roaming is active, false otherwise
	 */
	boolean getRoamingStatus();
	
	
	/**
	 * Get the time how long the device was airplane mode the last 24 hours
	 * 
	 * @return time in milliseconds
	 */
	long getAirplaneModeLastTwentyFourHours();
	
	
	/**
	 * Get the time how long the device was in airplane mode the last month
	 * 
	 * @return time in milliseconds
	 */
	long getAirplaneModeLastMonth();
}
