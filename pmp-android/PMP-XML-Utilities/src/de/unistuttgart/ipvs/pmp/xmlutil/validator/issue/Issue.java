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
public class Issue implements IIssue {
    
    /**
     * The location of the issue
     */
    private IIssueLocation location;
    
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
    public Issue(IssueType type, IIssueLocation location) {
        this.type = type;
        this.location = location;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue#getLocation()
     */
    @Override
    public IIssueLocation getLocation() {
        return location;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue#setLocation(de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation)
     */
    @Override
    public void setLocation(IIssueLocation location) {
        this.location = location;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue#getType()
     */
    @Override
    public IssueType getType() {
        return type;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue#setType(de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType)
     */
    @Override
    public void setType(IssueType type) {
        this.type = type;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue#getParameters()
     */
    @Override
    public List<String> getParameters() {
        return parameters;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue#addParameter(java.lang.String)
     */
    @Override
    public void addParameter(String parameter) {
        this.parameters.add(parameter);
    }
    
}
