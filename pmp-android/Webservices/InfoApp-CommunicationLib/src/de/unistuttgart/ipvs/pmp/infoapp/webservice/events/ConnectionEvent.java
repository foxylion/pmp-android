/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-CommunicationLib
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
package de.unistuttgart.ipvs.pmp.infoapp.webservice.events;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A connection event stores information about the state of the connection
 * at a given timestamp
 * 
 * @author Patrick Strobel
 * 
 */
public class ConnectionEvent extends Event {
    
    public enum Mediums {
        BLUETOOTH,
        WIFI
    };
    
    private Mediums medium;
    private boolean connected;
    private boolean enabled;
    private String city;
    
    
    /**
     * Creates a new connection event
     * 
     * @param id
     *            The event's ID
     * @param timestamp
     *            Point in time when this event occurred
     * @param medium
     *            Medium that has been used for communication
     * @param connected
     *            Indicates if the device was connected when this even occurred
     * @param enabled
     *            Indicates if the communication adapter was turned on when this event occurred
     * @param city
     *            City at which this event occurred
     */
    public ConnectionEvent(int id, long timestamp, Mediums medium, boolean connected, boolean enabled, String city) {
        super(id, timestamp);
        
        this.medium = medium;
        this.connected = connected;
        this.enabled = enabled;
        this.city = city;
    }
    
    
    /**
     * Gets the medium that has been used for the communication
     * 
     * @return The medium
     */
    public Mediums getMedium() {
        return this.medium;
    }
    
    
    /**
     * Gets the connection's status
     * 
     * @return True, if device was connected when this event occurred
     */
    public boolean isConnected() {
        return this.connected;
    }
    
    
    /**
     * Gets the adapter's status
     * 
     * @return True, if the communication adapter was turned on when this event occurred
     */
    public boolean isEnabled() {
        return this.enabled;
    }
    
    
    /**
     * Get the city, at which the device has been connected
     * 
     * @return The city's name
     */
    public String getCity() {
        return this.city;
    }
    
    
    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = super.toJSONObject();
        
        switch (this.medium) {
            case BLUETOOTH:
                json.put("medium", "b");
                break;
            
            case WIFI:
                json.put("medium", "w");
                break;
        }
        
        json.put("connected", this.connected);
        json.put("enabled", this.enabled);
        json.put("city", this.city);
        return json;
    }
}
