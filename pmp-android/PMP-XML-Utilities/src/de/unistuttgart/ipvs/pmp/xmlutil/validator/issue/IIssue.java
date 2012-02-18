package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

import java.util.List;

public interface IIssue {
    
    /**
     * Get the location object for the issue
     * 
     * @return the location
     */
    public abstract IIssueLocation getLocation();
    
    
    /**
     * Set the location object for the issue
     * 
     * @param location
     *            the location to set
     */
    public abstract void setLocation(IIssueLocation location);
    
    
    /**
     * Get the type of the issue
     * 
     * @return the type
     */
    public abstract IssueType getType();
    
    
    /**
     * Set the type of the issue
     * 
     * @param type
     *            the type to set
     */
    public abstract void setType(IssueType type);
    
    
    /**
     * Get the parameter
     * 
     * @return the parameter ("", if no parameter exists)
     */
    public abstract List<String> getParameters();
    
    
    /**
     * Set the parameter
     * 
     * @param parameter
     *            the parameter to set
     */
    public abstract void addParameter(String parameter);
    
}
