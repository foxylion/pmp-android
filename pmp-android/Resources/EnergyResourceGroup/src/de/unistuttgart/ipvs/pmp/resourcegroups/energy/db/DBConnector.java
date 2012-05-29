package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.DeviceBootEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.ScreenEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.AbstractResultSet;
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
        
        DBConnector.instance = new DBConnector(context);
        
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
    
    
    /**
     * Store a screen event to the database
     */
    public void storeScreenEvent(ScreenEvent se) {
        if (se.isChangedTo()) {
            
            // Update last screen on date
            this.updateDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_SCREEN_ON_DATE, se.getTimestamp());
            
            // Log
            Log.i(EnergyConstants.LOG_TAG, "Screen turned on (Timestamp: " + se.getTimestamp() + ")");
            
        } else {
            
            // Get last screen on date
            long lastScreenOnDate = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_SCREEN_ON_DATE);
            
            // Get current screen on time
            long currentScreenOnTime = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_SCREEN_ON_TIME);
            
            // Get current screen on time since last boot
            long currentScreenOnTimeLastBoot = this
                    .getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_BOOT_SCREEN_ON_TIME);
            
            // New screen on time
            long newScreenOnTime = (se.getTimestamp() - lastScreenOnDate) + currentScreenOnTime;
            
            // New screen on time last boot
            long newScreenOnTimeLastBoot = (se.getTimestamp() - lastScreenOnDate) + currentScreenOnTimeLastBoot;
            
            // Update screen on time
            this.updateDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_SCREEN_ON_TIME, newScreenOnTime);
            
            // Update screen on time since last boot
            this.updateDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_BOOT_SCREEN_ON_TIME,
                    newScreenOnTimeLastBoot);
            
            // Log
            Log.i(EnergyConstants.LOG_TAG, "Screen turned off (New screen-on time: " + newScreenOnTime
                    + ", since last boot: " + newScreenOnTimeLastBoot + ")");
        }
    }
    
    
    /**
     * Store a device boot event to the database
     */
    public void storeDeviceBootEvent(DeviceBootEvent dbe) {
        // Check, if deviceOnFlag = 0 or = 1
        long deviceOn = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_DEVICE_ON_FLAG);
        boolean deviceOnFlag = false;
        if (deviceOn == 1) {
            deviceOnFlag = true;
        }
        
        if (dbe.isChangedTo() && !deviceOnFlag) {
            // Set the deviceOn flag to 1
            this.updateDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_DEVICE_ON_FLAG, 1);
            
            // Update the last boot date
            this.updateDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_BOOT_DATE, dbe.getTimestamp());
            
            // Reset the screen on time since last boot
            this.updateDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_BOOT_SCREEN_ON_TIME, 0);
            
            // Log
            Log.i(EnergyConstants.LOG_TAG, "Device turned on (New last boot date: " + dbe.getTimestamp() + ")");
            
        } else if (!dbe.isChangedTo() && deviceOnFlag) {
            // Set the deviceOn flag to 0
            this.updateDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_DEVICE_ON_FLAG, 0);
            
            // Get the current total uptime
            long currentTotalUptime = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_TOTAL_UPTIME);
            
            // Get the last boot date
            long lastBootDate = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_BOOT_DATE);
            
            // Update the total uptime
            long newTotalUptime = currentTotalUptime + (dbe.getTimestamp() - lastBootDate);
            this.updateDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_TOTAL_UPTIME, newTotalUptime);
            
            // Log
            Log.i(EnergyConstants.LOG_TAG, "Device turned off (New total uptime: " + newTotalUptime + ")");
        }
    }
    
    
    /**
     * Get the result set of current values
     */
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
            // pretty print
            try {
                rs.setStatusTime(Util.convertMillisecondsToString(System.currentTimeMillis() - pluggedTime));
            } catch (Exception e) {
                rs.setStatusTime(String.valueOf(System.currentTimeMillis() - pluggedTime) + " ms");
            }
        }
        cursor.close();
        close();
        return rs;
    }
    
    
    /**
     * Get the result set of the last boot values
     */
    public ResultSetLastBootValues getLastBootValues() {
        
        // Get the last boot date
        long lastBootDate = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_BOOT_DATE);
        
        // Return the result set
        return (ResultSetLastBootValues) createResultSet(this.getAllBatteryEvents(lastBootDate),
                new ResultSetLastBootValues(), lastBootDate);
        
    }
    
    
    /**
     * Get the result set of the total values
     */
    public ResultSetTotalValues getTotalValues() {
        
        // Get the date of first measurement
        long dateOfFirstMeasurement = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_FIRST_MEASUREMENT_DATE);
        
        // Return the result set
        return (ResultSetTotalValues) createResultSet(getAllBatteryEvents(dateOfFirstMeasurement),
                new ResultSetTotalValues(), dateOfFirstMeasurement);
    }
    
    
    public ResultSetUpload getUploadValues() {
        ResultSetUpload rs = new ResultSetUpload();
        rs.setBatteryEvents(getAllBatteryEvents(0));
        return rs;
    }
    
    
    /**
     * Create the result set for last boot values and total values
     * 
     * @param beList
     *            list of battery events
     * @param seList
     *            list of screen events
     * @param dbeList
     *            list of device boot events
     * @param rs
     *            the abstract result set
     * @param date
     *            the date
     */
    private AbstractResultSet createResultSet(List<BatteryEvent> beList, AbstractResultSet rs, long date) {
        
        /*
         *  Set the date
         */
        rs.setDate(String.valueOf(date));
        
        /*
         * Set the uptime
         */
        long lastBootDate = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_BOOT_DATE);
        long lastBootUptime = System.currentTimeMillis() - lastBootDate;
        long uptime = 0;
        if (rs instanceof ResultSetLastBootValues) {
            uptime = lastBootUptime;
        } else if (rs instanceof ResultSetTotalValues) {
            uptime = lastBootUptime + this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_TOTAL_UPTIME);
        }
        // Pretty format
        try {
            rs.setUptime(Util.convertMillisecondsToString(uptime));
        } catch (Exception e) {
            rs.setUptime(String.valueOf(uptime) + " ms");
        }
        
        /*
         * Set the duration of charging and count charging processes
         */
        if (beList.size() > 0) {
            long durationOfCharging = 0;
            long lastTimestampChargingTurnedOn = 0;
            
            // Start up condition
            boolean isCharging = beList.get(0).getStatus().equals(EnergyConstants.STATUS_CHARGING);
            if (isCharging) {
                lastTimestampChargingTurnedOn = beList.get(0).getTimestamp();
            }
            
            // Charging process counter
            int chargingProcesses = 0;
            
            // Traverse battery events
            for (BatteryEvent be : beList) {
                
                if (!isCharging && be.getStatus().equals(EnergyConstants.STATUS_CHARGING)) {
                    isCharging = true;
                    lastTimestampChargingTurnedOn = be.getTimestamp();
                    chargingProcesses++;
                } else if (isCharging && !be.getStatus().equals(EnergyConstants.STATUS_CHARGING)) {
                    isCharging = false;
                    durationOfCharging += be.getTimestamp() - lastTimestampChargingTurnedOn;
                }
            }
            // Add the current charging time, if the device is charging currently
            if (isCharging) {
                durationOfCharging += (System.currentTimeMillis() - beList.get(beList.size() - 1).getTimestamp());
            }
            // Pretty format
            try {
                rs.setDurationOfCharging(Util.convertMillisecondsToString(durationOfCharging));
            } catch (Exception e) {
                rs.setDurationOfCharging(String.valueOf(durationOfCharging) + " ms");
            }
            
            /*
             * Set the uptime with battery
             */
            long uptimeBattery = uptime - durationOfCharging;
            // Pretty format
            try {
                rs.setUptimeBattery(Util.convertMillisecondsToString(uptimeBattery));
            } catch (Exception e) {
                rs.setUptimeBattery(String.valueOf(uptimeBattery) + " ms");
            }
            
            /*
             * Set the ratio (charging:battery)
             */
            try {
                if (durationOfCharging > uptimeBattery) {
                    int normalizedValue = Math.round(durationOfCharging / uptimeBattery);
                    rs.setRatio(String.valueOf(normalizedValue) + ":1");
                } else if (durationOfCharging < uptimeBattery) {
                    int normalizedValue = Math.round(uptimeBattery / durationOfCharging);
                    rs.setRatio("1:" + String.valueOf(normalizedValue));
                } else {
                    rs.setRatio("1:1");
                }
            } catch (Exception e) {
                rs.setRatio("-");
            }
            
            /*
             * Set the count of charging
             */
            rs.setCountOfCharging(String.valueOf(chargingProcesses) + " times");
            
            /*
             * Set temperature peak and average
             */
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
            String tempAverage = String.valueOf(temperatureSum / beList.size());
            // Cut the length of the string
            if (tempAverage.length() > 4) {
                tempAverage = tempAverage.substring(0, 4);
            }
            rs.setTemperatureAverage(tempAverage + "°C");
            
            // Set the temperature peak
            rs.setTemperaturePeak(String.valueOf(temperaturePeak) + "°C");
        }
        
        /*
         * Set the screen on time
         */
        long screenOnTimeStored = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_SCREEN_ON_TIME);
        long screenOnLastBootTimeStored = this
                .getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_BOOT_SCREEN_ON_TIME);
        long lastScreenOnDate = this.getDeviceDataValue(DBConstants.TABLE_DEVICE_DATA_KEY_LAST_SCREEN_ON_DATE);
        
        // Add the current screen on time
        long screenOnTime = System.currentTimeMillis() - lastScreenOnDate;
        
        if (rs instanceof ResultSetLastBootValues) {
            screenOnTime += screenOnLastBootTimeStored;
        } else if (rs instanceof ResultSetTotalValues) {
            screenOnTime += screenOnTimeStored;
        }
        
        // Pretty format
        try {
            rs.setScreenOn(Util.convertMillisecondsToString(screenOnTime));
        } catch (Exception e) {
            rs.setScreenOn(String.valueOf(screenOnTime) + " ms");
        }
        
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
                DBConstants.TABLE_BATTERY_COL_TIMESTAMP + " >= " + since, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            beList.add(cursorToBatteryEvent(cursor));
            cursor.moveToNext();
        }
        
        cursor.close();
        close();
        
        return beList;
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
    
    
    /**
     * INTERNAL USE ONLY!
     * 
     * @param key
     *            the key
     * @return value of the key-value-pair of the table "device data"
     */
    private long getDeviceDataValue(String key) {
        open();
        
        String whereClause = DBConstants.TABLE_DEVICE_DATA_COL_KEY + "='" + key + "'";
        Cursor cursor = this.database.query(DBConstants.TABLE_DEVICE_DATA, DBConstants.TABLE_DEVICE_DATA_ALL_COLS,
                whereClause, null, null, null, null);
        cursor.moveToFirst();
        long returnValue = cursor.getLong(1);
        
        cursor.close();
        close();
        
        return returnValue;
    }
    
    
    /**
     * INTERNAL USE ONLY!
     * 
     * @param key
     *            the key
     * @param value
     *            the value of the key-value-pair of the table "device data"
     */
    private void updateDeviceDataValue(String key, long value) {
        open();
        
        // Update screen on time
        ContentValues cvs = new ContentValues();
        cvs.put(DBConstants.TABLE_DEVICE_DATA_COL_VALUE, value);
        
        // Where clause
        String whereClause = DBConstants.TABLE_DEVICE_DATA_COL_KEY + "='" + key + "'";
        
        // Insert into database
        this.database.update(DBConstants.TABLE_DEVICE_DATA, cvs, whereClause, null);
        
        close();
    }
    
}
