package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAssignedApp implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6897921426190796025L;
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
