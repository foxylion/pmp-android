/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-XML-UTILITIES
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

import java.util.List;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IIssue {
    
    /**
     * Get the {@link IIssueLocation} for the issue
     * 
     * @return the {@link IIssueLocation}
     */
    public abstract IIssueLocation getLocation();
    
    
    /**
     * Set the {@link IIssueLocation} for the issue
     * 
     * @param location
     *            the {@link IIssueLocation} to set
     */
    public abstract void setLocation(IIssueLocation location);
    
    
    /**
     * Get the {@link IssueType} of the issue
     * 
     * @return the {@link IssueType}
     */
    public abstract IssueType getType();
    
    
    /**
     * Set the {@link IssueType} of the issue
     * 
     * @param type
     *            the {@link IssueType} to set
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
    
    
    /**
     * Get the string representation of the {@link IIssue}
     * 
     * @return the string representation of the {@link IIssue}
     */
    @Override
    public abstract String toString();
    
}
