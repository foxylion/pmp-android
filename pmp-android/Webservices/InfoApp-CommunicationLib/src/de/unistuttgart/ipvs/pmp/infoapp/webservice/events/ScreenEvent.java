package de.unistuttgart.ipvs.pmp.infoapp.webservice.events;

/**
 * A screen event stores information about the state of the device's display
 * at a given timestamp
 * 
 * @author Patrick Strobel
 */
public class ScreenEvent extends Event {
    
    private boolean display;
    
    
    /**
     * Creates a new screen event
     * 
     * @param id
     *            The event's ID
     * @param timestamp
     *            Point in time when this event occurred
     * @param display
     *            Indicates if the device's display is turned on (true) or turned off (false)
     */
    public ScreenEvent(int id, long timestamp, boolean display) {
        super(id, timestamp);
        this.display = display;
    }
    
    
    /**
     * Get the device's display status
     * 
     * @return True, if display is on
     */
    public boolean isDisplayOn() {
        return this.display;
    }
    
}
