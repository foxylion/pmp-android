package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import java.util.Locale;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

public abstract class LocaleEditingSupport extends EditingSupport {

	private ComboBoxViewerCellEditor editor;
	
	public LocaleEditingSupport(TableViewer viewer) {
		super(viewer);
		editor = new ComboBoxViewerCellEditor((Composite) viewer.getControl());
		editor.setContentProvider(new ArrayContentProvider());
		editor.setInput(Locale.getISOLanguages());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

}
