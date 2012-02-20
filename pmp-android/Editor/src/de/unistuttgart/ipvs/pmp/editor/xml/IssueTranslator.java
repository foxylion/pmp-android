package de.unistuttgart.ipvs.pmp.editor.xml;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;

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
	    switch (issue.getType()) {
	    case LOCALE_INVALID:
		issues += "Locale is invalid\n";
		break;
	    case LOCALE_MISSING:
		issues += "Locale is missing\n";
		break;
	    case NAME_LOCALE_OCCURRED_TOO_OFTEN:
		issues += "Locale of a name occured to often\n";
		break;
	    case NAME_LOCALE_EN_MISSING:
		issues += "A name with an English locale is missing\n";
		break;
	    case DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:
		issues += "Locale of a description occured to often\n";
		break;
	    case DESCRIPTION_LOCALE_EN_MISSING:
		issues += "A description with an English locale is missing\n";
		break;
	    case IDENTIFIER_MISSING:
		issues += "Identifier is missing\n";
		break;
	    default:
		issues += issue.getType().toString() + "\n";
		break;
	    }
	}
	return issues;
    }
}
