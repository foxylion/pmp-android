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
    
    
    public void storeScreenEvent(ScreenEvent se) {
        
        // Create content values
        ContentValues cvs = new ContentValues();
        cvs.put(DBConstants.TABLE_SCREEN_COL_TIMESTAMP, se.getTimestamp());
        cvs.put(DBConstants.TABLE_SCREEN_COL_CHANGED_TO, se.isChangedTo());
        
        // Insert into database
        open();
        long id = this.database.insert(DBConstants.TABLE_SCREEN, null, cvs);
        close();
        
        // Log
        Log.i(EnergyConstants.LOG_TAG, "Stored screen event in database (ID: " + id + ")");
        Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + se.getTimestamp());
        Log.i(EnergyConstants.LOG_TAG, "Changed to: " + se.isChangedTo());
    }
    
    
    public void storeDeviceBootEvent(DeviceBootEvent dbe) {
        
        // Create content values
        ContentValues cvs = new ContentValues();
        cvs.put(DBConstants.TABLE_DEVICE_BOOT_COL_TIMESTAMP, dbe.getTimestamp());
        cvs.put(DBConstants.TABLE_DEVICE_BOOT_COL_CHANGED_TO, dbe.isChangedTo());
        
        // Insert into database
        open();
        long id = this.database.insert(DBConstants.TABLE_DEVICE_BOOT, null, cvs);
        close();
        
        // Log
        Log.i(EnergyConstants.LOG_TAG, "Stored device boot event in database (ID: " + id + ")");
        Log.i(EnergyConstants.LOG_TAG, "Timestamp: " + dbe.getTimestamp());
        Log.i(EnergyConstants.LOG_TAG, "Changed to: " + dbe.isChangedTo());
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
            // pretty print 
            long currentTime = System.currentTimeMillis();
            long statusTimeInSec = (currentTime - pluggedTime) / 1000;
            long days = statusTimeInSec / 60 / 60 / 24;
            long daysInSec = days * 60 * 60 * 24;
            long hours = ((statusTimeInSec - daysInSec) / 60 / 60);
            long hoursInSec = hours * 60 * 60;
            long mins = ((statusTimeInSec - daysInSec - hoursInSec) / 60);
            long minsInSec = mins * 60;
            long secs = statusTimeInSec - daysInSec - hoursInSec - minsInSec;
            
            StringBuilder sb = new StringBuilder();
            if (days > 0)
                sb.append(String.valueOf(days) + "d ");
            if (hours > 0)
                sb.append(String.valueOf(hours) + "h ");
            if (mins > 0)
                sb.append(String.valueOf(mins) + "m ");
            sb.append(String.valueOf(secs) + "s");
            rs.setStatusTime(sb.toString());
        }
        cursor.close();
        close();
        return rs;
    }
    
    
    public ResultSetLastBootValues getLastBootValues() {
        
        // Get the last boot date
        long lastBootDate;
        open();
        Cursor cursor = database.query(DBConstants.TABLE_DEVICE_BOOT, DBConstants.TABLE_DEVICE_BOOT_ALL_COLS, null,
                null, null, null, null);
        if (cursor.moveToLast()) {
            lastBootDate = cursor.getLong(1);
        } else {
            lastBootDate = 0;
        }
        close();
        
        // Return the result set
        return (ResultSetLastBootValues) createResultSet(getAllBatteryEvents(lastBootDate),
                getAllScreenEvents(lastBootDate), getAllDeviceBootEvents(lastBootDate), new ResultSetLastBootValues(),
                lastBootDate);
        
    }
    
    
    public ResultSetTotalValues getTotalValues() {
        
        // Get the date of first measurement
        long dateOfFirstMeasurement;
        open();
        Cursor cursor = database.query(DBConstants.TABLE_BATTERY, DBConstants.TABLE_BATTERY_ALL_COLS, null, null, null,
                null, null);
        if (cursor.moveToFirst()) {
            dateOfFirstMeasurement = cursor.getLong(1);
        } else {
            dateOfFirstMeasurement = 0;
        }
        close();
        
        // Return the result set
        return (ResultSetTotalValues) createResultSet(getAllBatteryEvents(dateOfFirstMeasurement),
                getAllScreenEvents(dateOfFirstMeasurement), getAllDeviceBootEvents(dateOfFirstMeasurement),
                new ResultSetTotalValues(), dateOfFirstMeasurement);
    }
    
    
    public ResultSetUpload getUploadValues() {
        ResultSetUpload rs = new ResultSetUpload();
        rs.setBatteryEvents(getAllBatteryEvents(0));
        return rs;
    }
    
    
    /**
     * Create the result set for Last boot values und total values
     * 
     * @param beList
     * @param rs
     * @param date
     */
    private AbstractResultSet createResultSet(List<BatteryEvent> beList, List<ScreenEvent> seList,
            List<DeviceBootEvent> dbeList, AbstractResultSet rs, long date) {
        
        /*
         *  Set the date
         */
        rs.setDate(String.valueOf(date));
        
        /*
         * Set the uptime 
         */
        System.out.println(beList.size());
        System.out.println(dbeList.size());
        if (beList.size() > 0 && dbeList.size() > 0) {
            long uptime = 0;
            long lastUptimeTimeStamp = dbeList.get(0).getTimestamp();
            boolean deviceOn = dbeList.get(0).isChangedTo();
            for (int itr = 1; itr < dbeList.size(); itr++) {
                DeviceBootEvent dbe = dbeList.get(itr);
                if (deviceOn && !dbe.isChangedTo()) {
                    uptime += dbe.getTimestamp() - lastUptimeTimeStamp;
                    deviceOn = false;
                } else if (!deviceOn && dbe.isChangedTo()) {
                    deviceOn = true;
                }
                lastUptimeTimeStamp = dbe.getTimestamp();
            }
            rs.setUptime(String.valueOf(uptime));
            
            /*
             * Set the duration of charging
             */
            long durationOfCharging = 0;
            long lastDurationTimeStamp = dbeList.get(0).getTimestamp();
            boolean isChargingD = beList.get(0).getStatus().equals(EnergyConstants.STATUS_CHARGING);
            for (BatteryEvent be : beList) {
                if (isChargingD && !be.getStatus().equals(EnergyConstants.STATUS_CHARGING)) {
                    isChargingD = false;
                } else if (!isChargingD && be.getStatus().equals(EnergyConstants.STATUS_CHARGING)) {
                    isChargingD = true;
                    durationOfCharging += be.getTimestamp() - lastDurationTimeStamp;
                }
            }
            rs.setDurationOfCharging(String.valueOf(durationOfCharging));
            
            /*
             * Set the uptime with battery
             */
            long uptimeBattery = uptime - durationOfCharging;
            rs.setUptimeBattery(String.valueOf(uptimeBattery));
            
            /*
             * Set the ratio (charging:battery)
             */
            //            float ratio = durationOfCharging / uptimeBattery;
            //            rs.setRatio(String.valueOf(ratio));
            
            /*
             * Set the count of charging
             */
            boolean isCharging = beList.get(0).getStatus().equals(EnergyConstants.STATUS_CHARGING);
            int chargeCount = 0;
            if (isCharging) {
                chargeCount++;
            }
            for (BatteryEvent be : beList) {
                if (isCharging && !be.getStatus().equals(EnergyConstants.STATUS_CHARGING)) {
                    isCharging = false;
                } else if (!isCharging && be.getStatus().equals(EnergyConstants.STATUS_CHARGING)) {
                    isCharging = true;
                    chargeCount++;
                }
            }
            rs.setCountOfCharging(String.valueOf(chargeCount) + " times");
            
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
            rs.setTemperatureAverage(String.valueOf(temperatureSum / beList.size()) + "°C");
            
            // Set the temperature peak
            rs.setTemperaturePeak(String.valueOf(temperaturePeak) + "°C");
        }
        
        /*
         * Set the screen on time
         */
        if (seList.size() > 0) {
            long time = 0;
            long lastTimeStamp = seList.get(0).getTimestamp();
            boolean screenOn = seList.get(0).isChangedTo();
            for (int itr = 1; itr < seList.size(); itr++) {
                ScreenEvent se = seList.get(itr);
                if (screenOn && !se.isChangedTo()) {
                    time += se.getTimestamp() - lastTimeStamp;
                    screenOn = false;
                } else if (!screenOn && se.isChangedTo()) {
                    screenOn = true;
                }
                lastTimeStamp = se.getTimestamp();
            }
            rs.setScreenOn(String.valueOf(time));
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
     * Get all screen events since a given time
     * 
     * @param since
     *            time in ms since 1970
     * @return list of screen events
     */
    private List<ScreenEvent> getAllScreenEvents(long since) {
        List<ScreenEvent> seList = new ArrayList<ScreenEvent>();
        open();
        Cursor cursor = database.query(DBConstants.TABLE_SCREEN, DBConstants.TABLE_SCREEN_ALL_COLS,
                DBConstants.TABLE_SCREEN_COL_TIMESTAMP + " >= " + since, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            seList.add(cursorToScreenEvent(cursor));
            cursor.moveToNext();
        }
        
        cursor.close();
        close();
        return seList;
    }
    
    
    /**
     * Get all device boot events since a given time
     * 
     * @param since
     *            time in ms since 1970
     * @return list of device boot events
     */
    private List<DeviceBootEvent> getAllDeviceBootEvents(long since) {
        List<DeviceBootEvent> dbeList = new ArrayList<DeviceBootEvent>();
        open();
        Cursor cursor = database.query(DBConstants.TABLE_DEVICE_BOOT, DBConstants.TABLE_DEVICE_BOOT_ALL_COLS,
                DBConstants.TABLE_DEVICE_BOOT_COL_TIMESTAMP + " >= " + since, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dbeList.add(cursorToDeviceBootEvent(cursor));
            cursor.moveToNext();
        }
        
        cursor.close();
        close();
        return dbeList;
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
     * Convert the cursor into a {@link ScreenEvent}-Object
     * 
     * @param cursor
     * @return {@link ScreenEvent}-Object
     */
    private ScreenEvent cursorToScreenEvent(Cursor cursor) {
        ScreenEvent se = new ScreenEvent();
        se.setId(cursor.getInt(0));
        se.setTimestamp(cursor.getLong(1));
        if (cursor.getInt(2) == 1) {
            se.setChangedTo(true);
        } else {
            se.setChangedTo(false);
        }
        return se;
    }
    
    
    /**
     * Convert the cursor into a {@link DeviceBootEvent}-Object
     * 
     * @param cursor
     * @return {@link DeviceBootEvent}-Object
     */
    private DeviceBootEvent cursorToDeviceBootEvent(Cursor cursor) {
        DeviceBootEvent dbe = new DeviceBootEvent();
        dbe.setId(cursor.getInt(0));
        dbe.setTimestamp(cursor.getLong(1));
        if (cursor.getInt(2) == 1) {
            dbe.setChangedTo(true);
        } else {
            dbe.setChangedTo(false);
        }
        return dbe;
    }
    
}
