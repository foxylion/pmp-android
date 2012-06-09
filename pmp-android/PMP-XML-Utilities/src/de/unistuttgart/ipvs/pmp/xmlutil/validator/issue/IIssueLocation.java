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

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IIssueLocation extends Serializable {
    
    /**
     * Get the {@link IIssue}s
     * 
     * @return list of {@link IIssue}s
     */
    public abstract List<IIssue> getIssues();
    
    
    /**
     * Add an {@link IIssue}
     * 
     * @param issue
     *            {@link IIssue} to add
     */
    public abstract void addIssue(IIssue issue);
    
    
    /**
     * Remove an {@link IIssue}
     * 
     * @param issue
     *            {@link IIssue} to remove
     */
    public abstract void removeIssue(IIssue issue);
    
    
    /**
     * Clear all {@link IIssue}s
     */
    public abstract void clearIssues();
    
    
    /**
     * Check, whether the {@link IIssue} with the given {@link IssueType} is present or not
     */
    public abstract boolean hasIssueType(IssueType issueType);
    
}
