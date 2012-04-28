package de.unistuttgart.ipvs.pmp.infoapp.webservice.events;

/**
 * A cellular connection event stores information about the state of the cellular connection
 * at a given timestamp
 * 
 * @author Patrick Strobel
 */
public class CellularConnectionEvent extends Event {
    
    private boolean roaming;
    private boolean airplane;
    
    
    /**
     * Creates a new battery event
     * 
     * @param id
     *            The event's ID
     * @param timestamp
     *            Point in time when this event occurred
     * @param roaming
     *            Indicates if the device was in roaming mode when this event occurred
     * @param airplane
     *            Indicates if the device was in airplane mode when this event occurred
     */
    public CellularConnectionEvent(int id, long timestamp, boolean roaming, boolean airplane) {
        super(id, timestamp);
        this.roaming = roaming;
        this.airplane = airplane;
    }
    
    
    /**
     * Gets the roaming status
     * 
     * @return True, if roaming is active
     */
    public boolean isRoaming() {
        return this.roaming;
    }
    
    
    /**
     * Gets the airplane mode status
     * 
     * @return True, if airplane mode is enabled
     */
    public boolean isAirplane() {
        return this.airplane;
    }
    
}
