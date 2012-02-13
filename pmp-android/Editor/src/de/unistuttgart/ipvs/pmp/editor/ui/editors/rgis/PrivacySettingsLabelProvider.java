package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import java.util.Locale;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

public class PrivacySettingsLabelProvider extends LabelProvider {
	
	@Override
	public String getText(Object element) {
		if (element instanceof RGISPrivacySetting) {
			RGISPrivacySetting ps = (RGISPrivacySetting)element;
			return ps.getIdentifier() + " (" + ps.getValidValueDescription() + ")";
		}
		
		if (element instanceof String){
			return (String)element;
		}
		
		return "Undefined";
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof RGISPrivacySetting) {
			RGISPrivacySetting ps = (RGISPrivacySetting)element;
			if (ps.getIdentifier() == null || ps.getIdentifier().isEmpty() || 
					ps.getValidValueDescription() == null || ps.getValidValueDescription().isEmpty()) {
				return Images.ERROR16;
			}
			
			// Show also an error icon, if default name or desc is not set
			String name = ps.getNameForLocale(Locale.ENGLISH);
			String desc = ps.getDescriptionForLocale(Locale.ENGLISH);
			
			if (name == null || name.isEmpty() || desc == null || desc.isEmpty()) {
				return Images.ERROR16;
			}
		}
		
		// Check if the default-name and desc are set and show warning when not
		if (element  instanceof String){
			if (((String)element).isEmpty()) {
				return Images.ERROR16;
			}
		}
		
		return Images.INFO16;
	}

}
