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
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.CellularConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent.Mediums;

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
     * Opened database
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
    private void open() throws SQLiteException {
        this.db = this.dbHelper.getWritableDatabase();
    }
    
    
    /**
     * Close the databases
     */
    private void close() {
        this.dbHelper.close();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeWifiEvent(long, de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum, java.lang.String)
     */
    @Override
    public synchronized void storeWifiEvent(long timestamp, Events event, String city, Events state) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        values.put(DBConstants.COLUMN_CITY, city);
        values.put(DBConstants.COLUMN_STATE, state.toString());
        this.db.insert(DBConstants.TABLE_WIFI, null, values);
        close();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeWifiEvent(int, long, de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum, java.lang.String)
     */
    @Override
    @Deprecated
    public synchronized void storeWifiEvent(int id, long timestamp, Events event, String city) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_ID, id);
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        values.put(DBConstants.COLUMN_CITY, city);
        this.db.insert(DBConstants.TABLE_WIFI, null, values);
        close();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeBTEvent(long, java.lang.String, java.lang.String)
     */
    @Override
    public synchronized void storeBTEvent(long timestamp, Events event, String city) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        values.put(DBConstants.COLUMN_CITY, city);
        this.db.insert(DBConstants.TABLE_BT, null, values);
        close();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeBTEvent(int, long, de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum, java.lang.String)
     */
    @Override
    @Deprecated
    public synchronized void storeBTEvent(int id, long timestamp, Events event, String city) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_ID, id);
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        values.put(DBConstants.COLUMN_CITY, city);
        this.db.insert(DBConstants.TABLE_BT, null, values);
        close();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeCellPhoneEvent(long, de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.EventEnum)
     */
    @Override
    public synchronized void storeCellPhoneEvent(long timestamp, Events event) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        this.db.insert(DBConstants.TABLE_CELL, null, values);
        close();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#storeCellPhoneEvent(int, long, java.lang.String)
     */
    @Override
    @Deprecated
    public synchronized void storeCellPhoneEvent(int id, long timestamp, Events event) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_ID, id);
        values.put(DBConstants.COLUMN_TIMESTAMP, timestamp);
        values.put(DBConstants.COLUMN_EVENT, event.toString());
        this.db.insert(DBConstants.TABLE_CELL, null, values);
        close();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#getTimeDuration(java.lang.String, long, int)
     */
    @Override
    public synchronized long getTimeDuration(String tableName, long duration, int id) {
        open();
        long actualTime = new java.util.Date().getTime();
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
        
        long lastTimeStamp = 0;
        
        // Check this only if there are 2 events
        if (cursor.getCount() >= 2) {
            do {
                try {
                    long timeStamp = cursor.getLong(0);
                    String event = cursor.getString(1);
                    // Last time stamp was an off event, set this time stamp
                    if (event.equals(Events.ON.toString()) && lastTimeStamp == 0) {
                        lastTimeStamp = timeStamp;
                    }
                    
                    // Last time stamp was also an on event, not good, take this event
                    if (event.equals(Events.ON.toString()) && lastTimeStamp != 0) {
                        lastTimeStamp = timeStamp;
                    }
                    
                    //Last time stamp was an on event, calc the result
                    if (event.equals(Events.OFF.toString()) && lastTimeStamp != 0) {
                        result += timeStamp - lastTimeStamp;
                        lastTimeStamp = 0;
                    }
                } catch (NullPointerException e) {
                }
                
            } while (cursor.moveToNext());
        }
        
        if (lastTimeStamp != 0) {
            result += actualTime - lastTimeStamp;
        }
        
        cursor.close();
        close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#getConnectedCities(java.lang.String)
     */
    @Override
    public synchronized List<String> getConnectedCities(String tableName) {
        open();
        List<String> result = new ArrayList<String>();
        
        // Get the cities only
        String columns[] = new String[1];
        columns[0] = DBConstants.COLUMN_CITY;
        
        // Order the events by the city
        String orderBy = DBConstants.COLUMN_CITY + " ASC";
        
        Cursor cursor = this.db.query(tableName, columns, null, null, null, null, orderBy);
        cursor.moveToFirst();
        
        // Check this only if there are more than one event
        if (cursor.getCount() > 0) {
            do {
                try {
                    String city = cursor.getString(0);
                    if (city != null) {
                        if (!result.contains(city)) {
                            result.add(city);
                        }
                    }
                } catch (NullPointerException e) {
                }
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        close();
        
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#getWifiEvents()
     */
    @Override
    public synchronized List<ConnectionEvent> getWifiEvents() {
        open();
        List<ConnectionEvent> result = new ArrayList<ConnectionEvent>();
        
        String orderBy = DBConstants.COLUMN_TIMESTAMP + " ASC";
        
        // Get everything
        String columns[] = new String[4];
        columns[0] = DBConstants.COLUMN_TIMESTAMP;
        columns[1] = DBConstants.COLUMN_EVENT;
        columns[2] = DBConstants.COLUMN_CITY;
        columns[3] = DBConstants.COLUMN_STATE;
        
        Cursor cursor = this.db.query(DBConstants.TABLE_WIFI, columns, null, null, null, null, orderBy);
        cursor.moveToFirst();
        
        if (cursor.getCount() > 0) {
            do {
                long timeStamp = cursor.getLong(0);
                
                String eventString = cursor.getString(1);
                boolean event = false;
                if (eventString.equals(Events.ON.toString())) {
                    event = true;
                }
                String city = null;
                if (cursor.getString(2) != null) {
                    city = cursor.getString(2);
                }
                String stateString = cursor.getString(3);
                boolean state = false;
                if (stateString.equals(Events.ON.toString())) {
                    state = true;
                }
                result.add(new ConnectionEvent(timeStamp, Mediums.WIFI, event, state, city));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#getBluetoothEvents()
     */
    @Override
    public synchronized List<ConnectionEvent> getBluetoothEvents() {
        open();
        List<ConnectionEvent> result = new ArrayList<ConnectionEvent>();
        
        String orderBy = DBConstants.COLUMN_TIMESTAMP + " ASC";
        
        // Get everything
        String columns[] = new String[3];
        columns[0] = DBConstants.COLUMN_TIMESTAMP;
        columns[1] = DBConstants.COLUMN_EVENT;
        columns[2] = DBConstants.COLUMN_CITY;
        
        Cursor cursor = this.db.query(DBConstants.TABLE_BT, columns, null, null, null, null, orderBy);
        cursor.moveToFirst();
        
        if (cursor.getCount() > 0) {
            do {
                long timeStamp = cursor.getLong(0);
                String eventString = cursor.getString(1);
                boolean event = false;
                if (eventString.equals(Events.ON.toString())) {
                    event = true;
                }
                String city = cursor.getString(2);
                result.add(new ConnectionEvent(timeStamp, Mediums.BLUETOOTH, false, event, city));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resourcegroups.connection.database.IDBConnector#getCellEvents()
     */
    @Override
    public synchronized List<CellularConnectionEvent> getCellEvents() {
        open();
        List<CellularConnectionEvent> result = new ArrayList<CellularConnectionEvent>();
        
        String orderBy = DBConstants.COLUMN_TIMESTAMP + " ASC";
        
        // Get everything
        String columns[] = new String[2];
        columns[0] = DBConstants.COLUMN_TIMESTAMP;
        columns[1] = DBConstants.COLUMN_EVENT;
        
        Cursor cursor = this.db.query(DBConstants.TABLE_CELL, columns, null, null, null, null, orderBy);
        cursor.moveToFirst();
        
        if (cursor.getCount() > 0) {
            do {
                long timeStamp = cursor.getLong(0);
                String eventString = cursor.getString(1);
                boolean event = false;
                if (eventString.equals(Events.ON.toString())) {
                    event = true;
                }
                
                result.add(new CellularConnectionEvent(timeStamp, event));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return result;
    }
    
    
    /**
     * Clears all lists
     */
    public synchronized void clearLists() {
        open();
        this.db.delete(DBConstants.TABLE_BT, null, null);
        this.db.delete(DBConstants.TABLE_CELL, null, null);
        this.db.delete(DBConstants.TABLE_WIFI, null, null);
        close();
    }
}
