package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import java.util.Locale;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Content provider that feeds the tree viewer with the data from the RGIS-Model
 * 
 * @author Patrick Strobel
 */
public class PrivacySettingsContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {

		// If we are at the root level, return the all privacy settings
		if (inputElement instanceof RGIS) {
			RGIS rgis = (RGIS)inputElement;
			return rgis.getPrivacySettings().toArray();
		} 
		
		// If the parent element is a privacy setting, return it's values
		if (inputElement instanceof RGISPrivacySetting) {
			RGISPrivacySetting ps = (RGISPrivacySetting)inputElement;
			return new String[] {ps.getDescriptionForLocale(Locale.ENGLISH), ps.getValidValueDescription()};
		}
		
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof RGISPrivacySetting) {
			RGISPrivacySetting ps = (RGISPrivacySetting)parentElement;
			return new String[] {ps.getNameForLocale(Locale.ENGLISH), ps.getDescriptionForLocale(Locale.ENGLISH)};
		}

		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return (element instanceof RGISPrivacySetting);
	}

}
