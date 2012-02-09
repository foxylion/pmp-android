package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import java.util.Map;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class MapContentProvider implements IContentProvider, IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		Map<Object,Object> inputMap = (Map<Object,Object>)inputElement;
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
