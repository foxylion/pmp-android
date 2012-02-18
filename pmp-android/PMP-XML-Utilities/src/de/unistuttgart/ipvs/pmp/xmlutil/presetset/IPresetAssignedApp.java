package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

public interface IPresetAssignedApp extends IIssueLocation {
    
    /**
     * Get the identifier
     * 
     * @return the identifier
     */
    public abstract String getIdentifier();
    
    
    /**
     * Set the identifier
     * 
     * @param identifier
     *            the identifier to set
     */
    public abstract void setIdentifier(String identifier);
    
}
