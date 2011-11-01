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

import android.database.Cursor;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;

/**
 * {@link DatabaseSingleton} provides an instance of the {@link DatabaseOpenHelper}, and can be received by calling
 * <code>DatabaseSingleton.getInstance().getDatabaseHelper();</code>.
 * 
 * @author Jakob Jarosch
 * 
 */
public class DatabaseSingleton {
    
    /**
     * List of all SQL-files for adding sample values to the database, the key is the version of the database.
     */
    private static final String[] SAMPLE_SQL_FILES = new String[] { null, "samples-v1.sql" };
    
    /**
     * List of all SQL-files for cleaning sample values from the database, the key is the version of the database.
     */
    private static final String[] CLEAN_SAMPLE_SQL_FILES = new String[] { null, "samples-v1-clean.sql" };
    
    /**
     * The instance.
     */
    private static final DatabaseSingleton instance = new DatabaseSingleton();
    
    /**
     * A {@link DatabaseOpenHelper} instance.
     */
    private DatabaseOpenHelper doh = null;
    
    
    /**
     * Private constructor, prevents direct object creation.
     */
    private DatabaseSingleton() {
        
    }
    
    
    /**
     * @return Returns an instance of the {@link DatabaseSingleton} class.
     */
    public static DatabaseSingleton getInstance() {
        return instance;
    }
    
    
    /**
     * @return Returns an instance of the {@link DatabaseOpenHelper} class.
     */
    public DatabaseOpenHelper getDatabaseHelper() {
        if (this.doh == null) {
            this.doh = new DatabaseOpenHelper(PMPApplication.getContext());
        }
        
        return this.doh;
    }
    
    
    public void createSampleData() {
        Log.d("Sample data will be installed, read the queries from " + SAMPLE_SQL_FILES[1]);
        
        String sqlQueries = getDatabaseHelper().readSqlFile(SAMPLE_SQL_FILES[1]);
        
        if (sqlQueries != null) {
            Log.d("Loaded sample file, inserting them into database...");
            DatabaseOpenHelper.executeMultipleQueries(getDatabaseHelper().getWritableDatabase(), sqlQueries);
            Log.d("Inserted queries into database (with, or without errors, see above).");
        }
    }
    
    
    public boolean isSampleDataInstalled() {
        Cursor cursor = getDatabaseHelper().getReadableDatabase().rawQuery(
                "SELECT Identifier FROM App WHERE Identifier LIKE 'Sample#%'", null);
        return cursor.getCount() > 0;
    }
    
    
    public void removeSampleData() {
        Log.d("Sample data will be removed, read the queries from " + CLEAN_SAMPLE_SQL_FILES[1]);
        
        String sqlQueries = getDatabaseHelper().readSqlFile(CLEAN_SAMPLE_SQL_FILES[1]);
        
        if (sqlQueries != null) {
            Log.d("Loaded clean file, cleaning them from the database...");
            DatabaseOpenHelper.executeMultipleQueries(getDatabaseHelper().getWritableDatabase(), sqlQueries);
            Log.d("Cleaned samples from database (with, or without errors, see above).");
        }
    }
}
