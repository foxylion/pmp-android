/*
 * Copyright 2012 pmp-android development team
 * Project: EnergyResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.event;

/**
 * This abstract event contains only an identifier (id) and a timestamp
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractEvent {
    
    protected int id;
    protected long timestamp;
    
    
    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    
    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return this.timestamp;
    }
    
    
    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
}
