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
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventIdException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventOrderException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

/**
 * Gives access to battery events
 * 
 * @author Patrick Strobel
 */
public class BatteryEventManager extends EventManager {
    
    /**
     * Creates a new manager for battery events
     * 
     * @param service
     *            Helper class used for communication with the webservice
     */
    public BatteryEventManager(Service service) {
        super(service);
    }
    
    
    @Override
    public void commitEvents(List<? extends Event> events) throws InternalDatabaseException, InvalidParameterException,
            InvalidEventIdException, InvalidEventOrderException, IOException {
        commitEvents(events, "upload_battery_events.php");
    }
    
    
    @Override
    public int getLastId() throws InternalDatabaseException, InvalidParameterException, IOException {
        return getLastId("last_battery_event.php");
    }
    
}
