package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localization;

import java.util.Locale;

import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.FocusCellHighlighter;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.LocaleEditingSupport;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.TextEditingSupport;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.AbstractLocale;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;

public class LocaleTable {
	
	private final BasicIS data;
	private final Composite parent;
	private final Composite outerCompo;
	private final Type type;
	private final TableViewer tableViewer;
	private boolean dirty = false;
	public enum Type {NAME, DESCRIPTION};
	
	public LocaleTable(Composite parent, BasicIS data, Type type, FormToolkit toolkit) {
		this.parent = parent;
		this.data = data;
		this.type = type;
		
		// Create grid for storing the table and buttons
		outerCompo = toolkit.createComposite(parent);
		outerCompo.setLayout(new GridLayout(2, false));
		
		// Set layout so that the table uses the whole width
		GridData tableLayout = new GridData();
		tableLayout.horizontalAlignment = GridData.FILL;
		tableLayout.verticalAlignment = GridData.FILL;
		tableLayout.grabExcessHorizontalSpace = true;
		tableLayout.grabExcessVerticalSpace = true;

		// Workaround for SWT-Bug
		// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
		tableLayout.widthHint = 1;
		
		Composite tableCompo = toolkit.createComposite(outerCompo);
		tableCompo.setLayoutData(tableLayout);
		TableColumnLayout columnLayout = new TableColumnLayout();
		tableCompo.setLayout(columnLayout);
		
		tableViewer = new TableViewer(tableCompo, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(tableLayout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		createColumns(columnLayout);
		
		// Add keyboard navigation
		ColumnViewerEditorActivationStrategy activationStrategy = new ColumnViewerEditorActivationStrategy(tableViewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {				
				// Editing is enabled as before but also by pressing enter
				boolean activateByKey = event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED
						&& event.keyCode == KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.ENTER_NAME);
				return super.isEditorActivationEvent(event) || activateByKey;
			}
				
		};
		
		activationStrategy.setEnableEditorActivationWithKeyboard(true);
			
		FocusCellHighlighter highlighter = new FocusCellOwnerDrawHighlighter(tableViewer);
		TableViewerFocusCellManager focusManager = new TableViewerFocusCellManager(tableViewer, highlighter);
		
		TableViewerEditor.create(tableViewer, focusManager, activationStrategy, ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
				
		
		tableViewer.setContentProvider(new ListContentProvider());
		if (type == Type.NAME) {
			tableViewer.setInput(data.getNames());
		} else {
			tableViewer.setInput(data.getDescriptions());
		}
		
		// Add buttons
		createButtons(toolkit);
		
		
	}
	
	/**
	 * Create the add and remove button
	 * @param toolkit
	 */
	private void createButtons(FormToolkit toolkit) {
		Composite buttonCompo = toolkit.createComposite(outerCompo);
		buttonCompo.setLayout(new FillLayout(SWT.VERTICAL));
		GridData buttonLayout = new GridData();
		buttonLayout.verticalAlignment = SWT.BEGINNING;
		buttonCompo.setLayoutData(buttonLayout);
		
		Button addButton = toolkit.createButton(buttonCompo, "Add", SWT.PUSH);
		final Button removeButton = toolkit.createButton(buttonCompo, "Remove", SWT.PUSH);
		removeButton.setEnabled(false);
		
		addButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (type == Type.NAME) {
					Name name = new Name();
					name.setLocale(new Locale("-"));
					name.setName("-");
					data.addName(name);
				} else {
					Description desc = new Description();
					desc.setLocale(new Locale("-"));
					desc.setDescription("-");
					data.addDescription(desc);
				}
				refresh();
				
			}
		});
		
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sl = (StructuredSelection)tableViewer.getSelection();

				if (type == Type.NAME) {
					data.removeName((Name)sl.getFirstElement());
				} else {
					data.removeDescription((Description)sl.getFirstElement());
				}
				refresh();
				
			}
		});
		
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				removeButton.setEnabled(!event.getSelection().isEmpty());
				
			}
		});
	}
	
	/**
	 * Creates the table's columns
	 * @param columnLayout
	 */
	private void createColumns(TableColumnLayout columnLayout) {
		
		ColumnLabelProvider localeLabel = new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AbstractLocale) element).getLocale().getLanguage();
			}
		};
		
		EditingSupport localeEditing = new LocaleEditingSupport(tableViewer) {
			
			@Override
			protected Object getValue(Object element) {
				return ((AbstractLocale)element).getLocale().getLanguage();
			}
			
			@Override
			protected void setValue(Object element, Object value) {
				AbstractLocale al = ((AbstractLocale)element);
				String input = ((String)value).toLowerCase();
				
				// Mark as dirty, when the locale has been edited
				if (!al.getLocale().getLanguage().equals(input)) {
					Locale locale = new Locale(input);
					((AbstractLocale)element).setLocale(locale);
					dirty = true;
					tableViewer.update(element, null);
				}
			}
		};
		
		buildColumn("Locale", 100, localeLabel, localeEditing, columnLayout);
		
		ColumnLabelProvider valueLabel = new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (type == Type.NAME) {
					return ((Name)element).getName();
				} else {
					return ((Description)element).getDescription();
				}
			}
		};
		
		EditingSupport valueEditing = new TextEditingSupport(tableViewer) {

			@Override
			protected Object getValue(Object element) {
				if (type == Type.NAME) {
					return ((Name)element).getName();
				} else {
					return ((Description)element).getDescription();
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				String input = (String)value;
				
				if (type == Type.NAME) {
					Name name = (Name)element;
					if (!name.getName().equals(input)) {
						name.setName(input);
						dirty = true;
					}
				} else {
					Description desc = (Description)element;
					if (!desc.equals(desc)) {
						desc.setDescription(input);
						dirty = true;
					}
				}
			}
			
		};
		
		String valueTitle;
		if (type == Type.NAME) {
			valueTitle = "Name";
		} else {
			valueTitle = "Description";
		}
		
		buildColumn(valueTitle, 0, valueLabel, valueEditing, columnLayout);
	}
	
	/**
	 * Build a column using the given data
	 * @param text	Title of the column
	 * @param width	Width of the column in pixel. 
	 * 				If set to 0 or a negative value, the column will use all available width
	 * @param labelProvider
	 * @param editingSupport
	 * @param columnLayout
	 */
	private void buildColumn(String text, int width, CellLabelProvider labelProvider, EditingSupport editingSupport, TableColumnLayout columnLayout) {
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.BORDER);
		TableColumn control = column.getColumn();
		if (width <= 0) {
			columnLayout.setColumnData(control,	new ColumnWeightData(1,true));			
		} else {
			columnLayout.setColumnData(control,	new ColumnPixelData(width,true));
		}
		control.setText(text);
		column.setLabelProvider(labelProvider);
		column.setEditingSupport(editingSupport);
		
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public Composite getComposite() {
		return outerCompo;
	}
	
	/**
	 * Refreshes the table so that current values from the model will be shown
	 */
	public void refresh() {
		tableViewer.refresh();
	//parent.getParent().getParent().getParent().getParent().layout(true, true);
	
	}

}
