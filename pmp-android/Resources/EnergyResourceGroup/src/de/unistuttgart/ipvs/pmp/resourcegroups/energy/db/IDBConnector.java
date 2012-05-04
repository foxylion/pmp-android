package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.DeviceBootEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.ScreenEvent;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IDBConnector {
    
    /**
     * Store a battery event
     * 
     * @param be
     *            the battery event
     */
    public void storeBatteryEvent(BatteryEvent be);
    
    
    /**
     * Store the screen event
     * 
     * @param se
     *            the screen event
     */
    public void storeScreenEvent(ScreenEvent se);
    
    
    /**
     * Store the device boot event
     * 
     * @param dbe
     *            the device boot event
     */
    public void storeDeviceBootEvent(DeviceBootEvent dbe);
    
}
