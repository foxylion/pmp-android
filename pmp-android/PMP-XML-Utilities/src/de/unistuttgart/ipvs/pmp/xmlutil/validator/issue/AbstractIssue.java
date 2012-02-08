package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

import java.util.List;

public abstract class AbstractIssue {
    
    /**
     * List of parameters
     */
    private List<String> parameters;
    
    
    /**
     * Get the parameter
     * 
     * @return the parameter ("", if no parameter exists)
     */
    public List<String> getParameters() {
        return parameters;
    }
    
    
    /**
     * Set the parameter
     * 
     * @param parameter
     *            the parameter to set
     */
    public void addParameter(String parameter) {
        this.parameters.add(parameter);
    }
    
}
