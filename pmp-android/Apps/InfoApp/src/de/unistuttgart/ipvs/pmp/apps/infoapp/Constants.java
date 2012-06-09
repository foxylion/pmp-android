/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp
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
package de.unistuttgart.ipvs.pmp.apps.infoapp;

import java.text.SimpleDateFormat;

/**
 * Class containing the constants of the infoapp
 * 
 * @author Thorsten Berberich, Marcus Vetter
 * 
 */
public class Constants {
    
    /**
     * Service Features
     */
    public static final String CONNECTION_WIFI_INFO = "wifi-info";
    public static final String CONNECTION_BT_INFO = "bluetooth-info";
    public static final String CONNECTION_DATA_INFO = "data-connection-info";
    public static final String CONNECTION_CELL_INFO = "cell-phone-info";
    public static final String CONNECTION_STATISTICS = "connections-statistics";
    public static final String ENERGY_SF_CURRENT_VALUES = "energy-current";
    public static final String ENERGY_SF_LAST_BOOT_VALUES = "energy-since-last-boot";
    public static final String ENERGY_SF_TOTAL_VALUES = "energy-total";
    public static final String ENERGY_SF_UPLOAD_DATA = "energy-statistics";
    
    /**
     * Connection ResourceGroup
     */
    public static final String CONNECTION_RG_IDENTIFIER = "de.unistuttgart.ipvs.pmp.resourcegroups.connection";
    public static final String CONNECTION_RG_RESOURCE = "connectionResource";
    
    /**
     * Energy ResourceGroup
     */
    public static final String ENERGY_RG_IDENTIFIER = "de.unistuttgart.ipvs.pmp.resourcegroups.energy";
    public static final String ENERGY_RG_RESOURCE = "energyResource";
    
    /**
     * The simple date format
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
}
