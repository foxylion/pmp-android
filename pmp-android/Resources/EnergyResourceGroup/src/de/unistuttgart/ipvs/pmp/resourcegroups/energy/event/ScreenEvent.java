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
 * This is a concrete implementation of the {@link AbstractEvent}, the screen event
 * 
 * @author Marcus Vetter
 * 
 */
public class ScreenEvent extends AbstractEvent {
    
    /**
     * Attribute of the screen event
     */
    private boolean changedTo;
    
    
    /**
     * Constructor
     */
    public ScreenEvent(int id, long timestamp, boolean changedTo) {
        super();
        this.id = id;
        this.timestamp = timestamp;
        this.changedTo = changedTo;
    }
    
    
    /**
     * Empty constructor
     */
    public ScreenEvent() {
    }
    
    
    /**
     * @return the changedTo
     */
    public boolean isChangedTo() {
        return this.changedTo;
    }
    
    
    /**
     * @param changedTo
     *            the changedTo to set
     */
    public void setChangedTo(boolean changedTo) {
        this.changedTo = changedTo;
    }
    
}
