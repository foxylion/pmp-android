/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
package de.unistuttgart.ipvs.pmp.apps.vhike.model;

import java.util.ArrayList;
import java.util.Calendar;

public class TripOverview {
    
    public int id;
    public String destination;
    public String stopovers;
    public Calendar startTime;
    public int numberOfPassengers;
    public int numberOfOffers;
    public int numberOfNewMessages;
    public int numberOfAvailableSeat;
    public ArrayList<CompactUser> passengers;
    public ArrayList<CompactMessage> messages;
    
    
    public TripOverview(int id, String destination, String stopovers, ArrayList<CompactUser> passengers,
            long startTime, int numberOfAvailableSeat, ArrayList<CompactMessage> newMessages) {
        
        if (id < 0) {
            throw new IllegalArgumentException("Invalid Trip ID");
        }
        this.id = id;
        this.destination = destination;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startTime);
        this.startTime = c;
        this.messages = newMessages;
        this.passengers = passengers;
        this.numberOfAvailableSeat = numberOfAvailableSeat;
        this.stopovers = stopovers.replaceAll("(^;|;$)", "").replace(";", ", ").replaceAll("\\s+", " ");
    }
}
