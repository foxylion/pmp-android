package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.internal;

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

	// If we are at the root level, return all privacy settings
	if (inputElement instanceof RGIS) {
	    RGIS rgis = (RGIS) inputElement;
	    return rgis.getPrivacySettings().toArray();
	}

	return null;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
	if (parentElement instanceof RGISPrivacySetting) {
	    RGISPrivacySetting ps = (RGISPrivacySetting) parentElement;

	    // Return default (English) name and description if set
	    NameString name = new NameString(
		    ps.getNameForLocale(Locale.ENGLISH), ps);
	    DescriptionString desc = new DescriptionString(
		    ps.getDescriptionForLocale(Locale.ENGLISH), ps);
	    ChangeDescriptionString changeDesc = new ChangeDescriptionString(
		    ps.getChangeDescriptionForLocale(Locale.ENGLISH), ps);
	    return new Object[] { name, desc, changeDesc };
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
