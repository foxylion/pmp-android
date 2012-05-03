package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	/**
	 * Statement to create the battery table
	 */
	private static final String CREATE_TABLE_BATTERY = "CREATE TABLE IF NOT EXISTS "
			+ DBConstants.TABLE_BATTERY
			+ " ( "
			+ DBConstants.TABLE_BATTERY_COL_ID
			+ " integer primary key autoincrement, "
			+ DBConstants.TABLE_BATTERY_COL_TIMESTAMP
			+ " integer, "
			+ DBConstants.TABLE_BATTERY_COL_LEVEL
			+ " integer, "
			+ DBConstants.TABLE_BATTERY_COL_HEALTH
			+ " text, "
			+ DBConstants.TABLE_BATTERY_COL_STATUS
			+ " text, "
			+ DBConstants.TABLE_BATTERY_COL_PLUGGED
			+ " text, "
			+ DBConstants.TABLE_BATTERY_COL_PRESENT
			+ " integer, "
			+ DBConstants.TABLE_BATTERY_COL_TECHNOLOGY
			+ " text, "
			+ DBConstants.TABLE_BATTERY_COL_TEMPERATURE
			+ " real, "
			+ DBConstants.TABLE_BATTERY_COL_VOLTAGE + " integer );";

	/**
	 * Statement to create the screen table
	 */
	private static final String CREATE_TABLE_SCREEN = "CREATE TABLE IF NOT EXISTS "
			+ DBConstants.TABLE_SCREEN
			+ " ( "
			+ DBConstants.TABLE_SCREEN_COL_ID
			+ " integer primary key autoincrement, "
			+ DBConstants.TABLE_SCREEN_COL_TIMESTAMP
			+ " integer, "
			+ DBConstants.TABLE_SCREEN_COL_CHANGED_TO + " integer );";

	/**
	 * Statement to create the device boot table
	 */
	private static final String CREATE_TABLE_DEVICE_BOOT = "CREATE TABLE IF NOT EXISTS "
			+ DBConstants.TABLE_DEVICE_BOOT
			+ " ( "
			+ DBConstants.TABLE_DEVICE_BOOT_COL_ID
			+ " integer primary key autoincrement, "
			+ DBConstants.TABLE_DEVICE_BOOT_COL_TIMESTAMP + "integer );";

	public SQLiteHelper(Context context) {
		super(context, DBConstants.DATABASE_NAME, null,
				DBConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_BATTERY);
		db.execSQL(CREATE_TABLE_SCREEN);
		db.execSQL(CREATE_TABLE_DEVICE_BOOT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(EnergyConstants.LOG_TAG, "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_BATTERY);
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_SCREEN);
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_DEVICE_BOOT);
		onCreate(db);
	}

}
