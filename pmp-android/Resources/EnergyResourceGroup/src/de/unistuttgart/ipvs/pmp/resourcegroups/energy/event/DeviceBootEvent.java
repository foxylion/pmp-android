package de.unistuttgart.ipvs.pmp.resourcegroups.energy.event;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class DeviceBootEvent extends AbstractEvent {
    
    private boolean changedTo;
    
    
    /**
     * Constructor
     */
    public DeviceBootEvent(int id, long timestamp, boolean changedTo) {
        this.id = id;
        this.timestamp = timestamp;
        this.changedTo = changedTo;
    }
    
    
    /**
     * Constructor without attributes
     */
    public DeviceBootEvent() {
    }
    
    
    /**
     * @return the changedTo
     */
    public boolean isChangedTo() {
        return changedTo;
    }
    
    
    /**
     * @param changedTo
     *            the changedTo to set
     */
    public void setChangedTo(boolean changedTo) {
        this.changedTo = changedTo;
    }
    
}
