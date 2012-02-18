package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

public interface IIdentifierIS extends IIssueLocation {
    
    /**
     * Get the identifier
     * 
     * @return identifier
     */
    public abstract String getIdentifier();
    
    
    /**
     * Set the identifier
     * 
     * @param identifier
     *            identifier to set
     */
    public abstract void setIdentifier(String identifier);
    
}
