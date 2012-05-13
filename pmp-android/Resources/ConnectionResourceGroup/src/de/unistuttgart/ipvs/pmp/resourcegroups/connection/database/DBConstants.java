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

/**
 * Constants that are use for the database
 * 
 * @author Thorsten Berberich
 * 
 */
public class DBConstants {
    
    /**
     * Database name and version
     */
    public static final String DATABASE_NAME = "connectiondb";
    public static final int DATABASE_VERSION = 3;
    
    /**
     * Table names
     */
    public static final String TABLE_WIFI = "wifi";
    public static final String TABLE_BT = "bluetooth";
    public static final String TABLE_CELL = "cellphone";
    
    /**
     * Column names
     */
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_EVENT = "event";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    
    /**
     * Devices
     */
    public static final String DEVICE_WIFI = "wifi";
    public static final String DEVICE_BT = "bluetooth";
}
