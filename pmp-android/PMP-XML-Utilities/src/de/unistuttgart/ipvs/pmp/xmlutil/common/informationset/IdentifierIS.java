package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

public abstract class IdentifierIS extends IssueLocation implements IIdentifierIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 7193039662231041710L;
    
    /**
     * The identifier
     */
    private String identifier = "";
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IIdentifierIS#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IIdentifierIS#setIdentifier(java.lang.String)
     */
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
