/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
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
package de.unistuttgart.ipvs.pmp.editor.xml;

import java.util.List;

import de.unistuttgart.ipvs.pmp.editor.util.I18N;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Translates the given {@link Issue}s into human readable texts
 * 
 * @author Thorsten Berberich
 * 
 */
public class IssueTranslator {
    
    /**
     * Translates the given {@link List} of {@link IIssue}s into a string that
     * can be display as tool tip
     * 
     * @param list
     *            {@link List} of {@link Issue}s
     * @return {@link String} with the tool tip message
     */
    public String translateIssues(List<IIssue> list) {
        String issues = ""; //$NON-NLS-1$
        
        for (IIssue issue : list) {
            issues += I18N.getIssue(issue.getType(), issue.getParameters()) + "\n"; //$NON-NLS-1$
        }
        return issues;
    }
    
    
    /**
     * Translates one given {@link IssueType} into a string for a tool tip
     * 
     * @param type
     *            {@link IssueType} to translate
     * @return {@link String} for a tool tip
     */
    public String getTranslationWithoutParameters(IssueType type) {
        return I18N.getIssue(type);
    }
}
