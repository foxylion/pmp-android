package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

import java.io.Serializable;
import java.util.List;

public interface IIssueLocation extends Serializable {
    
    /**
     * Get the issues
     * 
     * @return list of issues
     */
    public abstract List<IIssue> getIssues();
    
    
    /**
     * Add an issue
     * 
     * @param issue
     *            issue to add
     */
    public abstract void addIssue(IIssue issue);
    
    
    /**
     * Remove an issue
     * 
     * @param issue
     *            issue to remove
     */
    public abstract void removeIssue(IIssue issue);
    
    
    /**
     * Clear all issues and clear also all issues of linked data
     */
    public abstract void clearIssuesAndPropagate();
    
}
