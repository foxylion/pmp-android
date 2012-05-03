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
	protected static final int DATABASE_VERSION = 2;
	
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

}
