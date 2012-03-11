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
 * This is the abstract class for an {@link IIssueLocation}
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class IssueLocation implements IIssueLocation {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 814952109838292372L;
    
    /**
     * This list contains all {@link IIssue}s
     */
    private List<IIssue> issues = new ArrayList<IIssue>();
    
    
    @Override
    public List<IIssue> getIssues() {
        return this.issues;
    }
    
    
    @Override
    public void addIssue(IIssue issue) {
        this.issues.add(issue);
    }
    
    
    @Override
    public void removeIssue(IIssue issue) {
        this.issues.remove(issue);
    }
    
    
    @Override
    public void clearIssues() {
        this.issues.clear();
    }
    
    
    @Override
    public boolean hasIssueType(IssueType issueType) {
        boolean hasIssueType = false;
        for (IIssue issue : getIssues()) {
            if (issue.getType().equals(issueType)) {
                hasIssueType = true;
            }
        }
        return hasIssueType;
    }
    
}
