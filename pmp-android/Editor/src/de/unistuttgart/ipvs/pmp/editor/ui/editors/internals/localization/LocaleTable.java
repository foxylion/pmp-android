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
import org.eclipse.ui.forms.widgets.FormToolkit;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.LocaleEditingSupport;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.TextEditingSupport;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.LocalizedString;

public class LocaleTable {

    private final IBasicIS data;
    private final Composite outerCompo;
    private final Type type;
    private final TableViewer tableViewer;
    private boolean dirty = false;

    public enum Type {
	NAME, DESCRIPTION
    };

    public LocaleTable(Composite parent, IBasicIS data, Type type,
	    FormToolkit toolkit) {
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

	// But data into table
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

		if (type == Type.NAME) {
		    LocalizedString name = new LocalizedString();
		    name.setLocale(new Locale("-"));
		    name.setString("-");
		    data.addName(name);
		} else {
		    LocalizedString desc = new LocalizedString();
		    desc.setLocale(new Locale("-"));
		    desc.setString("-");
		    data.addDescription(desc);
		}
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
		return ((LocalizedString) element).getLocale().getLanguage();
	    }
	};

	EditingSupport localeEditing = new LocaleEditingSupport(tableViewer) {

	    @Override
	    protected Object getValue(Object element) {
		return ((LocalizedString) element).getLocale().getLanguage();
	    }

	    @Override
	    protected void setValue(Object element, Object value) {
		LocalizedString al = ((LocalizedString) element);
		String input = ((String) value).toLowerCase();

		// Mark as dirty, when the locale has been edited
		if (!al.getLocale().getLanguage().equals(input)) {
		    Locale locale = new Locale(input);
		    ((LocalizedString) element).setLocale(locale);
		    dirty = true;
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
		return locale1.getLanguage().compareToIgnoreCase(
			locale2.getLanguage());
	    }

	};

	ColumnLabelProvider valueLabel = new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((LocalizedString) element).getString();
	    }
	};

	EditingSupport valueEditing = new TextEditingSupport(tableViewer) {

	    @Override
	    protected Object getValue(Object element) {
		return ((LocalizedString) element).getString();
	    }

	    @Override
	    protected void setValue(Object element, Object value) {
		String input = (String) value;

		if (type == Type.NAME) {
		    LocalizedString name = (LocalizedString) element;
		    if (!name.getString().equals(input)) {
			name.setString(input);
			dirty = true;
			tableViewer.update(element, null);
		    }
		} else {
		    LocalizedString desc = (LocalizedString) element;
		    if (!desc.equals(desc)) {
			desc.setString(input);
			dirty = true;
			tableViewer.update(element, null);
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

	// Add sorter
	/*
	 * control.addSelectionListener(new SelectionAdapter() { private int
	 * direction = SWT.NONE; public void widgetSelected(SelectionEvent e) {
	 * 
	 * switch (direction) { case SWT.NONE: direction = SWT.DOWN; break; case
	 * SWT.DOWN: direction = SWT.UP; break; case SWT.UP: direction =
	 * SWT.None; break;
	 * 
	 * } control.getParent().setSortColumn(control);
	 * control.getParent().setSortDirection(direction);
	 * tableViewer.setSorter(new ViewerSorter());
	 * tableViewer.setComparator(new ViewerComparator() { public int
	 * compare(Viewer viewer, Object e1, Object e2) {
	 * System.out.println("e1"+ e1); int compared = 0; switch (direction) {
	 * case SWT.NONE: compared = 0; break; case SWT.DOWN: break; case
	 * SWT.UP: compared *= -1; break; } System.out.println("compare");
	 * return compared; } }); System.out.println("click"); refresh(); } });
	 */

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
	// parent.getParent().getParent().getParent().getParent().layout(true,
	// true);

    }

}
