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
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.CellularConnectionEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.CellularConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public class CellularConnection extends Filler {
    
    public CellularConnection(Service service, long fromMillis, long toMillis) {
        super(service, fromMillis, toMillis);
    }
    
    
    @Override
    protected int maxSpreading() {
        return 24 * 60;
    }
    
    
    @Override
    public Event generateEvent(long time) {
        
        boolean airplane = false;
        
        if (Math.random() < 0.2) {
            airplane = true;
        }
        CellularConnectionEvent event = new CellularConnectionEvent(++this.id, time, airplane);
        return event;
    }
    
    
    @Override
    public void uploadEvents(List<Event> events) {
        CellularConnectionEventManager ccem = new CellularConnectionEventManager(this.service);
        
        try {
            ccem.commitEvents(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
