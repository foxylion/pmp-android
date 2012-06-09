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
package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class PrePlannedTrip {
    
    int tid;
    String destination;
    String date;
    int passengers;
    int invites;
    
    
    public PrePlannedTrip(int tid, String destination, String date, int passengers, int invites) {
        this.tid = tid;
        this.destination = destination;
        this.date = date;
        this.passengers = passengers;
        this.invites = invites;
    }
    
    
    public int getTid() {
        return this.tid;
    }
    
    
    public String getDestination() {
        return this.destination;
    }
    
    
    public String getDate() {
        // TODO: Time parsen zu Date
        return this.date;
    }
    
    
    public int getPassengers() {
        return this.passengers;
    }
    
    
    public int getInvites() {
        return this.invites;
    }
}
