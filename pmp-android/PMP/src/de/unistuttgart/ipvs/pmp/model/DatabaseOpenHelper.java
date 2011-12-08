/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.Log;

/**
 * This is a helper for opening the database used by PMP.<br/>
 * It automatically creates the required tables in the SQLite database.<br/>
 * A database instance can be got by calling {@link DatabaseOpenHelper#getWritableDatabase()}.
 * 
 * @author Jakob Jarosch
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    
    /**
     * Name of the database.
     */
    private static final String DB_NAME = "pmp-database";
    
    /**
     * Current database version.
     */
    private static final int DB_VERSION = 2;
    
    /**
     * The context used to open the files from assets folder.
     */
    private Context context;
    
    /**
     * The query builder
     */
    private SQLiteQueryBuilder sqlqb;
    
    /**
     * List of all SQL-files for database-creation, the key is the version of the database.
     */
    private static final String[] SQL_FILES = new String[] { null, "database-v1.sql", "database-v2.sql" };
    
    /**
     * List of all SQL-files for database-clean, the key is the version of the database.
     */
    private static final String[] CLEAN_SQL_FILES = new String[] { null, "database-v1-clean.sql",
            "database-v2-clean.sql" };
    
    
    /**
     * DatabaseHelper-Constructor.
     */
    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.sqlqb = new SQLiteQueryBuilder();
    }
    
    
    /**
     * Called when the database is opened first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("creating database structure.");
        
        String sqlQueries = readSqlFile(SQL_FILES[DB_VERSION]);
        
        if (sqlQueries != null) {
            Log.d("Successfully read the database from " + SQL_FILES[DB_VERSION] + ", executing now...");
            DatabaseOpenHelper.executeMultipleQueries(db, sqlQueries);
            Log.d("Created the database (with or without errors, see above).");
        }
    }
    
    
    /**
     * Called when upgrading from a previous version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Will try to update database from " + oldVersion + " to " + newVersion);
        
        if ((newVersion == 2) && (oldVersion < 2)) {
            // delete everything in sight
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata';", null);
            if (c.moveToFirst()) {
                do {
                    db.execSQL("DROP TABLE " + c.getString(c.getColumnIndex("name")));
                } while (c.moveToNext());
            }
            
            // rerun creation
            onCreate(db);
        }
    }
    
    
    /**
     * Cleans all data from the tables.
     */
    public void cleanTables() {
        Log.d("Cleaning database.");
        
        String sqlQueries = readSqlFile(CLEAN_SQL_FILES[DB_VERSION]);
        
        if (sqlQueries != null) {
            Log.d("Successfully read the queries from " + CLEAN_SQL_FILES[DB_VERSION] + ", executing now...");
            DatabaseOpenHelper.executeMultipleQueries(getWritableDatabase(), sqlQueries);
            Log.d("Cleaned database (with, or without errors, see above).");
        }
    }
    
    
    /**
     * Read a SQL file from assets folder.
     * 
     * @param filename
     *            Filename of the SQL file.
     * 
     * @return String represented SQL query from the file. null if the file could not be read.
     */
    public String readSqlFile(String filename) {
        String sqlQuery = null;
        
        try {
            InputStream is = this.context.getAssets().open(filename);
            InputStreamReader bis = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(bis);
            
            StringBuilder sb = new StringBuilder();
            String curLine = null;
            while ((curLine = br.readLine()) != null) {
                sb.append(curLine);
                sb.append("\n");
            }
            
            br.close();
            bis.close();
            br.close();
            
            sqlQuery = sb.toString();
            
        } catch (IOException e) {
            Log.e("Reading the SQL file from " + filename + " failed.", e);
            sqlQuery = null;
        }
        
        return sqlQuery;
    }
    
    
    /**
     * This method executes multiple queries, which are concatenated by a semicolon.
     * 
     * @param db
     *            The {@link SQLiteDatabase} which should be used to execute the queries.
     * @param queries
     *            The queries which should be executed.
     */
    public static void executeMultipleQueries(SQLiteDatabase db, String queries) {
        Log.v("------- SQL-Queries to be executed ------");
        
        for (String query : queries.split(";")) {
            
            /* Skipping, empty query */
            if (query.trim().length() == 0) {
                continue;
            }
            
            Log.v(query);
            
            try {
                db.execSQL(query);
                Log.d("Query execution successful");
            } catch (SQLException e) {
                Log.e("Got an SQLException while executing query", e);
            }
        }
        
        Log.v("-------     End of SQL-Queries     ------");
    }
    
    
    public SQLiteQueryBuilder builder() {
        return this.sqlqb;
    }
    
}
