package de.unistuttgart.ipvs.pmp.resourcegroups.energy.webserver;

import java.util.List;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;

/**
 * The result set of the upload
 * 
 * @author Marcus Vetter
 * 
 */
public class ResultSetUpload {
    
    private List<BatteryEvent> batteryEvents;
    
    
    /**
     * @return the batteryEvents
     */
    public List<BatteryEvent> getBatteryEvents() {
        return this.batteryEvents;
    }
    
    
    /**
     * @param batteryEvents
     *            the batteryEvents to set
     */
    public void setBatteryEvents(List<BatteryEvent> batteryEvents) {
        this.batteryEvents = batteryEvents;
    }
    
    
    /**
     * Add a {@link BatteryEvent}
     * 
     * @param be
     *            Battery Event
     */
    public void addBatteryEvent(BatteryEvent be) {
        this.batteryEvents.add(be);
    }
    
}
