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
 * A profile event stores information about the state of the user's profile
 * at a given timestamp
 * 
 * @author Patrick Strobel
 * 
 */
public class ProfileEvent extends Event {
    
    public enum Events {
        CALL,
        SMS
    };
    
    public enum Directions {
        INCOMING,
        OUTGOING
    };
    
    private Events event;
    private Directions direction;
    private String city;
    
    
    /**
     * Creates a new profile event
     * 
     * @param timestamp
     *            Point in time when this event occurred
     * @param event
     *            Medium that has been occurred
     * @param direction
     *            Direction, in which the event occurred
     * @param city
     *            City at which this event occurred
     */
    public ProfileEvent(long timestamp, Events event, Directions direction, String city) {
        super(timestamp);
        
        this.event = event;
        this.direction = direction;
        this.city = city;
    }
    
    
    /**
     * Creates a new profile event
     * 
     * @param id
     *            The event's ID
     * @param timestamp
     *            Point in time when this event occurred
     * @param event
     *            Medium that has been occurred
     * @param direction
     *            Direction, in which the event occurred
     * @param city
     *            City at which this event occurred
     */
    public ProfileEvent(int id, long timestamp, Events event, Directions direction, String city) {
        super(id, timestamp);
        
        this.event = event;
        this.direction = direction;
        this.city = city;
    }
    
    
    /**
     * Direction, in which the event occurred
     * 
     * @return Event direction
     */
    public Directions getDirection() {
        return this.direction;
    }
    
    
    /**
     * Event that has been occurred
     * 
     * @return Event type
     */
    public Events getEvent() {
        return this.event;
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
        
        switch (this.event) {
            case CALL:
                json.put("event", "c");
                break;
            
            case SMS:
                json.put("event", "s");
                break;
        }
        
        switch (this.direction) {
            case INCOMING:
                json.put("direction", "i");
                break;
            
            case OUTGOING:
                json.put("direction", "o");
                break;
        }
        
        json.put("city", this.city);
        return json;
    }
}
