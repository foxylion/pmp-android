package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.internal;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

public class PrivacySettingsLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof RGISPrivacySetting) {
			RGISPrivacySetting ps = (RGISPrivacySetting) element;
			return ps.getIdentifier() + " (" + ps.getValidValueDescription()
					+ ")";
		}

		if (element instanceof EncapsulatedString) {
			EncapsulatedString es = (EncapsulatedString)element;
			String string = es.getString();
			// Trim the string
			if (string.length() > 50) {
				string = string.substring(0,50) + "...";
			}
			
			if (element instanceof NameString) {
				return "Name: " + string;
			}
			if (element instanceof DescriptionString) {
				return "Description: " + string;
			}
			
			if (element instanceof ChangeDescriptionString) {
				return "Change Description: " + string;
			}
		}

		return "Undefined";
	}

	@Override
	public Image getImage(Object element) {
		// Get PS
		RGISPrivacySetting ps = null;
		if (element instanceof RGISPrivacySetting) {
			ps = (RGISPrivacySetting) element;
			
			if (ps.getIssues().isEmpty()) {
				return Images.INFO16;
			} else {
				return Images.ERROR16;
			}
			
		} else if (element instanceof EncapsulatedString) {
			EncapsulatedString es = (EncapsulatedString)element;
			ps = es.getPrivacySetting();
			
			// Show icon only if this field is invalid
			for (IIssue i : ps.getIssues()) {
				IssueType type = i.getType();
				if (es instanceof NameString &&
						(type == IssueType.NAME_LOCALE_EN_MISSING
						|| type == IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN
						|| type == IssueType.NAME_MISSING)) {
					return Images.ERROR16;
				}
				
				if (es instanceof DescriptionString &&
						(type == IssueType.DESCRIPTION_LOCALE_EN_MISSING
						|| type == IssueType.DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN)) {
					return Images.ERROR16;
				}
				
				if (es instanceof ChangeDescriptionString &&
						(type == IssueType.CHANGE_DESCRIPTION_LOCALE_EN_MISSING
						|| type == IssueType.CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN)) {
					return Images.ERROR16;
				}
				//System.out.println("Issue: " + i.getType());
			}
			return Images.INFO16;
		}

		return null;
	}

}
