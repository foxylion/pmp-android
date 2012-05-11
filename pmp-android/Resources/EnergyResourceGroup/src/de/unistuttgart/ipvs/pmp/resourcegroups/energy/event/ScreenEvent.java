package de.unistuttgart.ipvs.pmp.resourcegroups.energy.event;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class ScreenEvent extends AbstractEvent {
    
    /**
     * Attribute of the screen event
     */
    private boolean changedTo;
    
    
    /**
     * Constructor
     */
    public ScreenEvent(int id, long timestamp, boolean changedTo) {
        super();
        this.id = id;
        this.timestamp = timestamp;
        this.changedTo = changedTo;
    }
    
    
    /**
     * Empty constructor
     */
    public ScreenEvent() {
    }
    
    
    /**
     * @return the changedTo
     */
    public boolean isChangedTo() {
        return this.changedTo;
    }
    
    
    /**
     * @param changedTo
     *            the changedTo to set
     */
    public void setChangedTo(boolean changedTo) {
        this.changedTo = changedTo;
    }
    
}
