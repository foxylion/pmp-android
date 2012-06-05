/*
 * Copyright 2012 pmp-android development team
 * Project: ConnectionResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.resource;

import java.util.Comparator;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;

/**
 * Orders {@link ConnectionEvent}s ascending according to the timestamp
 * 
 * @author Thorsten Berberich
 * 
 */
public class ConnectionEventComparator implements Comparator<ConnectionEvent> {
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(ConnectionEvent event1, ConnectionEvent event2) {
        return (int) (event1.getTimestamp() - event2.getTimestamp());
    }
    
}
