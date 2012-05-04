package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class DBConstants {
    
    /**
     * Database name and version
     */
    protected static final String DATABASE_NAME = "energy.db";
    protected static final int DATABASE_VERSION = 3;
    
    /**
     * Table names
     */
    public static final String TABLE_BATTERY = "battery";
    public static final String TABLE_SCREEN = "screen";
    public static final String TABLE_DEVICE_BOOT = "device_boot";
    
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
    public static final String TABLE_SCREEN_COL_ID = "id";
    public static final String TABLE_SCREEN_COL_TIMESTAMP = "timestamp";
    public static final String TABLE_SCREEN_COL_CHANGED_TO = "changed_to";
    public static final String TABLE_DEVICE_BOOT_COL_ID = "id";
    public static final String TABLE_DEVICE_BOOT_COL_TIMESTAMP = "timestamp";
    
    /**
     * Column arrays
     */
    public static final String[] TABLE_BATTERY_ALL_COLS = { TABLE_BATTERY_COL_ID, TABLE_BATTERY_COL_TIMESTAMP,
            TABLE_BATTERY_COL_LEVEL, TABLE_BATTERY_COL_HEALTH, TABLE_BATTERY_COL_STATUS, TABLE_BATTERY_COL_PLUGGED,
            TABLE_BATTERY_COL_PRESENT, TABLE_BATTERY_COL_TECHNOLOGY, TABLE_BATTERY_COL_TEMPERATURE,
            TABLE_BATTERY_COL_VOLTAGE };
    public static final String[] TABLE_SCREEN_ALL_COLS = { TABLE_SCREEN_COL_ID, TABLE_SCREEN_COL_TIMESTAMP,
            TABLE_SCREEN_COL_CHANGED_TO };
    public static final String[] TABLE_DEVICE_BOOT_ALL_COLS = { TABLE_DEVICE_BOOT_COL_ID,
            TABLE_DEVICE_BOOT_COL_TIMESTAMP };
    
}
