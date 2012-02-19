package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.contentprovider;

import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.dialogs.RequiredResourceGroupsDialog;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Provides the {@link RequiredResourceGroupsDialog} with the contents
 * 
 * @author Thorsten Berberich
 * 
 */
public class ResourceGroupsDialogContentProvider implements IContentProvider,
	IStructuredContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
     * .lang.Object)
     */
    @Override
    public Object[] getElements(Object arg0) {
	@SuppressWarnings("unchecked")
	List<RGIS> list = (List<RGIS>) arg0;
	return (RGIS[]) list.toArray(new RGIS[list.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {
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
    }
}
