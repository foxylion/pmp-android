package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

/**
 * This are the constants of the database (tables, rows, etc.)
 * 
 * @author Marcus Vetter
 * 
 */
public class DBConstants {
    
    /**
     * Database name and version
     */
    protected static final String DATABASE_NAME = "energydb";
    protected static final int DATABASE_VERSION = 10;
    
    /**
     * Table names
     */
    public static final String TABLE_BATTERY = "battery";
    public static final String TABLE_DEVICE_DATA = "device_data";
    
    /**
     * Column names
     */
    public static final String TABLE_BATTERY_COL_ID = "id";
    public static final String TABLE_BATTERY_COL_TIMESTAMP = "timestamp";
    public static final String TABLE_BATTERY_COL_LEVEL = "level";
    public static final String TABLE_BATTERY_COL_HEALTH = "health";
    public static final String TABLE_BATTERY_COL_STATUS = "status";
    public static final String TABLE_BATTERY_COL_PLUGGED = "plugged";
    public static final String TABLE_BATTERY_COL_PRESENT = "present";
    public static final String TABLE_BATTERY_COL_TECHNOLOGY = "technology";
    public static final String TABLE_BATTERY_COL_TEMPERATURE = "temperature";
    public static final String TABLE_BATTERY_COL_VOLTAGE = "voltage";
    
    public static final String TABLE_DEVICE_DATA_COL_KEY = "key";
    public static final String TABLE_DEVICE_DATA_COL_VALUE = "value";
    
    /**
     * Keys of the device data table
     */
    public static final String TABLE_DEVICE_DATA_KEY_DEVICE_ON_FLAG = "device_on_flag";
    public static final String TABLE_DEVICE_DATA_KEY_LAST_BOOT_DATE = "last_boot_date";
    public static final String TABLE_DEVICE_DATA_KEY_FIRST_MEASUREMENT_DATE = "first_measurement_date";
    public static final String TABLE_DEVICE_DATA_KEY_TOTAL_UPTIME = "total_uptime";
    public static final String TABLE_DEVICE_DATA_KEY_LAST_SCREEN_ON_DATE = "last_screen_on_date";
    public static final String TABLE_DEVICE_DATA_KEY_SCREEN_ON_TIME = "screen_on_time";
    public static final String TABLE_DEVICE_DATA_KEY_LAST_BOOT_SCREEN_ON_TIME = "last_boot_screen_on_time";
    
    /**
     * Column arrays
     */
    public static final String[] TABLE_BATTERY_ALL_COLS = { TABLE_BATTERY_COL_ID, TABLE_BATTERY_COL_TIMESTAMP,
            TABLE_BATTERY_COL_LEVEL, TABLE_BATTERY_COL_HEALTH, TABLE_BATTERY_COL_STATUS, TABLE_BATTERY_COL_PLUGGED,
            TABLE_BATTERY_COL_PRESENT, TABLE_BATTERY_COL_TECHNOLOGY, TABLE_BATTERY_COL_TEMPERATURE,
            TABLE_BATTERY_COL_VOLTAGE };
    public static final String[] TABLE_DEVICE_DATA_ALL_COLS = { TABLE_DEVICE_DATA_COL_KEY, TABLE_DEVICE_DATA_COL_VALUE };
}
