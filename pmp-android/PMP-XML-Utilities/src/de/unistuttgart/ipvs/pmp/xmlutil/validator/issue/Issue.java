/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcus Vetter
 */
public class Issue {
    
    /**
     * The location of the issue
     */
    private IssueLocation location;
    
    /**
     * The type of the issue
     */
    private IssueType type;
    
    /**
     * List of parameters
     */
    private List<String> parameters = new ArrayList<String>();
    
    
    /**
     * The constructor to set the type and location of the issue
     * 
     * @param type
     *            type of the issue
     * @param location
     *            location of the issue
     */
    public Issue(IssueType type, IssueLocation location) {
        this.type = type;
        this.location = location;
    }
    
    
    /**
     * Get the location object for the issue
     * 
     * @return the location
     */
    public IssueLocation getLocation() {
        return location;
    }
    
    
    /**
     * Set the location object for the issue
     * 
     * @param location
     *            the location to set
     */
    public void setLocation(IssueLocation location) {
        this.location = location;
    }
    
    
    /**
     * Get the type of the issue
     * 
     * @return the type
     */
    public IssueType getType() {
        return type;
    }
    
    
    /**
     * Set the type of the issue
     * 
     * @param type
     *            the type to set
     */
    public void setType(IssueType type) {
        this.type = type;
    }
    
    
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
