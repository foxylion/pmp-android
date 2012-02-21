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


public class PassengerObject {
 
    
    int user_id;
    
    boolean picked_up;
    
    public PassengerObject(int user_id, boolean picked) {
        super();
        this.user_id = user_id;
        this.picked_up = picked;
    }
    
    public int getUser_id() {
        return user_id;
    }
    
    public boolean isPicked_up() {
        return picked_up;
    }
    
    
}
