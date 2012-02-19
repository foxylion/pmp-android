package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.contentprovider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.dialogs.RequiredPrivacySettingsDialog;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Provides the contents for the {@link RequiredPrivacySettingsDialog}
 * 
 * @author Thorsten Berberich
 * 
 */
public class PrivacySettingsDialogContentProvider implements
	IStructuredContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
     * .viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
     * .lang.Object)
     */
    @Override
    public Object[] getElements(Object arg0) {
	return ((RGIS) arg0).getPrivacySettings().toArray();
    }

}
