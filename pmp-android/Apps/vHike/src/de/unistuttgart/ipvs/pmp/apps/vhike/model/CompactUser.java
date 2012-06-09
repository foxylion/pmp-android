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

public class CompactUser {
    
    public int id;
    public String name;
    public float rating;
    
    
    public CompactUser(int id, String name, int rating) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        this.id = id;
        this.name = name;
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        this.rating = rating;
    }
}
