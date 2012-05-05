package de.unistuttgart.ipvs.pmp.resourcegroups.energy.event;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class DeviceBootEvent extends AbstractEvent {
    
    /**
     * Constructor
     */
    public DeviceBootEvent(int id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }
    
}
