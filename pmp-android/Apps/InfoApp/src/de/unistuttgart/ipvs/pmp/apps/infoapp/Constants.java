/**
 * 
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
