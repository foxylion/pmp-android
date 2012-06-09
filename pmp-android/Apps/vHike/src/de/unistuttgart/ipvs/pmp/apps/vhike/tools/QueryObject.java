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

/**
 * Query Objects will be generated when there is a call of searchQuery {@link JSonRequestReader}
 * 
 * @author Alexander Wassiljew
 * 
 */
public class QueryObject {
    
    int queryid = 0;
    int userid = 0;
    String username = "";
    float cur_lat = 0;
    float cur_lon = 0;
    int seats = 0;
    float distance = 0;
    
    
    public QueryObject(int queryid, int userid, String username, float cur_lat, float cur_lon, int seats,
            float distance) {
        this.queryid = queryid;
        this.userid = userid;
        this.username = username;
        this.cur_lat = cur_lat;
        this.cur_lon = cur_lon;
        this.seats = seats;
        this.distance = distance;
    }
    
    
    public int getQueryid() {
        return this.queryid;
    }
    
    
    public int getUserid() {
        return this.userid;
    }
    
    
    public String getUsername() {
        return this.username;
    }
    
    
    public float getCur_lat() {
        return this.cur_lat;
    }
    
    
    public float getCur_lon() {
        return this.cur_lon;
    }
    
    
    public int getSeats() {
        return this.seats;
    }
    
    
    public float getDistance() {
        return this.distance;
    }
    
}
