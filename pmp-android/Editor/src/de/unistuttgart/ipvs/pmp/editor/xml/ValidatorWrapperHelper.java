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

import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Helper class for the Validation-Wrapper INTERNAL USE ONLY!
 * 
 * @author Marcus Vetter
 * 
 */
public class ValidatorWrapperHelper {

    /**
     * Singleton stuff
     */
    private static ValidatorWrapperHelper instance = null;

    private ValidatorWrapperHelper() {

    }

    protected static ValidatorWrapperHelper getInstance() {
	if (instance == null) {
	    instance = new ValidatorWrapperHelper();
	}
	return instance;
    }

    /**
     * INTERNAL USE ONLY! Extend the attachments of
     * "identifier occurred too often"
     * 
     * @param identifierISs
     *            list of identifier information sets
     * @param issueType
     *            the issue type
     * @param issueParameters
     *            the parameters of the issue
     */
    protected void attachIIdentifierIS(List<IIdentifierIS> identifierISs,
	    IssueType issueType, List<String> issueParameters) {
	// Attach service features with issue
	for (IIdentifierIS identifierIS : identifierISs) {
	    String ident = identifierIS.getIdentifier();

	    // if the identifier is the same, add the issue
	    if ((issueParameters.size() == 1 && ident.equals(issueParameters
		    .get(0)))
		    || (issueParameters.size() == 0 && ident.equals(""))) {
		identifierIS.addIssue(new Issue(issueType, identifierIS));
	    }
	}
    }

    /**
     * INTERNAL USE ONLY! Extend the attachments of
     * "Name/Description/RGISChangeDescription occurred too often"
     * 
     * @param localizedStrings
     *            list of localized strings
     * @param issueType
     *            the issue type
     * @param issueParameters
     *            the parameters of the issue
     */
    protected void attachIBasicIS(List<ILocalizedString> localizedStrings,
	    IssueType issueType, List<String> issueParameters) {
	// Attach localized strings with issue
	for (ILocalizedString localizedString : localizedStrings) {
	    String locale = localizedString.getLocale().getLanguage();

	    // if the locale is the same
	    if ((issueParameters.size() == 1 && locale.equals(issueParameters
		    .get(0)))
		    || (issueParameters.size() == 0 && locale.equals(""))) {
		localizedString.addIssue(new Issue(issueType, localizedString));
	    }
	}
    }

}
