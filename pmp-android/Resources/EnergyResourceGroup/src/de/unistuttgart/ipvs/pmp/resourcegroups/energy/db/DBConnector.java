package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

import android.util.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;

public class DBConnector implements IDBConnector {

	/**
	 * Instance (Singleton pattern)
	 */
	private static DBConnector instance = null;

	/**
	 * Private constructor (Singleton pattern)
	 */
	private DBConnector() {
		initialize();
	}

	/**
	 * Get the instance (Singleton pattern)
	 * 
	 * @return the instance
	 */
	public static IDBConnector getInstance() {
		if (instance == null) {
			instance = new DBConnector();
		}
		return instance;
	}

	/**
	 * Initialize the sql tables, create them if necessary
	 */
	private void initialize() {
	}

	public void storeBatteryState(long timestamp, int level, String health,
			String status, String plugged, boolean present, String technology, int temperature,
			int voltage) {
		Log.i(EnergyConstants.LOG_TAG, "Store battery state in database: ");
		Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + timestamp);
		Log.i(EnergyConstants.LOG_TAG, "Level: " + level);
		Log.i(EnergyConstants.LOG_TAG, "Health: " + health);
		Log.i(EnergyConstants.LOG_TAG, "Status: " + status);
		Log.i(EnergyConstants.LOG_TAG, "Plugged: " + plugged);
		Log.i(EnergyConstants.LOG_TAG, "Present: " + present);
		Log.i(EnergyConstants.LOG_TAG, "Technology: " + technology);
		Log.i(EnergyConstants.LOG_TAG, "Temperature: " + temperature);
		Log.i(EnergyConstants.LOG_TAG, "Voltage: " + voltage);
	}

	public void storeScreenState(long timestamp, boolean changedTo) {
		Log.i(EnergyConstants.LOG_TAG, "Store screen state in database: ");
		Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + timestamp);
		Log.i(EnergyConstants.LOG_TAG, "Changed to: " + changedTo);
	}

	public void storeDeviceBoot(long timestamp) {
		Log.i(EnergyConstants.LOG_TAG, "Store device boot in database: ");
		Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + timestamp);
	}

}
