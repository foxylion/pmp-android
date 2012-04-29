package de.unistuttgart.ipvs.pmp.infoapp.webservice.events;

/**
 * An awake event stores information about the state of the device
 * at a given timestamp
 * 
 * @author Patrick Strobel
 */
public class AwakeEvent extends Event {
    
    private boolean awake;
    
    
    /**
     * Creates a new awake event
     * 
     * @param id
     *            The event's ID
     * @param timestamp
     *            Point in time when this event occurred
     * @param awake
     *            Indicates if the device is active (true) or in standby (false)
     */
    public AwakeEvent(int id, long timestamp, boolean awake) {
        super(id, timestamp);
        this.awake = awake;
    }
    
    
    /**
     * Gets the device's awake status
     * 
     * @return True, if device is active
     */
    public boolean isAwake() {
        return this.awake;
    }
    
}
