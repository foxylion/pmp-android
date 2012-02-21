/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
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

public class HistoryPersonObject {
    
    int userid;
    String username;
    float rating;
    int rating_num;
    boolean rated;
    
    
    public HistoryPersonObject(int userid, String username, float rating, int rating_num, boolean rated) {
        super();
        this.userid = userid;
        this.username = username;
        this.rating = rating;
        this.rating_num = rating_num;
        this.rated = rated;
    }
    
    
    public int getUserid() {
        return this.userid;
    }
    
    
    public String getUsername() {
        return this.username;
    }
    
    
    public float getRating() {
        return this.rating;
    }
    
    
    public int getRating_num() {
        return this.rating_num;
    }
    
    
    public boolean isRated() {
        return this.rated;
    }
    
}
