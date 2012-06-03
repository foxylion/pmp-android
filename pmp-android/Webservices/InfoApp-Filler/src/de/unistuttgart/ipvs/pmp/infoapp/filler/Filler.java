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

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public abstract class Filler {
    
    protected Service service;
    private long fromMillis;
    private long toMillis;
    
    
    /**
     * Prepares the filler
     * 
     * @param service
     *            Service used for communicating with the webservices
     * @param fromMillis
     *            The timestamp of the event generated first
     * @param toMillis
     *            The timestamp of the event generated last
     */
    public Filler(Service service, long fromMillis, long toMillis) {
        this.service = service;
        this.fromMillis = fromMillis;
        this.toMillis = toMillis;
    }
    
    
    /**
     * Generates all events and uploads them to the webservice
     */
    public void fill() {
        System.out.print("Generating... ");
        List<Event> events = new LinkedList<Event>();
        
        long timeMillis = this.fromMillis;
        int spreading = maxSpreading() * 60000;
        
        while (timeMillis < this.toMillis) {
            events.add(generateEvent(timeMillis));
            
            // Increment date/time randomly with a value between 0 ms and 100 minutes
            int inc = (int) (Math.random() * spreading);
            timeMillis += inc;
        }
        
        System.out.print("Uploading (" + events.size() + " Events)... ");
        uploadEvents(events);
        System.out.println("Done!");
        
    }
    
    
    /**
     * Maximum time between two events.
     * 
     * @return The time between two events is computed randomly between 0 and the returned value in
     *         minutes
     */
    protected abstract int maxSpreading();
    
    
    /**
     * Generates a single event of the proper event-type
     * 
     * @param time
     *            The events timestamp
     * @return The generated event
     */
    public abstract Event generateEvent(long time);
    
    
    /**
     * Uploads the generated evetns to the proper webservice
     * 
     * @param events
     *            The generated events
     */
    public abstract void uploadEvents(List<Event> events);
    
}
