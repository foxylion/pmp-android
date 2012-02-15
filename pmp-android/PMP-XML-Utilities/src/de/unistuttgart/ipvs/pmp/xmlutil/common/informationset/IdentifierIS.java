package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

public abstract class IdentifierIS extends IssueLocation {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 7193039662231041710L;
    
    /**
     * The identifier
     */
    private String identifier = "";
    
    
    /**
     * Get the identifier
     * 
     * @return identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /**
     * Set the identifier
     * 
     * @param identifier
     *            identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
