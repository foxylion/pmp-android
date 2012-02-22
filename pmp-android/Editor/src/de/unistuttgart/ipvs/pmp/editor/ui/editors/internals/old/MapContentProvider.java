package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.old;

import java.util.Map;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Feeds a table with data from a map
 * 
 * @author Patrick Strobel
 */
public class MapContentProvider implements IContentProvider,
	IStructuredContentProvider {

    @Override
    public Object[] getElements(Object inputElement) {
	@SuppressWarnings("unchecked")
	Map<Object, Object> inputMap = (Map<Object, Object>) inputElement;
	return inputMap.values().toArray();
    }

    @Override
    public void dispose() {
	// TODO Auto-generated method stub

    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	// TODO Auto-generated method stub

    }

}
