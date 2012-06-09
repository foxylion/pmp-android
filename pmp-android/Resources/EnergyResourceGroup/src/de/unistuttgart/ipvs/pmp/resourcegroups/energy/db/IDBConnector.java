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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.DeviceBootEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.ScreenEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetCurrentValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetLastBootValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetTotalValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.webserver.ResultSetUpload;

/**
 * This is the interface for an database connection for storing and loading energy and device data events
 * 
 * @author Marcus Vetter
 * 
 */
public interface IDBConnector {
    
    /**
     * Store a {@link BatteryEvent}
     * 
     * @param be
     *            the {@link BatteryEvent}
     */
    public void storeBatteryEvent(BatteryEvent be);
    
    
    /**
     * Store the {@link ScreenEvent}
     * 
     * @param se
     *            the {@link ScreenEvent}
     */
    public void storeScreenEvent(ScreenEvent se);
    
    
    /**
     * Store the {@link DeviceBootEvent}
     * 
     * @param dbe
     *            the {@link DeviceBootEvent}
     */
    public void storeDeviceBootEvent(DeviceBootEvent dbe);
    
    
    /**
     * Get the current values
     * 
     * @return a object of {@link ResultSetCurrentValues}
     */
    public ResultSetCurrentValues getCurrentValues();
    
    
    /**
     * Get the values since last boot
     * 
     * @return a object of {@link ResultSetLastBootValues}
     */
    public ResultSetLastBootValues getLastBootValues();
    
    
    /**
     * Get the total values
     * 
     * @return a object of {@link ResultSetTotalValues}
     */
    public ResultSetTotalValues getTotalValues();
    
    
    /**
     * Get the values for an upload
     * 
     * @return a object of {@link ResultSetUpload}
     */
    public ResultSetUpload getUploadValues();
    
    
    /**
     * This will clear the database
     */
    public void clearDatabase();
    
}
