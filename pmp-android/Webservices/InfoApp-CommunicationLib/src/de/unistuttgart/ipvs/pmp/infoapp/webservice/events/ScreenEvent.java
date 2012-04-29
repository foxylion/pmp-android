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

/**
 * A screen event stores information about the state of the device's display
 * at a given timestamp
 * 
 * @author Patrick Strobel
 */
public class ScreenEvent extends Event {
    
    private boolean display;
    
    
    /**
     * Creates a new screen event
     * 
     * @param id
     *            The event's ID
     * @param timestamp
     *            Point in time when this event occurred
     * @param display
     *            Indicates if the device's display is turned on (true) or turned off (false)
     */
    public ScreenEvent(int id, long timestamp, boolean display) {
        super(id, timestamp);
        this.display = display;
    }
    
    
    /**
     * Get the device's display status
     * 
     * @return True, if display is on
     */
    public boolean isDisplayOn() {
        return this.display;
    }
    
}
