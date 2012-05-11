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

import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.CellularConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;

/**
 * Interface for the database functions
 * 
 * @author Thorsten Berberich
 * 
 */
public interface IDBConnector {
    
    /**
     * Store a wifi event
     * 
     * @param timestamp
     *            timestamp of the event in milliseconds since 1970
     * @param EventEnum
     *            event, ON or OFF
     * @param city
     *            city where the event happened
     */
    public void storeWifiEvent(long timestamp, EventEnum event, String city);
    
    
    /**
     * Store a wifi event, use this only if you want to set a new highest id
     * 
     * @param id
     *            id of the entry
     * @param timestamp
     *            timestamp of the event in milliseconds since 1970
     * @param EventEnum
     *            event, ON or OFF
     * @param city
     *            city where the event happened
     */
    public void storeWifiEvent(int id, long timestamp, EventEnum event, String city);
    
    
    /**
     * Store a bluetooth event
     * 
     * @param timestamp
     *            timestamp of the event in milliseconds since 1970
     * @param EventEnum
     *            event, ON or OFF
     * @param city
     *            city where the event happened
     */
    public void storeBTEvent(long timestamp, EventEnum event, String city);
    
    
    /**
     * Store a bluetooth event, use this only if you want to set a new highest id
     * 
     * @param id
     *            id of the entry
     * @param timestamp
     *            timestamp of the event in milliseconds since 1970
     * @param EventEnum
     *            event, ON or OFF
     * @param city
     *            city where the event happened
     */
    public void storeBTEvent(int id, long timestamp, EventEnum event, String city);
    
    
    /**
     * Store a cell phone event
     * 
     * @param timestamp
     *            timestamp of the event in milliseconds since 1970
     * @param EventEnum
     *            event, ON or OFF
     */
    public void storeCellPhoneEvent(long timestamp, EventEnum event);
    
    
    /**
     * Store a cell phone event, use this only if you want to set a new highest id
     * 
     * @param id
     *            id of the entry
     * @param timestamp
     *            timestamp of the event in milliseconds since 1970
     * @param EventEnum
     *            event, ON or OFF
     */
    public void storeCellPhoneEvent(int id, long timestamp, EventEnum event);
    
    
    /**
     * Calculate the time duration between all on and off events in the given table name for every event with a id that
     * is greater than the given
     * 
     * @param tableName
     *            table name
     * @param duration
     *            how long you want to search back
     * @param id
     *            only entries with a greater id than this will be included </br> insert <b>0</b> if you <b>don't</b>
     *            want to check a id
     */
    public long getTimeDuration(String tableName, long duration, int id);
    
    
    /**
     * Get all cities where the user was connected
     * 
     * @param tableName
     *            Name of the table, see {@link DBConstants}
     * @return List of strings with the cities and the times where the user connected, e.g. :"10x Stuttgart"
     */
    public List<String> getConnectedCities(String tableName);
    
    
    /**
     * Get all wifi events
     * 
     * @return List with {@link ConnectionEvent}s
     */
    public List<ConnectionEvent> getWifiEvents();
    
    
    /**
     * Get all bluetooth events
     * 
     * @return List with {@link ConnectionEvent}s
     */
    public List<ConnectionEvent> getBluetoothEvents();
    
    
    /**
     * Get all cellular phone network events
     * 
     * @return List with {@link CellularConnectionEvent}s
     */
    public List<CellularConnectionEvent> getCellEvents();
    
}
