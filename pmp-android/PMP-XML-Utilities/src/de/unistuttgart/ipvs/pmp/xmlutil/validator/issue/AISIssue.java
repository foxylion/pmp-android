package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

public class AISIssue {
    
    /**
     * The location of the issue
     */
    private AISIssueLocation location;
    
    /**
     * The type of the issue
     */
    private AISIssueType type;
    
    
    /**
     * 
     */
    
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
