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
 * A cellular connection event stores information about the state of the cellular connection
 * at a given timestamp
 * 
 * @author Patrick Strobel
 */
public class CellularConnectionEvent extends Event {
    
    private boolean roaming;
    private boolean airplane;
    
    
    /**
     * Creates a new battery event
     * 
     * @param timestamp
     *            Point in time when this event occurred
     * @param roaming
     *            Indicates if the device was in roaming mode when this event occurred
     * @param airplane
     *            Indicates if the device was in airplane mode when this event occurred
     */
    public CellularConnectionEvent(long timestamp, boolean roaming, boolean airplane) {
        super(timestamp);
        this.roaming = roaming;
        this.airplane = airplane;
    }
    
    
    /**
     * Creates a new battery event
     * 
     * @param id
     *            The event's ID
     * @param timestamp
     *            Point in time when this event occurred
     * @param roaming
     *            Indicates if the device was in roaming mode when this event occurred
     * @param airplane
     *            Indicates if the device was in airplane mode when this event occurred
     */
    public CellularConnectionEvent(int id, long timestamp, boolean roaming, boolean airplane) {
        super(id, timestamp);
        this.roaming = roaming;
        this.airplane = airplane;
    }
    
    
    /**
     * Gets the roaming status
     * 
     * @return True, if roaming is active
     */
    public boolean isRoaming() {
        return this.roaming;
    }
    
    
    /**
     * Gets the airplane mode status
     * 
     * @return True, if airplane mode is enabled
     */
    public boolean isAirplane() {
        return this.airplane;
    }
    
    
    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = super.toJSONObject();
        json.put("roaming", this.roaming);
        json.put("airplane", this.airplane);
        return json;
    }
    
}
