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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.ConnectionConstants;

/**
 * {@link SQLiteOpenHelper} for the connection RG
 * 
 * @author Thorsten Berberich
 * 
 */
public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {
    
    /**
     * Create statement for the wifi table with the columns id, timestamp, event and city
     */
    private static final String CREATE_WIFI_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_WIFI + " ( "
            + Constants.COLUMN_ID + " integer primary key autoincrement, " + Constants.COLUMN_TIMESTAMP + " integer, "
            + Constants.COLUMN_EVENT + " text, " + Constants.COLUMN_CITY + " text );";
    
    /**
     * Create statement for the bluetooth table with the columns id, timestamp, event and city
     */
    private static final String CREATE_BT_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_BT + " ( "
            + Constants.COLUMN_ID + " integer primary key autoincrement, " + Constants.COLUMN_TIMESTAMP + " integer, "
            + Constants.COLUMN_EVENT + " text, " + Constants.COLUMN_CITY + " text );";
    
    /**
     * Create statement for the cell phone table with the columns id, timestamp and event
     */
    private static final String CREATE_CELL_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_CELL + " ( "
            + Constants.COLUMN_ID + " integer primary key autoincrement, " + Constants.COLUMN_TIMESTAMP + " integer, "
            + Constants.COLUMN_EVENT + " text ); ";
    
    
    /**
     * Constructor
     * 
     * @param context
     *            Context
     * @param name
     *            database name
     * @param factory
     * @param version
     *            Database version
     */
    public SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    
    
    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WIFI_TABLE);
        db.execSQL(CREATE_BT_TABLE);
        db.execSQL(CREATE_CELL_TABLE);
    }
    
    
    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ConnectionConstants.LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_BT);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CELL);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_WIFI);
        onCreate(db);
    }
    
}
