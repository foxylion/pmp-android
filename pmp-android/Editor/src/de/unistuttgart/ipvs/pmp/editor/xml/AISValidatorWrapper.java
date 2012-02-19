package de.unistuttgart.ipvs.pmp.editor.xml;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.AISValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
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
 * 
 * @author Marcus Vetter
 * 
 */
public class AISValidatorWrapper extends AISValidator {

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

	@Override
	public List<IIssue> validateAppInformation(IAIS ais, boolean attachData) {
		List<IIssue> issues = super.validateAppInformation(ais, attachData);
		extendAttachments(issues, attachData);
		return issues;
	}

	@Override
	public List<IIssue> validateServiceFeatures(IAIS ais, boolean attachData) {
		List<IIssue> issues = super.validateServiceFeatures(ais, attachData);
		extendAttachments(issues, attachData);
		return issues;
	}

	@Override
	public List<IIssue> validateServiceFeature(IAISServiceFeature sf,
			boolean attachData) {
		List<IIssue> issues = super.validateServiceFeature(sf, attachData);
		extendAttachments(issues, attachData);
		return issues;
	}

	@Override
	public List<IIssue> validateRequiredResourceGroup(
			IAISRequiredResourceGroup rrg, boolean attachData) {
		List<IIssue> issues = super.validateRequiredResourceGroup(rrg,
				attachData);
		extendAttachments(issues, attachData);
		return issues;
	}

	@Override
	public List<IIssue> validateRequiredPrivacySetting(
			IAISRequiredPrivacySetting rps, boolean attachData) {
		List<IIssue> issues = super.validateRequiredPrivacySetting(rps,
				attachData);
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
					case NAME_LOCALE_OCCURRED_TOO_OFTEN:
						// Cast the location (should be possible, if
						// its this issue type)
						IBasicIS nameLocation = (IBasicIS) issue.getLocation();

						// Attach names with issue
						attachIBasicIS(nameLocation.getNames(),
								IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN,
								parameters);

						break;

					case DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:
						// Cast the location (should be possible, if
						// its this issue type)
						IBasicIS descrLocation = (IBasicIS) issue.getLocation();

						// Attach descriptions with issue
						attachIBasicIS(
								descrLocation.getDescriptions(),
								IssueType.DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN,
								parameters);

						break;

					case SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE:
						// Cast the location (should be possible, if
						// its this issue type)
						IAIS aisLocation = (IAIS) issue.getLocation();

						// Attach service features with this issue
						for (IAISServiceFeature sf : aisLocation
								.getServiceFeatures()) {

							// if the sf has not the issue yet
							boolean hasIssue = false;
							for (IIssue assignedIssue : sf.getIssues()) {
								if (assignedIssue
										.getType()
										.equals(IssueType.SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE))
									hasIssue = true;
							}
							if (!hasIssue) {
								sf.addIssue(new Issue(
										IssueType.SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE,
										sf));
							}
						}

						break;

					case SF_IDENTIFIER_OCCURRED_TOO_OFTEN:
						// Cast the location (should be possible, if
						// its this issue type)
						IAIS ais = (IAIS) issue.getLocation();

						// Attach service features with issue
						List<IIdentifierIS> identifierISs = new ArrayList<IIdentifierIS>();
						for (IAISServiceFeature sf : ais.getServiceFeatures()) {
							identifierISs.add(sf);
						}
						attachIIdentifierIS(identifierISs,
								IssueType.SF_IDENTIFIER_OCCURRED_TOO_OFTEN,
								parameters);

						break;

					case RRG_IDENTIFIER_OCCURRED_TOO_OFTEN:
						// Cast the location (should be possible, if
						// its this issue type)
						IAISServiceFeature sf = (IAISServiceFeature) issue
								.getLocation();

						// Attach service features with issue
						List<IIdentifierIS> iISs = new ArrayList<IIdentifierIS>();
						for (IAISRequiredResourceGroup rrg : sf
								.getRequiredResourceGroups()) {
							iISs.add(rrg);
						}
						attachIIdentifierIS(iISs,
								IssueType.RRG_IDENTIFIER_OCCURRED_TOO_OFTEN,
								parameters);

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
						attachIIdentifierIS(identISs,
								IssueType.RPS_IDENTIFIER_OCCURRED_TOO_OFTEN,
								parameters);

						break;
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
	private void attachIIdentifierIS(List<IIdentifierIS> identifierISs,
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
	private void attachIBasicIS(List<ILocalizedString> localizedStrings,
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
