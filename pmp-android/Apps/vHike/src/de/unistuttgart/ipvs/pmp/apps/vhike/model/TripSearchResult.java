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

import java.util.Calendar;

public class TripSearchResult {
    
    public long tripId;
    public String departure;
    public String destination;
    public Calendar date;
    public String stopovers;
    public String username;
    public long userId;
    public float rating;
    public int seat;
    
    
    /**
     * Create a search result for a trip
     * 
     * @param tripId
     * @param departure
     * @param destination
     * @param date
     * @param stopovers
     * @param username
     * @param userId
     * @param rating
     * @param availableSeat
     */
    public TripSearchResult(long tripId, String departure, String destination, Calendar date,
            String stopovers, String username, long userId, float rating, int availableSeat) {
        this.tripId = tripId;
        this.departure = departure;
        this.destination = destination;
        this.date = date;
        this.stopovers = stopovers;
        this.username = username;
        this.userId = userId;
        this.rating = rating;
        this.seat = availableSeat;
    }
    
    
    /**
     * Create a search result for a query
     * 
     * @param tripId
     * @param departure
     * @param destination
     * @param date
     * @param username
     * @param userId
     * @param rating
     * @param neededSeat
     */
    public TripSearchResult(long tripId, String departure, String destination, Calendar date,
            String username,
            long userId, float rating, int neededSeat) {
        this.tripId = tripId;
        this.departure = departure;
        this.destination = destination;
        this.date = date;
        this.username = username;
        this.userId = userId;
        this.rating = rating;
        this.seat = neededSeat;
    }
}
