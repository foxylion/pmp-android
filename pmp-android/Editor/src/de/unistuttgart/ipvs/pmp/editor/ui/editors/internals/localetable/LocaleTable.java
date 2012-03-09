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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.fieldassist.ControlDecoration;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.tooltips.TooltipTableListener;
import de.unistuttgart.ipvs.pmp.editor.xml.IssueTranslator;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.LocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

public class LocaleTable {

    private IBasicIS data;
    private final Composite outerCompo;
    private final Type type;
    private final TableViewer tableViewer;
    private boolean dirty = false;
    private final ILocaleTableAction tableAction;
    private ControlDecoration tableDec;

    public enum Type {
	NAME, DESCRIPTION, CHANGE_DESCRIPTION
    };

    /**
     * See information and warning in description of {@link
     * LocaleTable(Composite parent, IBasicIS data, Type type, ISetDirtyAction
     * dirtyAction, FormToolkit toolkit)}
     * 
     * @param parent
     * @param type
     * @param tableAction
     * @param toolkit
     */
    public LocaleTable(Composite parent, Type type,
	    ILocaleTableAction tableAction, FormToolkit toolkit) {
	this(parent, null, type, tableAction, toolkit);
    }

    /**
     * Creates a new locale table. This table shows all available names,
     * descriptions or change descriptions and their locale.
     * 
     * @param parent
     *            Parent widget, to which this table should be added
     * @param data
     *            Data, from which the information will be loaded and written
     *            into
     * @param type
     *            Type of data, that should be shown.<br />
     *            <b>Warning:</b> If type is set to {@code CHANGE_DESCRIPTION},
     *            {@code data} has to of type {@code RGISPrivacySetting}.
     *            Otherwise this table will throw {@code ClassCastException}s
     *            while user interaction!
     * @param tableAction
     *            Action, that will be initated when the data in this table was
     *            changed by the user
     * @param toolkit
     *            SWT-Toolkit
     */
    public LocaleTable(Composite parent, IBasicIS data, Type type,
	    ILocaleTableAction tableAction, FormToolkit toolkit) {
	this.data = data;
	this.type = type;
	this.tableAction = tableAction;

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

	// Add decoration
	tableDec = new ControlDecoration(tableViewer.getControl(), SWT.TOP
		| SWT.LEFT);
	tableDec.setImage(Images.IMG_DEC_FIELD_ERROR);
	if (data != null) {
	    validate();
	}

	// Add keyboard navigation
	ColumnViewerEditorActivationStrategy activationStrategy = new ColumnViewerEditorActivationStrategy(
		tableViewer) {
	    @Override
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

	// Add tooltip listener
	TooltipTableListener tooltipListener = new TooltipTableListener(
		tableViewer, parent.getShell());
	tableViewer.getTable().addListener(SWT.Dispose, tooltipListener);
	tableViewer.getTable().addListener(SWT.KeyDown, tooltipListener);
	tableViewer.getTable().addListener(SWT.MouseMove, tooltipListener);
	tableViewer.getTable().addListener(SWT.MouseHover, tooltipListener);

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
	addButton.setImage(Images.IMG_OBJ_ADD);
	final Button removeButton = toolkit.createButton(buttonCompo, "Remove",
		SWT.PUSH);
	removeButton.setEnabled(false);
	removeButton.setImage(Images.IMG_ETOOL_DELETE);

	addButton.addSelectionListener(new SelectionAdapter() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {

		LocalizedString ls = new LocalizedString();

		switch (type) {
		case NAME:
		    data.addName(ls);
		    break;
		case DESCRIPTION:
		    data.addDescription(ls);
		    break;
		case CHANGE_DESCRIPTION:
		    ((IRGISPrivacySetting) data).addChangeDescription(ls);
		    break;
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

		LocalizedString selection = (LocalizedString) sl
			.getFirstElement();

		switch (type) {
		case NAME:
		    data.removeName(selection);
		    break;
		case DESCRIPTION:
		    data.removeDescription(selection);
		    break;
		case CHANGE_DESCRIPTION:
		    ((IRGISPrivacySetting) data)
			    .removeChangeDescription(selection);
		    break;
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
	// Locale column
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

	    @Override
	    public Image getImage(Object element) {
		LocalizedString ls = (LocalizedString) element;
		if (ls.getIssues().isEmpty()) {
		    return null;
		} else {
		    return Images.ERROR16;
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
		String input = (String) value;

		// Save input when input has changed
		if (ls.getLocale() == null
			&& !input.isEmpty()
			|| ls.getLocale() != null
			&& !ls.getLocale().getLanguage()
				.equalsIgnoreCase(input)) {
		    // Mark as dirty, when the locale has been edited
		    Locale locale = new Locale(input);
		    ls.setLocale(locale);
		    setDirty(true);
		    validate();
		    tableViewer.update(element, null);
		}
	    }
	};

	TableViewerColumn localeColumn = buildColumn("Locale", 50, localeLabel,
		localeEditing, columnLayout);
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
		if (ls.getString() == null || ls.getString().isEmpty()) {
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
		if (ls.getString() == null) {
		    return "";
		} else {
		    return ls.getString();
		}
	    }

	    @Override
	    protected void setValue(Object element, Object value) {
		LocalizedString ls = ((LocalizedString) element);
		String input = (String) value;

		if (ls.getString() == null || ls.getString() != null
			&& !ls.getString().equals(input)) {
		    ls.setString(input);
		    setDirty(true);
		    validate();
		    tableViewer.update(element, null);
		}
	    }

	};

	String valueTitle;
	switch (type) {
	case NAME:
	    valueTitle = "Name";
	    break;
	case DESCRIPTION:
	    valueTitle = "Description";
	    break;
	case CHANGE_DESCRIPTION:
	    valueTitle = "Change Description";
	    break;
	default:
	    valueTitle = "Undefined";
	}

	TableViewerColumn valueColumn = buildColumn(valueTitle, 0, valueLabel,
		valueEditing, columnLayout);
	new ColumnViewerSorter(tableViewer, valueColumn) {

	    @Override
	    public int doCompare(Viewer viewer, Object e1, Object e2) {
		String value1, value2;
		value1 = ((LocalizedString) e1).getString();
		value2 = ((LocalizedString) e2).getString();

		if (value1 == null && value2 == null) {
		    return 0;
		}

		if (value1 == null) {
		    return -1;
		}

		if (value2 == null) {
		    return 1;
		}

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
	    switch (type) {
	    case NAME:
		tableViewer.setInput(data.getNames());
		break;
	    case DESCRIPTION:
		tableViewer.setInput(data.getDescriptions());
		break;
	    case CHANGE_DESCRIPTION:
		tableViewer.setInput(((RGISPrivacySetting) data)
			.getChangeDescriptions());
		break;
	    }

	    validate();
	}
    }

    public void setDirty(boolean dirty) {
	this.dirty = dirty;
	tableAction.doSetDirty(dirty);
    }

    public boolean isDirty() {
	return dirty;
    }

    public Composite getComposite() {
	return outerCompo;
    }

    private void validate() {
	tableAction.doValidate();

	// Set decoration
	tableDec.hide();
	List<IIssue> dataIssues = data.getIssues();
	List<IIssue> tableIssues = new ArrayList<IIssue>();
	for (IIssue i : dataIssues) {
	    IssueType t = i.getType();
	    switch (type) {
	    case NAME:
		if (t == IssueType.NAME_LOCALE_EN_MISSING
			|| t == IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN) {
		    tableDec.show();
		    tableIssues.add(i);
		}
		break;

	    case DESCRIPTION:
		if (t == IssueType.DESCRIPTION_LOCALE_EN_MISSING
			|| t == IssueType.DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN) {
		    tableDec.show();
		    tableIssues.add(i);
		}
		break;

	    case CHANGE_DESCRIPTION:
		if (t == IssueType.CHANGE_DESCRIPTION_LOCALE_EN_MISSING
			|| t == IssueType.CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN) {
		    tableDec.show();
		    tableIssues.add(i);
		}
		break;
	    }
	}

	tableDec.setDescriptionText(new IssueTranslator()
		.translateIssues(tableIssues));
    }

    /**
     * Refreshes the table so that current values from the model will be shown
     */
    public void refresh() {
	validate();
	tableViewer.refresh();
    }

}
