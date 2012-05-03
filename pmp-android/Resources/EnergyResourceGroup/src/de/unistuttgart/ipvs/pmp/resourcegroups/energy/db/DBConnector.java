package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.DeviceBootEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.ScreenEvent;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class DBConnector implements IDBConnector {

	/**
	 * Context
	 */
	private Context context;

	/**
	 * The sqlite database
	 */
	private SQLiteDatabase database;

	/**
	 * The sqlite database helper
	 */
	private SQLiteHelper dbHelper;

	/**
	 * Instance (Singleton pattern)
	 */
	private static DBConnector instance = null;

	/**
	 * Private constructor (Singleton pattern)
	 */
	private DBConnector(Context context) {
		this.context = context;
		initialize();
	}

	/**
	 * Get the instance (Singleton pattern)
	 * 
	 * @return the instance
	 */
	public static IDBConnector getInstance(Context context) {
		if (instance == null) {
			instance = new DBConnector(context);
		}
		return instance;
	}

	/**
	 * Initialize the sql tables, create them if necessary
	 */
	private void initialize() {
		dbHelper = new SQLiteHelper(this.context);
	}

	/**
	 * Open the database
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Close the database
	 */
	public void close() {
		dbHelper.close();
	}

	public void storeBatteryEvent(BatteryEvent be) {

		// Create content values
		ContentValues cvs = new ContentValues();
		cvs.put(DBConstants.TABLE_BATTERY_COL_TIMESTAMP, be.getTimestamp());
		cvs.put(DBConstants.TABLE_BATTERY_COL_LEVEL, be.getLevel());
		cvs.put(DBConstants.TABLE_BATTERY_COL_HEALTH, be.getHealth());
		cvs.put(DBConstants.TABLE_BATTERY_COL_STATUS, be.getStatus());
		cvs.put(DBConstants.TABLE_BATTERY_COL_PLUGGED, be.getPlugged());
		cvs.put(DBConstants.TABLE_BATTERY_COL_PRESENT, be.isPresent());
		cvs.put(DBConstants.TABLE_BATTERY_COL_TECHNOLOGY, be.getTechnology());
		cvs.put(DBConstants.TABLE_BATTERY_COL_TEMPERATURE, be.getTemperature());
		cvs.put(DBConstants.TABLE_BATTERY_COL_VOLTAGE, be.getVoltage());

		// Insert into database
		open();
		long id = database.insert(DBConstants.TABLE_BATTERY, null, cvs);
		close();

		Toast.makeText(this.context, "BATTERY ID: " + id, Toast.LENGTH_SHORT)
				.show();

		// Log
		Log.i(EnergyConstants.LOG_TAG, "Stored battery event in database (ID: "
				+ id + ")");
		Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + be.getTimestamp());
		Log.i(EnergyConstants.LOG_TAG, "Level: " + be.getLevel());
		Log.i(EnergyConstants.LOG_TAG, "Health: " + be.getHealth());
		Log.i(EnergyConstants.LOG_TAG, "Status: " + be.getStatus());
		Log.i(EnergyConstants.LOG_TAG, "Plugged: " + be.getPlugged());
		Log.i(EnergyConstants.LOG_TAG, "Present: " + be.isPresent());
		Log.i(EnergyConstants.LOG_TAG, "Technology: " + be.getTechnology());
		Log.i(EnergyConstants.LOG_TAG, "Temperature: " + be.getTemperature());
		Log.i(EnergyConstants.LOG_TAG, "Voltage: " + be.getVoltage());
	}

	public void storeScreenEvent(ScreenEvent se) {

		// Create content values
		ContentValues cvs = new ContentValues();
		cvs.put(DBConstants.TABLE_SCREEN_COL_TIMESTAMP, se.getTimestamp());
		cvs.put(DBConstants.TABLE_SCREEN_COL_CHANGED_TO, se.isChangedTo());

		// Insert into database
		open();
		long id = database.insert(DBConstants.TABLE_SCREEN, null, cvs);
		close();

		Toast.makeText(this.context, "SCREEN ID: " + id, Toast.LENGTH_SHORT)
				.show();

		// Log
		Log.i(EnergyConstants.LOG_TAG, "Stored screen event in database (ID: "
				+ id + ")");
		Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + se.getTimestamp());
		Log.i(EnergyConstants.LOG_TAG, "Changed to: " + se.isChangedTo());
	}

	public void storeDeviceBootEvent(DeviceBootEvent dbe) {

		// Create content values
		ContentValues cvs = new ContentValues();
		cvs.put(DBConstants.TABLE_DEVICE_BOOT_COL_TIMESTAMP, dbe.getTimestamp());

		// Insert into database
		open();
		long id = database.insert(DBConstants.TABLE_DEVICE_BOOT, null, cvs);
		close();

		Toast.makeText(this.context, "DEVICE_BOOT ID: " + id,
				Toast.LENGTH_SHORT).show();

		// Log
		Log.i(EnergyConstants.LOG_TAG,
				"Stored device boot event in database (ID: " + id + ")");
		Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + dbe.getTimestamp());
	}

}
