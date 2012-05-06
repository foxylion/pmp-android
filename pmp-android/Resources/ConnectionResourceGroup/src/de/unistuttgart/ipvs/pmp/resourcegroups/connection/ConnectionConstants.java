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

/**
 * Constants of the Connections RG
 * 
 * @author Thorsten Berberich
 * 
 */
public class ConnectionConstants {
    
    /**
     * The package name of the resource group
     */
    public static final String RG_PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.connection";
    
    /**
     * The resource
     */
    public static final String RES_ENERGY = "connectionResource";
    
    /**
     * The privacy settings
     */
    public static final String PS_WIFI_STATUS = "wifi-status-connection-status";
    public static final String PS_CONFIGURED_NETWORKS = "wifi-configured-networks";
    public static final String PS_WIFI_CONNECTED_CITIES = "wifi-connected-cities";
    public static final String PS_BLUETOOTH_STATUS = "bluetooth-status-connection-status";
    public static final String PS_BLUETOOTH_DEVICES = "bluetooth-paired-devices";
    public static final String PS_BT_CONNECTED_CITIES = "bluetooth-connected-cities";
    public static final String PS_DATA_STATUS = "data-connection-status";
    public static final String PS_CELL_STATUS = "cell-phone-network-status";
    public static final String PS_UPLOAD_DATA = "upload-data";
    
    /**
     * The log tag
     */
    public static final String LOG_TAG = "ConnectionResourceGroup";
    
    /**
     * Time durations
     */
    public static final long ONE_DAY = 86400000L;
    public static final long ONE_MONTH = 2592000000L;
}
