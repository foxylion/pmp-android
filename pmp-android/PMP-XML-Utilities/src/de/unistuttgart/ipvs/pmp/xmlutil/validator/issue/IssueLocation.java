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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the abstract class for an issue location
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class IssueLocation implements Serializable {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 814952109838292372L;
    
    /**
     * This list contains all issues appeared in this object
     */
    private List<Issue> issues = new ArrayList<Issue>();
    
    
    /**
     * Get the issues
     * 
     * @return list of issues
     */
    public List<Issue> getIssues() {
        return issues;
    }
    
    
    /**
     * Add an issue
     * 
     * @param issue
     *            issue to add
     */
    public void addIssue(Issue issue) {
        issues.add(issue);
    }
    
    
    /**
     * Clear all issues
     */
    public void clearIssues() {
        issues.clear();
    }
    
}
