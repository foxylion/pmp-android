package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAssignedApp {
    
    /**
     * The identifier
     */
    private String identifier;
    
    
    /**
     * Constructor to set the identifier
     * 
     * @param identifier
     *            identifier of the assigned app
     */
    public PresetAssignedApp(String identifier) {
        this.identifier = identifier;
    }
    
    
    /**
     * Get the identifier
     * 
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }
    
    
    /**
     * Set the identifier
     * 
     * @param identifier
     *            the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
