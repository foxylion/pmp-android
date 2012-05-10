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
 * Abstract base class for all events that might be sent to a webservices.
 * 
 * @author Patrick Strobel
 */
public abstract class Event {
    
    private int id;
    private long timestamp;
    
    
    public Event(long timestamp) {
        this.timestamp = timestamp;
    }
    
    
    public Event(int id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }
    
    
    /**
     * Returns the event's ID
     * 
     * @return The ID
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Returns the time this event occurred
     * 
     * @return Timestamp in ms accuracy
     */
    public long getTimestamp() {
        return this.timestamp;
    }
    
    
    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("timestamp", this.timestamp);
        return json;
    }
}
