package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.DeviceBootEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.ScreenEvent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetCurrentValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetLastBootValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetTotalValues;

/**
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
    
}
