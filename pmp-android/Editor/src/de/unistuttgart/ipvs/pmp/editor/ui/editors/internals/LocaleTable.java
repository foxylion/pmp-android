package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.LocalizedString;

public class LocaleTable {

	private IBasicIS data;
	private final Composite outerCompo;
	private final Type type;
	private final TableViewer tableViewer;
	private boolean dirty = false;
	private final ISetDirtyAction dirtyAction;

	public enum Type {
		NAME, DESCRIPTION
	};

	public LocaleTable(Composite parent, Type type, ISetDirtyAction dirtyAction,
			FormToolkit toolkit) {
		this(parent, null, type, dirtyAction, toolkit);
	}
	public LocaleTable(Composite parent, IBasicIS data, Type type, ISetDirtyAction dirtyAction,
			FormToolkit toolkit) {
		this.data = data;
		this.type = type;
		this.dirtyAction = dirtyAction;

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

		tableViewer = new TableViewer(tableCompo, SWT.BORDER
				| SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(tableLayout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createColumns(columnLayout);

		// Add keyboard navigation
		ColumnViewerEditorActivationStrategy activationStrategy = new ColumnViewerEditorActivationStrategy(
				tableViewer) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				// Editing is enabled as before but also by pressing enter
				boolean activateByKey = event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED
						&& event.keyCode == KeyLookupFactory.getDefault()
								.formalKeyLookup(IKeyLookup.ENTER_NAME);
				return super.isEditorActivationEvent(event) || activateByKey;
			}

		};

		activationStrategy.setEnableEditorActivationWithKeyboard(true);

		FocusCellHighlighter highlighter = new FocusCellOwnerDrawHighlighter(
				tableViewer);
		TableViewerFocusCellManager focusManager = new TableViewerFocusCellManager(
				tableViewer, highlighter);

		TableViewerEditor.create(tableViewer, focusManager, activationStrategy,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		// Add table sorter
		tableViewer.setSorter(new ViewerSorter());

		// But data into table if set
		tableViewer.setContentProvider(new ListContentProvider());
		setData(data);

		// Add buttons
		createButtons(toolkit);

	}

	/**
	 * Create the add and remove button
	 * 
	 * @param toolkit
	 */
	private void createButtons(FormToolkit toolkit) {
		Composite buttonCompo = toolkit.createComposite(outerCompo);
		buttonCompo.setLayout(new FillLayout(SWT.VERTICAL));
		GridData buttonLayout = new GridData();
		buttonLayout.verticalAlignment = SWT.BEGINNING;
		buttonCompo.setLayoutData(buttonLayout);

		Button addButton = toolkit.createButton(buttonCompo, "Add", SWT.PUSH);
		final Button removeButton = toolkit.createButton(buttonCompo, "Remove",
				SWT.PUSH);
		removeButton.setEnabled(false);

		addButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				LocalizedString ls = new LocalizedString();
				
				if (type == Type.NAME) {
					data.addName(ls);
				} else {
					data.addDescription(ls);
				}
				setDirty(true);
				refresh();

			}
		});

		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sl = (StructuredSelection) tableViewer
						.getSelection();

				if (type == Type.NAME) {
					data.removeName((LocalizedString) sl.getFirstElement());
				} else {
					data.removeDescription((LocalizedString) sl
							.getFirstElement());
				}
				setDirty(true);
				refresh();

			}
		});

		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						removeButton
								.setEnabled(!event.getSelection().isEmpty());

					}
				});
	}

	/**
	 * Creates the table's columns
	 * 
	 * @param columnLayout
	 */
	private void createColumns(TableColumnLayout columnLayout) {

		ColumnLabelProvider localeLabel = new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				LocalizedString ls = (LocalizedString) element;
				
				if (ls.getLocale() == null) {
					return "-";
				} else {
					return ls.getLocale().getLanguage();
				}
			}
		};

		EditingSupport localeEditing = new LocaleEditingSupport(tableViewer) {

			@Override
			protected Object getValue(Object element) {
				LocalizedString ls = (LocalizedString) element;
				
				if (ls.getLocale() == null) {
					return "";
				} else {
					return ls.getLocale().getLanguage();
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				LocalizedString ls = (LocalizedString) element;
				String input = ((String) value).toUpperCase();
				
				// Save input when input has changed
				if (ls.getLocale() == null && !input.isEmpty() ||
						ls.getLocale() != null && !ls.getLocale().getLanguage().equalsIgnoreCase(input)) {
					// Mark as dirty, when the locale has been edited
					Locale locale = new Locale(input);
					ls.setLocale(locale);
					setDirty(true);
					tableViewer.update(element, null);
				}
			}
		};

		TableViewerColumn localeColumn = buildColumn("Locale", 100,
				localeLabel, localeEditing, columnLayout);
		new ColumnViewerSorter(tableViewer, localeColumn) {

			@Override
			public int doCompare(Viewer viewer, Object e1, Object e2) {
				Locale locale1 = ((LocalizedString) e1).getLocale();
				Locale locale2 = ((LocalizedString) e2).getLocale();
				
				if (locale1 == null && locale2 == null) {
					return 0;
				}
				if (locale1 == null) {
					return -1;
				}
				if (locale2 == null) {
					return 1;
				}
				
				return locale1.getLanguage().compareToIgnoreCase(
						locale2.getLanguage());
			}

		};

		// Value column
		ColumnLabelProvider valueLabel = new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				LocalizedString ls = (LocalizedString) element;
				if (ls.getString().isEmpty()) {
					return "-";
				} else {
					return ls.getString();
				}
			}
		};

		EditingSupport valueEditing = new TextEditingSupport(tableViewer) {

			@Override
			protected Object getValue(Object element) {
				LocalizedString ls = (LocalizedString) element;
				return ls.getString();
			}

			@Override
			protected void setValue(Object element, Object value) {
				LocalizedString ls = ((LocalizedString) element);
				String input = (String) value;
				
				if (!ls.getString().equals(input)) {
					ls.setString(input);
					setDirty(true);
					tableViewer.update(element, null);
				}
			}

		};

		String valueTitle;
		if (type == Type.NAME) {
			valueTitle = "Name";
		} else {
			valueTitle = "Description";
		}

		TableViewerColumn valueColumn = buildColumn(valueTitle, 0, valueLabel,
				valueEditing, columnLayout);
		new ColumnViewerSorter(tableViewer, valueColumn) {

			@Override
			public int doCompare(Viewer viewer, Object e1, Object e2) {
				String value1, value2;
				value1 = ((LocalizedString) e1).getString();
				value2 = ((LocalizedString) e2).getString();
				return value1.compareToIgnoreCase(value2);
			}

		};
	}

	/**
	 * Build a column using the given data
	 * 
	 * @param text
	 *            Title of the column
	 * @param width
	 *            Width of the column in pixel. If set to 0 or a negative value,
	 *            the column will use all available width
	 * @param labelProvider
	 * @param editingSupport
	 * @param columnLayout
	 */
	private TableViewerColumn buildColumn(String text, int width,
			CellLabelProvider labelProvider, EditingSupport editingSupport,
			TableColumnLayout columnLayout) {
		TableViewerColumn column = new TableViewerColumn(tableViewer,
				SWT.BORDER);
		
		final TableColumn control = column.getColumn();
		if (width <= 0) {
			columnLayout.setColumnData(control, new ColumnWeightData(1, true));
		} else {
			columnLayout.setColumnData(control,
					new ColumnPixelData(width, true));
		}
		
		control.setText(text);
		column.setLabelProvider(labelProvider);
		column.setEditingSupport(editingSupport);

		return column;

	}
	
	public void setData(IBasicIS data) {
		this.data = data;
		if (data != null) {
			if (type == Type.NAME) {
				tableViewer.setInput(data.getNames());
			} else {
				tableViewer.setInput(data.getDescriptions());
			}
		}
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		dirtyAction.doSetDirty(dirty);
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
	}

}
