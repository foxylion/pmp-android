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
	String issues = "";

	for (IIssue issue : list) {
	    issues += getTranslation(issue.getType()) + "\n";
	}
	return issues;
    }

    public String getTranslation(IssueType type) {
	switch (type) {
	case CHANGE_DESCRIPTION_LOCALE_EN_MISSING:
	    return "A change description with an English locale is missing";
	case CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:
	    return "Locale of a change description occured to often";
	case CLASSNAME_MISSING:
	    return "Classname is missing";
	case CONDITION_MISSING:
	    return "Condition is missing";
	case CREATOR_MISSING:
	    return "Creator is missing";
	case DESCRIPTION_LOCALE_EN_MISSING:
	    return "A description with an English locale is missing";
	case DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:
	    return "Locale of a description occured to often";
	case ICON_MISSING:
	    return "Icon is missing";
	case IDENTIFIER_MISSING:
	    return "Identifier is missing";
	case LOCALE_INVALID:
	    return "Locale is invalid";
	case LOCALE_MISSING:
	    return "Locale is missing";
	case MINREVISION_INVALID:
	    return "Minimum revision is invalid";
	case MINREVISION_MISSING:
	    return "Miniumum revision is missing";
	case NAME_LOCALE_EN_MISSING:
	    return "A name with an English locale is missing";
	case NAME_LOCALE_OCCURRED_TOO_OFTEN:
	    return "Locale of a name occured to often";
	case NAME_MISSING:
	    return "Name is missing";
	default:
	    return type.toString();
	}
    }
}
