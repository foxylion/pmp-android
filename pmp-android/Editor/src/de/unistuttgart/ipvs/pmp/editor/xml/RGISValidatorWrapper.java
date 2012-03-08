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

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.RGISValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Wrapper of the RGISValidator. The following issues are also attached to the
 * subobjects: - PS_IDENTIFIER_OCCURRED_TOO_OFTEN -
 * CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISValidatorWrapper {

    /**
     * ValidatorWrapperHelper
     */
    ValidatorWrapperHelper helper = ValidatorWrapperHelper.getInstance();

    /**
     * Singleton stuff
     */
    private static RGISValidatorWrapper instance = null;

    private RGISValidatorWrapper() {

    }

    public static RGISValidatorWrapper getInstance() {
	if (instance == null) {
	    instance = new RGISValidatorWrapper();
	}
	return instance;
    }

    public List<IIssue> validateRGIS(IRGIS rgis, boolean attachData) {
	List<IIssue> issues = new RGISValidator().validateRGIS(rgis, attachData);
	extendAttachments(issues, attachData);
	return issues;
    }

    /**
     * INTERNAL USE ONLY! Extend the attachments of issues
     * 
     * @param issues
     *            the list of issues
     * @param attachData
     *            only extend the attachments, if its true
     */
    private void extendAttachments(List<IIssue> issues, boolean attachData) {
	if (attachData) {
	    try {
		for (IIssue issue : issues) {

		    // The issue
		    IssueType type = issue.getType();
		    List<String> parameters = issue.getParameters();

		    // Switch the issue types
		    switch (type) {
		    case PS_IDENTIFIER_OCCURRED_TOO_OFTEN:
			// Cast the location (should be possible, if
			// its this issue type)
			IRGIS rgis = (IRGIS) issue.getLocation();

			// Attach service features with issue
			List<IIdentifierIS> identifierISs = new ArrayList<IIdentifierIS>();
			for (IRGISPrivacySetting ps : rgis.getPrivacySettings()) {
			    identifierISs.add(ps);
			}
			helper.attachIIdentifierIS(identifierISs,
				IssueType.PS_IDENTIFIER_OCCURRED_TOO_OFTEN,
				parameters);

			break;

		    case CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:
			// Cast the location (should be possible, if
			// its this issue type)
			IRGISPrivacySetting changeDescrLocation = (IRGISPrivacySetting) issue
				.getLocation();

			// Attach change descriptions with issues
			helper.attachIBasicIS(
				changeDescrLocation.getChangeDescriptions(),
				IssueType.CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN,
				parameters);

			break;

		    }

		}
	    } catch (ClassCastException cce) {
		System.out
			.println("Oooops, shit happens! The RGISValidatorWrapper failed or the XML Library has changed.");
		cce.printStackTrace();
	    }

	}
    }

}
