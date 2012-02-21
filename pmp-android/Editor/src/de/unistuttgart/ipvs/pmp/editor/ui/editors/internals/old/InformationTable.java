/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.old;

import java.util.Arrays;
import java.util.Locale;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.LocaleEditingSupport;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.TextEditingSupport;

/**
 * Creates a table with columns for the locale, name and description. It
 * provides a add button, which allow the user to add a new entry and a delete
 * button to remove existing entries. All entries of this table are
 * user-editable.
 * 
 * @author Patrick Strobel
 * 
 */
public class InformationTable {

	private final Composite composite;
	private final TableViewer tableViewer;
	private final StoredInformation storedInformation;
	private final Composite parent;
	private boolean dirty = false;

	public InformationTable(final Composite parent,
			StoredInformation storedInfo, FormToolkit toolkit) {
		this.parent = parent;
		this.storedInformation = storedInfo;

		this.composite = toolkit.createComposite(parent);
		this.composite.setLayout(new GridLayout(1, true));

		// Use grid layout so that the table uses the whole screen width
		final GridData layoutData = new GridData();
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.grabExcessHorizontalSpace = true;

		// Workaround for SWT-Bug needed
		// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
		layoutData.widthHint = 1;

		// Set layout as we want to set a relative columns width
		Composite tableComposite = new Composite(this.composite, SWT.NONE);
		tableComposite.setLayoutData(layoutData);
		TableColumnLayout columnLayout = new TableColumnLayout();
		tableComposite.setLayout(columnLayout);

		// Define the table's view
		this.tableViewer = new TableViewer(tableComposite, SWT.BORDER
				| SWT.FULL_SELECTION);
		this.tableViewer.getTable().setLayoutData(layoutData);
		this.tableViewer.getTable().setHeaderVisible(true);
		this.tableViewer.getTable().setLinesVisible(true);

		// Set header
		buildColumns(columnLayout);

		// Add content
		this.tableViewer.setContentProvider(new MapContentProvider());
		this.tableViewer.setInput(this.storedInformation.getMap());

		Composite buttonCompo = toolkit.createComposite(this.composite);
		buttonCompo.setLayout(new RowLayout());

		// Add button to allow the user to add a new entry
		final Button addButton = toolkit.createButton(buttonCompo, "Add",
				SWT.PUSH);
		addButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add empty entry to table
				InformationTable.this.storedInformation.add(new Information(
						"-", "-", "-"));
				InformationTable.this.dirty = true;
				refresh();

			}

		});

		// Add button to allow the user to remove a selected entry
		final Button removeButton = toolkit.createButton(buttonCompo, "Remove",
				SWT.PUSH);
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Remove entry from model if an entry is selected
				StructuredSelection sel = (StructuredSelection) InformationTable.this.tableViewer
						.getSelection();
				Information info = (Information) sel.getFirstElement();
				if (info != null) {
					InformationTable.this.storedInformation.remove(info
							.getLocale());
					InformationTable.this.dirty = true;
					refresh();
				}
			}

		});

		// Add listener to auto-update the table whenever a value is changed by
		// the user
		this.tableViewer.getColumnViewerEditor().addEditorActivationListener(
				new ColumnViewerEditorActivationListener() {

					@Override
					public void beforeEditorDeactivated(
							ColumnViewerEditorDeactivationEvent event) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeEditorActivated(
							ColumnViewerEditorActivationEvent event) {
						addButton.setEnabled(false);
					}

					@Override
					public void afterEditorDeactivated(
							ColumnViewerEditorDeactivationEvent event) {
						InformationTable.this.tableViewer.refresh();
						addButton.setEnabled(true);
					}

					@Override
					public void afterEditorActivated(
							ColumnViewerEditorActivationEvent event) {
						// TODO Auto-generated method stub

					}
				});

		// Only enable remove-button when a row is selected in the table
		this.tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						removeButton
								.setEnabled(!event.getSelection().isEmpty());
					}
				});
	}

	public Composite getControl() {
		return this.composite;
	}

	public void refresh() {
		// Refresh view and layout
		// tableViewer.getTable().setLayoutData(layoutData);
		this.tableViewer.refresh();
		this.parent.getParent().layout();
		this.parent.layout();
	}

	public StoredInformation getStoredInformation() {
		return this.storedInformation;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return this.dirty;
	}

	private void buildColumns(TableColumnLayout columnLayout) {
		// Error
		TableViewerColumn errorColumn = new TableViewerColumn(this.tableViewer,
				SWT.BORDER);
		columnLayout.setColumnData(errorColumn.getColumn(),
				new ColumnPixelData(25, false));
		errorColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}

			@Override
			public Image getImage(Object element) {
				String locale = ((Information) element).getLocale();
				// Show an error if the locale is invalid
				if (Arrays.binarySearch(Locale.getISOLanguages(), locale) < 0) {
					return Images.ERROR16;
				}
				return Images.FILE16;
			}
		});

		// Locale
		TableViewerColumn localeColumn = new TableViewerColumn(
				this.tableViewer, SWT.BORDER);
		columnLayout.setColumnData(localeColumn.getColumn(),
				new ColumnPixelData(50, true));
		localeColumn.getColumn().setText("Locale");
		localeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information) element).getLocale();
			}
		});

		localeColumn.setEditingSupport(new LocaleEditingSupport(
				this.tableViewer) {

			@Override
			protected Object getValue(Object element) {
				return ((Information) element).getLocale();
			}

			@Override
			protected void setValue(Object element, Object value) {
				String locale = ((String) value).toLowerCase();

				// Set locale only if it's not in use by another entry
				if (!InformationTable.this.storedInformation
						.localeExists(locale)) {
					((Information) element).setLocale(locale);
					InformationTable.this.dirty = true;
				}
			}

		});

		// Name
		TableViewerColumn nameColumn = new TableViewerColumn(this.tableViewer,
				SWT.BORDER);
		columnLayout.setColumnData(nameColumn.getColumn(),
				new ColumnWeightData(1, true));
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information) element).getName();
			}
		});

		nameColumn.setEditingSupport(new TextEditingSupport(this.tableViewer) {

			@Override
			protected Object getValue(Object element) {
				return ((Information) element).getName();
			}

			@Override
			protected void setValue(Object element, Object value) {
				Information info = (Information) element;
				if (info.getName() != value) {
					info.setName((String) value);
					InformationTable.this.dirty = true;
				}
			}
		});

		// Description
		TableViewerColumn descriptionColumn = new TableViewerColumn(
				this.tableViewer, SWT.BORDER);
		columnLayout.setColumnData(descriptionColumn.getColumn(),
				new ColumnWeightData(2, true));
		descriptionColumn.getColumn().setText("Description");
		descriptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Information) element).getDescription();
			}
		});

		descriptionColumn.setEditingSupport(new TextEditingSupport(
				this.tableViewer) {

			@Override
			protected Object getValue(Object element) {
				return ((Information) element).getDescription();
			}

			@Override
			protected void setValue(Object element, Object value) {
				Information info = (Information) element;
				if (info.getDescription() != value) {
					info.setDescription((String) value);
					InformationTable.this.dirty = true;
				}

			}
		});

	}

}
