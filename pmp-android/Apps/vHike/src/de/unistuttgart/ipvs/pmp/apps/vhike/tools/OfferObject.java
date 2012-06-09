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

public class OfferObject {
    
    int offer_id;
    int user_id;
    String username;
    float rating;
    float rating_num;
    float lat;
    float lon;
    
    
    public OfferObject(int offer_id, int user_id, String username, float rating, float rating_num, float lat,
            float lon) {
        super();
        this.offer_id = offer_id;
        this.user_id = user_id;
        this.username = username;
        this.rating = rating;
        this.rating_num = rating_num;
        this.lat = lat;
        this.lon = lon;
    }
    
    
    public float getLat() {
        return this.lat;
    }
    
    
    public float getLon() {
        return this.lon;
    }
    
    
    public int getOffer_id() {
        return this.offer_id;
    }
    
    
    public int getUser_id() {
        return this.user_id;
    }
    
    
    public String getUsername() {
        return this.username;
    }
    
    
    public float getRating() {
        return this.rating;
    }
    
    
    public float getRating_num() {
        return this.rating_num;
    }
    
}
