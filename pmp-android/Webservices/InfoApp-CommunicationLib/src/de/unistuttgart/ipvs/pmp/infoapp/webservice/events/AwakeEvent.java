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
 * An awake event stores information about the state of the device
 * at a given timestamp
 * 
 * @author Patrick Strobel
 */
public class AwakeEvent extends Event {
    
    private boolean awake;
    
    
    /**
     * Creates a new awake event
     * 
     * @param id
     *            The event's ID
     * @param timestamp
     *            Point in time when this event occurred
     * @param awake
     *            Indicates if the device is active (true) or in standby (false)
     */
    public AwakeEvent(int id, long timestamp, boolean awake) {
        super(id, timestamp);
        this.awake = awake;
    }
    
    
    /**
     * Gets the device's awake status
     * 
     * @return True, if device is active
     */
    public boolean isAwake() {
        return this.awake;
    }
    
    
    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = super.toJSONObject();
        json.put("awake", this.awake);
        return json;
    }
    
}
