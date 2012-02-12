package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.jface.viewers.LabelProvider;

import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

public class PrivacySettingsLabelProvider extends LabelProvider {
	public String getText(Object element) {
		if (element instanceof RGISPrivacySetting) {
			RGISPrivacySetting ps = (RGISPrivacySetting)element;
			return ps.getIdentifier() + " (" + ps.getValidValueDescription() + ")";
		} else {
			return (String)element;
		}
	}

}
