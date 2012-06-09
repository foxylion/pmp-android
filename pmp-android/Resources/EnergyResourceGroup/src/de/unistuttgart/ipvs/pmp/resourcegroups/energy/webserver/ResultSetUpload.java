/*
 * Copyright 2012 pmp-android development team
 * Project: EnergyResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.webserver;

import java.util.List;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;

/**
 * The result set of the upload
 * 
 * @author Marcus Vetter
 * 
 */
public class ResultSetUpload {
    
    private List<BatteryEvent> batteryEvents;
    
    
    /**
     * @return the batteryEvents
     */
    public List<BatteryEvent> getBatteryEvents() {
        return this.batteryEvents;
    }
    
    
    /**
     * @param batteryEvents
     *            the batteryEvents to set
     */
    public void setBatteryEvents(List<BatteryEvent> batteryEvents) {
        this.batteryEvents = batteryEvents;
    }
    
    
    /**
     * Add a {@link BatteryEvent}
     * 
     * @param be
     *            Battery Event
     */
    public void addBatteryEvent(BatteryEvent be) {
        this.batteryEvents.add(be);
    }
    
}
