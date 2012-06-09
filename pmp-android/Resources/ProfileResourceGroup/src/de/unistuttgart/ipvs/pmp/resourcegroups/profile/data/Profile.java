/*
 * Copyright 2012 pmp-android development team
 * Project: ProfileResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.profile.data;

public class Profile {
    
    public int ring;
    public int apps;
    public int contacts;
    
    
    public int getRing() {
        return this.ring;
    }
    
    
    public void setRing(int ring) {
        this.ring = ring;
    }
    
    
    public int getApps() {
        return this.apps;
    }
    
    
    public void setApps(int apps) {
        this.apps = apps;
    }
    
    
    public int getContacts() {
        return this.contacts;
    }
    
    
    public void setContacts(int contacts) {
        this.contacts = contacts;
    }
}
