package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.content.Intent;
import android.os.BatteryManager;

/**
 * 
 * @author Marcus Vetter
 *
 */
public class EnergyConstants {
	
	/**
	 * The package name of the resource group
	 */
	public static final String RG_PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.energy";

	/**
	 * The resource
	 */
	public static final String RES_ENERGY = "energyResource";
	
	/**
	 * The app identifier
	 */
	public static String APP_IDENTIFIER = "";

	/**
	 * The privacy settings
	 */
	public static final String PS_BATTERY_STATUS = "battery-status";
	public static final String PS_BATTERY_HEALTH = "battery-health";
	public static final String PS_BATTERY_CHARGING_STATUS = "battery-charging-status";
	public static final String PS_BATTERY_CHARGING_SOURCE = "battery-charging-source";
	public static final String PS_BATTERY_CHARGING_TIME = "battery-charging-time";
	public static final String PS_BATTERY_TEMPERATURE = "battery-temperature";
	public static final String PS_BATTERY_CHARGING_RATIO = "battery-charging-ratio";
	public static final String PS_BATTERY_CHARGING_COUNT = "battery-charging-count";
	public static final String PS_DEVICE_BATTERY_CHARGING_UPTIME = "device-battery-charging-uptime";
	public static final String PS_DEVICE_DATES = "device-dates";
	public static final String PS_DEVICE_SCREEN = "device-screen";
	public static final String PS_UPLOAD_DATA = "upload-data";
	
	/**
	 * The log tag
	 */
	public static final String LOG_TAG = "EnergyResourceGroup";
	
	/**
	 * Intents of interest
	 */
	public static final String ACTION_SCREEN_ON = Intent.ACTION_SCREEN_ON;
	public static final String ACTION_SCREEN_OFF = Intent.ACTION_SCREEN_OFF;
	public static final String ACTION_BATTERY_CHANGED = Intent.ACTION_BATTERY_CHANGED;

	/**
	 * Extras of interest
	 */
	public static final String EXTRA_LEVEL = BatteryManager.EXTRA_LEVEL;
	public static final String EXTRA_HEALTH = BatteryManager.EXTRA_HEALTH;
	public static final String EXTRA_STATUS = BatteryManager.EXTRA_STATUS;
	public static final String EXTRA_PLUGGED = BatteryManager.EXTRA_PLUGGED;
	public static final String EXTRA_PRESENT = BatteryManager.EXTRA_PRESENT;
	public static final String EXTRA_TECHNOLOGY = BatteryManager.EXTRA_TECHNOLOGY;
	public static final String EXTRA_TEMPERATURE = BatteryManager.EXTRA_TEMPERATURE;
	public static final String EXTRA_VOLTAGE = BatteryManager.EXTRA_VOLTAGE;
	
	/**
	 * Health constants
	 */
	public static final String HEALTH_DEAD = "DEAD";
	public static final String HEALTH_GOOD = "GOOD";
	public static final String HEALTH_OVERHEAT = "OVERHEAT";
	public static final String HEALTH_OVER_VOLTAGE = "OVER_VOLTAGE";
	public static final String HEALTH_UNKNOWN = "UNKOWN";
	public static final String HEALTH_UNSPECIFIED_FAILURE = "UNSPECIFIED_FAILURE";
	
	/**
	 * Plugged constants
	 */
	public static final String PLUGGED_USB = "USB";
	public static final String PLUGGED_AC = "AC";
	public static final String PLUGGED_NOT_PLUGGED = "NOT_PLUGGED";
	
	/**
	 * Status constants
	 */
	public static final String STATUS_CHARGING = "CHARGING";
	public static final String STATUS_DISCHARGING = "DISCHARGING";
	public static final String STATUS_FULL = "FULL";
	public static final String STATUS_NOT_CHARGING = "NOT_CHARGING";
	public static final String STATUS_UNKNOWN = "UNKOWN";
}
