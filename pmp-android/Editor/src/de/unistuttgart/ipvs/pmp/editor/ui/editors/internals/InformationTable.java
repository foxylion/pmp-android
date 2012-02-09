package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Creates a table with columns for the locale, name and description.
 * It provides a add button, which allow the user to add a new entry and a delete button
 * to remove existing entries.
 * All entries of this table are user-editable.
 * 
 * @author Patrick Strobel
 *
 */
public class InformationTable {
	
	private final Composite composite;
	private final TableViewer tableViewer;
	private final StoredInformation storedInformation;

	public InformationTable(final Composite parent, StoredInformation storedInfo, FormToolkit toolkit) {
		storedInformation = storedInfo;
		
		composite = toolkit.createComposite(parent);
		composite.setLayout(new GridLayout(1, true));
		
		// Use grid layout so that the table uses the whole screen width
		final GridData layoutData = new GridData();
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.grabExcessHorizontalSpace = true;
		
		// Workaround for SWT-Bug needed (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
		layoutData.widthHint = 1;
		
		// Set layout as we want to set a relative columns width
		Composite tableComposite = new Composite(composite, SWT.NONE);
		tableComposite.setLayoutData(layoutData);
		TableColumnLayout columnLayout = new TableColumnLayout();
		tableComposite.setLayout(columnLayout);
		
		
		// Define the table's view
		tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(layoutData);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		// Set header
		buildColumns(columnLayout);
		
		// Add content
		tableViewer.setContentProvider(new MapContentProvider());
		tableViewer.setInput(storedInformation.getMap());
		
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
		
		Composite buttonCompo = toolkit.createComposite(composite);
		buttonCompo.setLayout(new RowLayout());
		
		// Add button to allow the user to add a new entry
		Button addButton = toolkit.createButton(buttonCompo, "Add", SWT.PUSH);
		addButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add empty entry to table
				storedInformation.add(new Information("-", "-", "-"));
				
				// Refresh view
				//tableViewer.getTable().setLayoutData(layoutData);
				tableViewer.refresh();
				parent.getParent().layout();
			}
			
		});
		
		// Add button to allow the user to remove a selected entry
		Button deleteButton = toolkit.createButton(buttonCompo, "Delete", SWT.PUSH);
		deleteButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Remove entry from model if an entry is selected
				StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
				Information info = (Information)sel.getFirstElement();
				if (info != null) {
					storedInformation.remove(info.getLocale());
					
					// Refresh view and layout
					tableViewer.refresh();
					parent.getParent().layout();
				}
			}
			
		});
	}
	
	public Composite getControl() {
		return composite;
	}
	
	private void buildColumns(TableColumnLayout columnLayout) {
		// Locale
		TableViewerColumn localeColumn = new TableViewerColumn(tableViewer, SWT.BORDER);
		columnLayout.setColumnData(localeColumn.getColumn(), new ColumnPixelData(50, true));
		localeColumn.getColumn().setText("Locale");
		localeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information)element).getLocale();
			}
		});
		localeColumn.setEditingSupport(new LocaleEditingSupport(tableViewer) {

			@Override
			protected Object getValue(Object element) {
				return ((Information)element).getLocale();
			}

			@Override
			protected void setValue(Object element, Object value) {
				// Set locale only if it's not in use by another entry
				if (!storedInformation.localeExists((String)value)) {
					((Information)element).setLocale((String)value);
				}
			}
			
		});
		
		
		// Name
		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.BORDER);
		columnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(50, true));
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information)element).getName();
			}
		});

		nameColumn.setEditingSupport(new TextEditingSupport(tableViewer) {

			@Override
			protected Object getValue(Object element) {
				return ((Information)element).getName();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Information)element).setName((String)value);
				
			}
		});

		// Description
		TableViewerColumn descriptionColumn = new TableViewerColumn(tableViewer, SWT.BORDER);
		columnLayout.setColumnData(descriptionColumn.getColumn(), new ColumnWeightData(50, true));
		descriptionColumn.getColumn().setText("Description");
		descriptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information)element).getDescription();
			}
		});
		
		descriptionColumn.setEditingSupport(new TextEditingSupport(tableViewer) {

			
			@Override
			protected Object getValue(Object element) {
				return ((Information)element).getDescription();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Information)element).setDescription((String)value);
				
			}
		});
	}
	
}
