/*
 * Copyright 2012 pmp-android development team
 * Project: ConnectionResourceGroup
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Implements the {@link IDBConnector} interface
 * 
 * @author Thorsten Berberich
 * 
 */
public class DBConnector implements IDBConnector {
    
    /**
     * Only instance of this class
     */
    private static DBConnector instance = null;
    
    /**
     * Context of the rg
     */
    private Context context;
    
    /**
     * {@link SQLiteOpenHelper}
     */
    private SQLiteOpenHelper dbHelper;
    
    /**
     * Opened databse
     */
    private SQLiteDatabase db;
    
    
    /**
     * Singleton method to get the instance
     * 
     * @return the instance of this class
     */
    public static DBConnector getInstance(Context context) {
        if (instance == null) {
            instance = new DBConnector(context);
        }
        return instance;
    }
    
    
    /**
     * Private constructor because of the singleton
     * 
     * @param context
     *            {@link Context} of the rg
     */
    private DBConnector(final Context context) {
        this.context = context;
        initialize();
    }
    
    
    /**
     * Initialize the {@link SQLiteOpenHelper}
     */
    private void initialize() {
        this.dbHelper = new SQLiteOpenHelper(this.context, DBConstants.DATABASE_NAME, null,
                DBConstants.DATABASE_VERSION);
    }
    
    
    /**
     * Open the database
     * 
     * @throws SQLiteException
     *             thrown whenever an error happens
     */
    public void open() throws SQLiteException {
        this.db = this.dbHelper.getWritableDatabase();
    }
    
    
    /**
     * Close the databases
     */
    public void close() {
        this.dbHelper.close();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeWifiEvent(long, de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum, java.lang.String)
     */
    @Override
    public void storeWifiEvent(long timestamp, EventEnum event, String city) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        values.put(DBConstants.COLUMN_CITY, city);
        this.db.insert(DBConstants.TABLE_WIFI, null, values);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeWifiEvent(int, long, de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum, java.lang.String)
     */
    @Override
    public void storeWifiEvent(int id, long timestamp, EventEnum event, String city) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_ID, id);
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        values.put(DBConstants.COLUMN_CITY, city);
        this.db.insert(DBConstants.TABLE_WIFI, null, values);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeBTEvent(long, java.lang.String, java.lang.String)
     */
    @Override
    public void storeBTEvent(long timestamp, EventEnum event, String city) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        values.put(DBConstants.COLUMN_CITY, city);
        this.db.insert(DBConstants.TABLE_BT, null, values);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeBTEvent(int, long, de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum, java.lang.String)
     */
    @Override
    public void storeBTEvent(int id, long timestamp, EventEnum event, String city) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_ID, id);
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        values.put(DBConstants.COLUMN_CITY, city);
        this.db.insert(DBConstants.TABLE_BT, null, values);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeCellPhoneEvent(long, de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum)
     */
    @Override
    public void storeCellPhoneEvent(long timestamp, EventEnum event) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        this.db.insert(DBConstants.TABLE_CELL, null, values);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeCellPhoneEvent(int, long, java.lang.String)
     */
    @Override
    public void storeCellPhoneEvent(int id, long timestamp, EventEnum event) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_ID, id);
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        this.db.insert(DBConstants.TABLE_CELL, null, values);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#getTimeDuration(java.lang.String, long, int)
     */
    @Override
    public long getTimeDuration(String tableName, long duration, int id) {
        long result = 0L;
        
        // Get only the timestamps and event columns
        String[] columns = new String[2];
        columns[0] = DBConstants.COLUMN_TIMESTAMP;
        columns[1] = DBConstants.COLUMN_EVENT;
        
        //Get only the columns that are greater than the id
        String whereClause = DBConstants.COLUMN_ID + ">" + String.valueOf(id);
        
        // Order the events by the timestamp
        String orderBy = DBConstants.COLUMN_TIMESTAMP + " ASC";
        Cursor cursor = this.db.query(tableName, columns, whereClause, null, null, null, orderBy);
        cursor.moveToFirst();
        
        // Check this only if there are 2 events
        if (cursor.getCount() > 2) {
            long lastTimeStamp = 0;
            do {
                try {
                    long timeStamp = cursor.getLong(0);
                    String event = cursor.getString(1);
                    // Last time stamp was an off event, set this time stamp
                    if (event.equals(EventEnum.ON.toString()) && lastTimeStamp == 0) {
                        lastTimeStamp = timeStamp;
                    }
                    
                    // Last time stamp was also an on event, not good, take this event
                    if (event.equals(EventEnum.ON.toString()) && lastTimeStamp != 0) {
                        lastTimeStamp = timeStamp;
                    }
                    
                    //Last time stamp was an on event, calc the result
                    if (event.equals(EventEnum.OFF.toString()) && lastTimeStamp != 0) {
                        result += timeStamp - lastTimeStamp;
                        lastTimeStamp = 0;
                    }
                } catch (NullPointerException e) {
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#getConnectedCities(java.lang.String)
     */
    @Override
    public List<String> getConnectedCities(String tableName) {
        List<String> result = new ArrayList<String>();
        
        // Stores the times where the user was connected
        HashMap<String, Integer> times = new HashMap<String, Integer>();
        
        // Get the cities only
        String columns[] = new String[1];
        columns[0] = DBConstants.COLUMN_CITY;
        
        // Order the events by the city
        String orderBy = DBConstants.COLUMN_CITY + " ASC";
        
        Cursor cursor = this.db.query(tableName, columns, null, null, null, null, orderBy);
        cursor.moveToFirst();
        
        // Check this only if there are 1 events
        if (cursor.getCount() > 0) {
            do {
                try {
                    String city = cursor.getString(0);
                    if (city != null) {
                        if (times.containsKey(city)) {
                            // The city was seen before
                            int oldTimes = times.get(city);
                            times.put(city, oldTimes + 1);
                        } else {
                            times.put(city, 0);
                        }
                    }
                } catch (NullPointerException e) {
                }
            } while (cursor.moveToNext());
        }
        
        // Store the results in a list and sort it
        for (String key : times.keySet()) {
            int timesConnected = times.get(key);
            String toStore = timesConnected + "x " + key;
            result.add(toStore);
        }
        Collections.sort(result, new ConnectedCitiesComparator());
        cursor.close();
        
        return result;
    }
}