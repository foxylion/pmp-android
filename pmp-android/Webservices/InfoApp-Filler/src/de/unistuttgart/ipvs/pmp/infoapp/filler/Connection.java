/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-Filler
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
package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.ConnectionEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

/**
 * Generates connection-events and commits them to the upload_connection_events-webservice.
 * 
 * @author Patrick Strobel
 */
public class Connection extends Filler {
    
    public Connection(Service service, long fromMillis, long toMillis) {
        super(service, fromMillis, toMillis);
    }
    
    private String[] cities = { "Stuttgart", "Vaihingen", "Böblingen", "Esslingen" };
    
    
    @Override
    protected int maxSpreading() {
        return 120;
    }
    
    
    @Override
    public Event generateEvent(long time) {
        ConnectionEvent.Mediums medium = ConnectionEvent.Mediums.BLUETOOTH;
        
        if (Math.random() < 0.5) {
            medium = ConnectionEvent.Mediums.WIFI;
        }
        
        boolean connected = false;
        boolean enabled = false;
        
        if (Math.random() < 0.7) {
            enabled = true;
            if (Math.random() < 0.5) {
                connected = true;
            }
        }
        
        int cityNum = (int) (Math.random() * this.cities.length);
        
        ConnectionEvent event = new ConnectionEvent(time, medium, connected, enabled, this.cities[cityNum]);
        return event;
    }
    
    
    @Override
    public void uploadEvents(List<Event> events) {
        ConnectionEventManager cem = new ConnectionEventManager(this.service);
        
        try {
            cem.commitEvents(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
