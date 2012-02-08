package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class InformationTable {
	
	private final TableViewer tableViewer;
	private final StoredInformation storedInformation = new StoredInformation();

	public InformationTable(Composite parent, Object layoutData) {
		
		// Set layout as we want to set a relative columns width
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(layoutData);
		TableColumnLayout columnLayout = new TableColumnLayout();
		tableComposite.setLayout(columnLayout);
		
		// Define the table's view
		tableViewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		// Set header
		buildColumns(columnLayout);
		
		// Add content
		tableViewer.setContentProvider(new ObservableListContentProvider());
		tableViewer.setInput(storedInformation.getObservableList());
		
		// Add listener to auto-update the table whenever a value is changed by the user
		tableViewer.getColumnViewerEditor().addEditorActivationListener(new ColumnViewerEditorActivationListener() {
			
			@Override
			public void beforeEditorDeactivated(
					ColumnViewerEditorDeactivationEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
				tableViewer.refresh();				
			}
			
			@Override
			public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void buildColumns(TableColumnLayout columnLayout) {
		// Locale
		TableViewerColumn localeColumn = new TableViewerColumn(tableViewer, SWT.BORDER);
		columnLayout.setColumnData(localeColumn.getColumn(), new ColumnPixelData(100));
		localeColumn.getColumn().setText("Locale");
		localeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information)element).getLocale();
			}
		});
		localeColumn.setEditingSupport(new ObservableValueEditingSupport(tableViewer, new DataBindingContext()) {

			@Override
			protected IObservableValue doCreateCellEditorObservable(
					CellEditor cellEditor) {
				return ViewersObservables.observeSingleSelection(((ComboBoxViewerCellEditor) cellEditor).getViewer());
			}

			@Override
			protected IObservableValue doCreateElementObservable(
					Object element, ViewerCell cell) {
				return BeansObservables.observeValue(element, "locale");
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new ComboBoxViewerCellEditor((Composite) tableViewer.getControl());
			}
			
		});
		
		// Name
		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.BORDER);
		columnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(1));
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information)element).getName();
			}
		});
		nameColumn.setEditingSupport(new ObservableValueEditingSupport(tableViewer, new DataBindingContext()) {

			@Override
			protected IObservableValue doCreateCellEditorObservable(
					CellEditor cellEditor) {
				return SWTObservables.observeText((Text) cellEditor.getControl(), SWT.Modify);
			}

			@Override
			protected IObservableValue doCreateElementObservable(
					Object element, ViewerCell cell) {
				return BeansObservables.observeValue(element, "name");
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor((Composite) tableViewer.getControl());
			}
			
		});
		
		// Description
		TableViewerColumn descriptionColumn = new TableViewerColumn(tableViewer, SWT.BORDER);
		columnLayout.setColumnData(descriptionColumn.getColumn(), new ColumnWeightData(2));
		descriptionColumn.getColumn().setText("Description");
		descriptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information)element).getDescription();
			}
		});
	}
	
}