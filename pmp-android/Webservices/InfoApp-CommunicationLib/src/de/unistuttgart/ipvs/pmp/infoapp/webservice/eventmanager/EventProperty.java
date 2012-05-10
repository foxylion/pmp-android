package de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager;

/**
 * This class stores general information about an event
 * 
 * @author Patrick Strobel
 * 
 */
public class EventProperty {
    
    private final int id;
    private final long timestamp;
    
    
    public EventProperty(int id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }
    
    
    /**
     * Gets the event's ID
     * 
     * @return The ID
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Get the event's Timestamp
     * 
     * @return The timestamp
     */
    public long getTimestamp() {
        return this.timestamp;
    }
    
}
