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
package de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventOrderException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

/**
 * Gives access to bluetooth or WiFi events
 * 
 * @author Patrick Strobel
 * 
 */
public class CellularConnectionEventManager extends EventManager {
    
    /**
     * Creates a new manager for cellular connection events
     * 
     * @param service
     *            Helper class used for communication with the webservice
     */
    public CellularConnectionEventManager(Service service) {
        super(service);
    }
    
    
    @Override
    public void commitEvents(List<? extends Event> events) throws InternalDatabaseException, InvalidParameterException,
            InvalidEventOrderException, IOException {
        commitEvents(events, "upload_connection_events_cellular.php");
    }
    
    
    @Override
    public EventProperty getLastEventProperty() throws InternalDatabaseException, InvalidParameterException,
            IOException {
        return getLastEventProperty("last_connection_event_cellular.php");
    }
    
}
