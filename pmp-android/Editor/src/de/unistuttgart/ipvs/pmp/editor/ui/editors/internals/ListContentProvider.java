package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ListContentProvider implements IContentProvider,
	IStructuredContentProvider {

    @SuppressWarnings("unchecked")
    @Override
    public Object[] getElements(Object inputElement) {
	return ((List<Object>) inputElement).toArray();
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
