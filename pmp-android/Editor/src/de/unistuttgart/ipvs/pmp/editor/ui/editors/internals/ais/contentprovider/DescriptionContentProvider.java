package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.contentprovider;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

/**
 * Provides the description table with data
 * 
 * @author Thorsten Berberich
 * 
 */
public class DescriptionContentProvider implements IContentProvider,
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
	return ((AISServiceFeature) arg0).getDescriptions().toArray();
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
