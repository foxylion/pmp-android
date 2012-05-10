package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.DeviceBootEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.ScreenEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetCurrentValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetLastBootValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetTotalValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.webserver.ResultSetUpload;

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
        this.dbHelper = new SQLiteHelper(this.context);
    }
    
    
    /**
     * Open the database
     * 
     * @throws SQLException
     */
    public void open() throws SQLException {
        this.database = this.dbHelper.getWritableDatabase();
    }
    
    
    /**
     * Close the database
     */
    public void close() {
        this.dbHelper.close();
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
        long id = this.database.insert(DBConstants.TABLE_BATTERY, null, cvs);
        close();
        
        Toast.makeText(this.context, "BATTERY ID: " + id, Toast.LENGTH_SHORT).show();
        
        // Log
        Log.i(EnergyConstants.LOG_TAG, "Stored battery event in database (ID: " + id + ")");
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
        long id = this.database.insert(DBConstants.TABLE_SCREEN, null, cvs);
        close();
        
        Toast.makeText(this.context, "SCREEN ID: " + id, Toast.LENGTH_SHORT).show();
        
        // Log
        Log.i(EnergyConstants.LOG_TAG, "Stored screen event in database (ID: " + id + ")");
        Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + se.getTimestamp());
        Log.i(EnergyConstants.LOG_TAG, "Changed to: " + se.isChangedTo());
    }
    
    
    public void storeDeviceBootEvent(DeviceBootEvent dbe) {
        
        // Create content values
        ContentValues cvs = new ContentValues();
        cvs.put(DBConstants.TABLE_DEVICE_BOOT_COL_TIMESTAMP, dbe.getTimestamp());
        
        // Insert into database
        open();
        long id = this.database.insert(DBConstants.TABLE_DEVICE_BOOT, null, cvs);
        close();
        
        Toast.makeText(this.context, "DEVICE_BOOT ID: " + id, Toast.LENGTH_SHORT).show();
        
        // Log
        Log.i(EnergyConstants.LOG_TAG, "Stored device boot event in database (ID: " + id + ")");
        Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + dbe.getTimestamp());
    }
    
    
    public ResultSetCurrentValues getCurrentValues() {
        ResultSetCurrentValues rs = new ResultSetCurrentValues();
        
        open();
        Cursor cursor = database.query(DBConstants.TABLE_BATTERY, DBConstants.TABLE_BATTERY_ALL_COLS, null, null, null,
                null, null);
        if (cursor.moveToLast()) {
            BatteryEvent lastBE = cursorToBatteryEvent(cursor);
            rs.setLevel(String.valueOf(lastBE.getLevel()) + " %");
            rs.setHealth(lastBE.getHealth());
            rs.setStatus(lastBE.getStatus());
            if (!lastBE.getPlugged().equals(EnergyConstants.PLUGGED_NOT_PLUGGED)) {
                rs.setPlugged(lastBE.getPlugged());
            }
            rs.setTemperature(String.valueOf(lastBE.getTemperature()) + " °C");
            // Calculate the time
            long pluggedTime = System.currentTimeMillis();
            while (!cursor.isBeforeFirst()) {
                pluggedTime = cursor.getLong(1);
                if (cursor.moveToPrevious()) {
                    if (!cursor.getString(4).equals(rs.getStatus())) {
                        break;
                    }
                }
                
            }
            long currentTime = System.currentTimeMillis();
            rs.setStatusTime((currentTime - pluggedTime) + "ms");
        }
        cursor.close();
        close();
        return rs;
    }
    
    
    public ResultSetLastBootValues getLastBootValues() {
        ResultSetLastBootValues rs = new ResultSetLastBootValues();
        
        /**
         * TODO: REMOVE DUMMY VALUES
         */
        rs.setCountOfCharging("-");
        rs.setDate("-");
        rs.setDurationOfCharging("-");
        rs.setRatio("-");
        rs.setScreenOn("-");
        rs.setTemperatureAverage("-");
        rs.setTemperaturePeak("-");
        rs.setUptime("-");
        rs.setUptimeBattery("-");
        
        return rs;
    }
    
    
    public ResultSetTotalValues getTotalValues() {
        ResultSetTotalValues rs = new ResultSetTotalValues();
        
        /**
         * TODO: REMOVE DUMMY VALUES
         */
        rs.setCountOfCharging("-");
        rs.setDate("-");
        rs.setDurationOfCharging("-");
        rs.setRatio("-");
        rs.setScreenOn("-");
        rs.setTemperatureAverage("-");
        rs.setTemperaturePeak("-");
        rs.setUptime("-");
        rs.setUptimeBattery("-");
        
        List<BatteryEvent> beList = getAllBatteryEvents(0);
        
        float temperaturePeak = 0;
        float temperatureSum = 0;
        for (BatteryEvent be : beList) {
            // Calculate the temperature average
            temperatureSum += be.getTemperature();
            
            // Calculate the temperature peak
            if (be.getTemperature() > temperaturePeak) {
                temperaturePeak = be.getTemperature();
            }
        }
        // Set the temperature average
        rs.setTemperatureAverage(String.valueOf(temperatureSum / beList.size()) + "°C");
        
        // Set the temperature peak
        rs.setTemperaturePeak(String.valueOf(temperaturePeak) + "°C");
        
        return rs;
    }
    
    
    public ResultSetUpload getUploadValues() {
        ResultSetUpload rs = new ResultSetUpload();
        rs.setBatteryEvents(getAllBatteryEvents(0));
        return rs;
    }
    
    
    /**
     * Get all battery events since a given time
     * 
     * @param since
     *            time in ms since 1970
     * @return list of battery events
     */
    private List<BatteryEvent> getAllBatteryEvents(long since) {
        List<BatteryEvent> beList = new ArrayList<BatteryEvent>();
        open();
        Cursor cursor = database.query(DBConstants.TABLE_BATTERY, DBConstants.TABLE_BATTERY_ALL_COLS,
                "DBConstants.TABLE_BATTERY_COL_TIMESTAMP > " + since, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BatteryEvent be = cursorToBatteryEvent(cursor);
            if (since < be.getTimestamp()) {
                beList.add(be);
            }
            cursor.moveToNext();
        }
        
        cursor.close();
        close();
        return null;
    }
    
    
    /**
     * Convert the cursor into a {@link BatteryEvent}-Object
     * 
     * @param cursor
     * @return {@link BatteryEvent}-Object
     */
    private BatteryEvent cursorToBatteryEvent(Cursor cursor) {
        BatteryEvent be = new BatteryEvent();
        be.setId(cursor.getInt(0));
        be.setTimestamp(cursor.getLong(1));
        be.setLevel(cursor.getInt(2));
        be.setHealth(cursor.getString(3));
        be.setStatus(cursor.getString(4));
        be.setPlugged(cursor.getString(5));
        if (cursor.getInt(6) == 1) {
            be.setPresent(true);
        } else {
            be.setPresent(false);
        }
        be.setTechnology(cursor.getString(7));
        be.setTemperature(cursor.getFloat(8));
        be.setVoltage(cursor.getInt(9));
        return be;
    }
    
}
