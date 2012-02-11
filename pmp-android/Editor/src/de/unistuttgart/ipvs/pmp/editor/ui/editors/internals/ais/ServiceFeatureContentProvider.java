/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

/**
 * @author Thorsten
 *
 */
public class ServiceFeatureContentProvider implements IContentProvider, IStructuredContentProvider{

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    @Override
    public Object[] getElements(Object arg0) {
	int size = Model.getInstance().getAis().getServiceFeatures().size();
	String[] sf = new String[size];
	for (int itr = 0; itr < size; itr++){
	    sf[itr] = Model.getInstance().getAis().getServiceFeatures().get(itr).getIdentifier();
	}
	return sf;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	// TODO Auto-generated method stub
	
    }

}
