package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAssignedApp extends IssueLocation implements Serializable, IPresetAssignedApp {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6897921426190796025L;
    /**
     * The identifier
     */
    private String identifier = "";
    
    
    /**
     * Constructor to set the identifier
     * 
     * @param identifier
     *            identifier of the assigned app
     */
    public PresetAssignedApp(String identifier) {
        this.identifier = identifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedApp#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedApp#setIdentifier(java.lang.String)
     */
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
