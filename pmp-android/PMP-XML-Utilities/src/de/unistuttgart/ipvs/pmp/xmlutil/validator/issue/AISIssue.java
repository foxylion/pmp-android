package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

/**
 * Instantiate this class, if an issue in the AIS occurrs.
 * You have to set the location and type of the issue and can also set a parameter, e.g. identifier or language
 * attribute
 * 
 * @author Marcus Vetter
 * 
 */
public class AISIssue extends AbstractIssue {
    
    /**
     * The location of the issue
     */
    private AISIssueLocation location;
    
    /**
     * The type of the issue
     */
    private AISIssueType type;
    
    
    /**
     * The constructor to set the type and location of the issue
     * 
     * @param type
     *            type of the issue
     * @param location
     *            location of the issue
     */
    public AISIssue(AISIssueType type, AISIssueLocation location) {
        this.type = type;
        this.location = location;
    }
    
    
    /**
     * Get the location object for the issue
     * 
     * @return the location
     */
    public AISIssueLocation getLocation() {
        return location;
    }
    
    
    /**
     * Set the location object for the issue
     * 
     * @param location
     *            the location to set
     */
    public void setLocation(AISIssueLocation location) {
        this.location = location;
    }
    
    
    /**
     * Get the type of the issue
     * 
     * @return the type
     */
    public AISIssueType getType() {
        return type;
    }
    
    
    /**
     * Set the type of the issue
     * 
     * @param type
     *            the type to set
     */
    public void setType(AISIssueType type) {
        this.type = type;
    }
    
}
