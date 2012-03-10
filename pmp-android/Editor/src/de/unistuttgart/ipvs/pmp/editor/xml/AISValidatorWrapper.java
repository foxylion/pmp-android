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

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.AISValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Wrapper of the AISValidator. The following issues are also attached to the
 * subobjects: - NAME_LOCALE_OCCURRED_TOO_OFTEN -
 * DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN -
 * SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE -
 * SF_IDENTIFIER_OCCURRED_TOO_OFTEN - RRG_IDENTIFIER_OCCURRED_TOO_OFTEN -
 * RPS_IDENTIFIER_OCCURRED_TOO_OFTEN
 * 
 * @author Marcus Vetter
 * 
 */
public class AISValidatorWrapper {

    /**
     * ValidatorWrapperHelper
     */
    ValidatorWrapperHelper helper = ValidatorWrapperHelper.getInstance();

    /**
     * Singleton stuff
     */
    private static AISValidatorWrapper instance = null;

    private AISValidatorWrapper() {

    }

    public static AISValidatorWrapper getInstance() {
	if (instance == null) {
	    instance = new AISValidatorWrapper();
	}
	return instance;
    }

    public List<IIssue> validateAIS(IAIS ais, boolean attachData) {
	List<IIssue> issues = new AISValidator().validateAIS(ais, attachData);
	extendAttachments(issues, attachData);
	addSummaryIssues(ais);
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
		    IssueType issueType = issue.getType();
		    List<String> issueParameters = issue.getParameters();
		    IIssueLocation issueLocation = issue.getLocation();

		    /*
		     * AIS
		     */
		    if (issueLocation instanceof AIS) {

			// Cast the location
			AIS ais = (AIS) issueLocation;

			// Switch the issue types
			switch (issueType) {
			case NAME_LOCALE_OCCURRED_TOO_OFTEN:

			    // Attach names with issue
			    helper.attachIBasicIS(ais.getNames(),
				    IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN,
				    issueParameters);

			    // Remove issue of the ais
			    ais.removeIssue(issue);

			    break;

			case DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:

			    // Attach descriptions with issue
			    helper.attachIBasicIS(
				    ais.getDescriptions(),
				    IssueType.DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN,
				    issueParameters);

			    // Remove issue of the ais
			    ais.removeIssue(issue);

			    break;

			case SF_IDENTIFIER_OCCURRED_TOO_OFTEN:

			    // Attach service features with issue
			    List<IIdentifierIS> identifierISs = new ArrayList<IIdentifierIS>();
			    for (IAISServiceFeature sf : ais
				    .getServiceFeatures()) {
				identifierISs.add(sf);
			    }
			    helper.attachIIdentifierIS(identifierISs,
				    IssueType.SF_IDENTIFIER_OCCURRED_TOO_OFTEN,
				    issueParameters);

			    // Remove issue of the ais
			    ais.removeIssue(issue);

			    break;

			case SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE:

			    // Attach service features with this issue
			    for (IAISServiceFeature sf : ais
				    .getServiceFeatures()) {

				for (String identifier : issue.getParameters()) {
				    if (sf.getIdentifier().equals(identifier)
					    && !sf.hasIssueType(IssueType.SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE)) {
					sf.addIssue(new Issue(
						IssueType.SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE,
						sf));
				    }
				}
			    }

			    // Remove issue of the ais
			    ais.removeIssue(issue);

			    break;
			}

		    }

		    /*
		     * service feature
		     */
		    if (issueLocation instanceof AISServiceFeature) {

			// Cast the location
			AISServiceFeature sf = (AISServiceFeature) issueLocation;

			// Switch the issue types
			switch (issueType) {
			case NAME_LOCALE_OCCURRED_TOO_OFTEN:

			    // Attach names with issue
			    helper.attachIBasicIS(sf.getNames(),
				    IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN,
				    issueParameters);

			    // Remove issue of the sf
			    sf.removeIssue(issue);

			    break;

			case DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:

			    // Attach descriptions with issue
			    helper.attachIBasicIS(
				    sf.getDescriptions(),
				    IssueType.DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN,
				    issueParameters);

			    // Remove issue of the sf
			    sf.removeIssue(issue);

			    break;

			case RRG_IDENTIFIER_OCCURRED_TOO_OFTEN:

			    // Attach service features with issue
			    List<IIdentifierIS> iISs = new ArrayList<IIdentifierIS>();
			    for (IAISRequiredResourceGroup rrg : sf
				    .getRequiredResourceGroups()) {
				iISs.add(rrg);
			    }
			    helper.attachIIdentifierIS(
				    iISs,
				    IssueType.RRG_IDENTIFIER_OCCURRED_TOO_OFTEN,
				    issueParameters);

			    break;

			case RPS_IDENTIFIER_OCCURRED_TOO_OFTEN:
			    // Cast the location (should be possible, if
			    // its this issue type)
			    IAISRequiredResourceGroup rrg = (IAISRequiredResourceGroup) issue
				    .getLocation();

			    // Attach service features with issue
			    List<IIdentifierIS> identISs = new ArrayList<IIdentifierIS>();
			    for (IAISRequiredPrivacySetting ps : rrg
				    .getRequiredPrivacySettings()) {
				identISs.add(ps);
			    }
			    helper.attachIIdentifierIS(
				    identISs,
				    IssueType.RPS_IDENTIFIER_OCCURRED_TOO_OFTEN,
				    issueParameters);

			    break;
			}
		    }

		}
	    } catch (ClassCastException cce) {
		System.out
			.println("Oooops, shit happens! The AISValidatorWrapper failed or the XML Library has changed.");
		cce.printStackTrace();
	    }

	}
    }

    /**
     * Add all summary issues
     * 
     * @param ais
     *            the AIS
     */
    private void addSummaryIssues(IAIS ais) {
	for (IAISServiceFeature sf : ais.getServiceFeatures()) {
	    boolean nameIssue = false;
	    boolean descriptionIssue = false;
	    boolean rrgIssue = false;
	    for (ILocalizedString name : sf.getNames()) {
		if (!name.getIssues().isEmpty())
		    nameIssue = true;
	    }
	    for (ILocalizedString description : sf.getDescriptions()) {
		if (!description.getIssues().isEmpty())
		    descriptionIssue = true;
	    }
	    for (IAISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
		if (!rrg.getIssues().isEmpty())
		    rrgIssue = true;
		for (IAISRequiredPrivacySetting rps : rrg
			.getRequiredPrivacySettings()) {
		    if (!rps.getIssues().isEmpty())
			rrgIssue = true;
		}
	    }
	    if (nameIssue && !sf.hasIssueType(IssueType.AIS_SF_NAME_ISSUES))
		sf.addIssue(new Issue(IssueType.AIS_SF_NAME_ISSUES, sf));
	    if (descriptionIssue
		    && !sf.hasIssueType(IssueType.AIS_SF_DESCRIPTION_ISSUES))
		sf.addIssue(new Issue(IssueType.AIS_SF_DESCRIPTION_ISSUES, sf));
	    if (rrgIssue
		    && !sf.hasIssueType(IssueType.AIS_SF_REQUIRED_RESOURCE_GROUP_ISSUES))
		sf.addIssue(new Issue(
			IssueType.AIS_SF_REQUIRED_RESOURCE_GROUP_ISSUES, sf));
	}
    }

}
