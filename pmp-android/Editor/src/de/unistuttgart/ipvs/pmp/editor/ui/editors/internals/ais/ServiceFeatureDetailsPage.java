package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;

/**
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureDetailsPage implements IDetailsPage,
	IDoubleClickListener {

    /**
     * Given form
     */
    private IManagedForm form;

    /**
     * The displayed table
     */
    private Table table;

    /**
     * The Columns to resize
     */
    private TableColumn localeColumn;
    private TableColumn nameColumn;
    private TableColumn descColumn;

    /**
     * The {@link AISServiceFeature} that is currently displayed
     */
    private AISServiceFeature displayed;

    /**
     * {@link Shell} of the parent
     */
    private Shell parentShell;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm
     * )
     */
    @Override
    public void initialize(IManagedForm arg0) {
	form = arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createContents(Composite parent) {
	// Set parent's layout
	parentShell = parent.getShell();
	GridData parentLayout = new GridData();
	parentLayout.verticalAlignment = GridData.FILL;
	parentLayout.grabExcessVerticalSpace = true;
	parentLayout.horizontalAlignment = GridData.FILL;
	parentLayout.grabExcessHorizontalSpace = true;
	parent.setLayout(new GridLayout());

	// Attributes section
	FormToolkit toolkit = form.getToolkit();

	Section attributeSection = toolkit.createSection(parent,
		Section.TWISTIE | Section.TITLE_BAR);
	attributeSection.setText("Attributes");
	attributeSection.setLayout(new GridLayout(1, false));
	attributeSection.setExpanded(true);
	attributeSection.setLayoutData(parentLayout);
	createSectionAttributeToolbar(attributeSection);

	Section rgSection = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR);
	rgSection.setText("Resourcegroups");
	rgSection.setLayout(new GridLayout(2, false));
	rgSection.setExpanded(true);
	rgSection.setLayoutData(parentLayout);

	TableViewer tableViewer = createAttributesTable(attributeSection,
		toolkit);
	attributeSection.setClient(tableViewer.getControl());

    }

    private TableViewer createAttributesTable(Composite parent,
	    FormToolkit toolkit) {
	// Use grid layout so that the table uses the whole screen width
	final GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;

	// Workaround for SWT-Bug needed
	// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
	layoutData.widthHint = 1;

	TableViewer tv = new TableViewer(parent, SWT.BORDER
		| SWT.FULL_SELECTION);
	tv.addDoubleClickListener(this);

	// Define the table's view
	table = tv.getTable();
	table.setLayoutData(layoutData);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);

	localeColumn = new TableColumn(table, SWT.LEFT);
	nameColumn = new TableColumn(table, SWT.LEFT);
	descColumn = new TableColumn(table, SWT.LEFT);
	localeColumn.setText("Locale");
	nameColumn.setText("Name");
	descColumn.setText("Description");

	return tv;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
     */
    @Override
    public void commit(boolean arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#dispose()
     */
    @Override
    public void dispose() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#isDirty()
     */
    @Override
    public boolean isDirty() {
	return Model.getInstance().isAisDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#isStale()
     */
    @Override
    public boolean isStale() {
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#refresh()
     */
    @Override
    public void refresh() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#setFocus()
     */
    @Override
    public void setFocus() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
     */
    @Override
    public boolean setFormInput(Object arg0) {
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse
     * .ui.forms.IFormPart, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IFormPart arg0, ISelection selection) {
	table.setRedraw(false);
	table.removeAll();
	TreePath[] path = ((TreeSelection) selection).getPaths();
	AISServiceFeature serviceFeature = (AISServiceFeature) path[0]
		.getFirstSegment();
	displayed = serviceFeature;
	if (serviceFeature instanceof AISServiceFeature) {
	    for (Description desc : ((AISServiceFeature) serviceFeature)
		    .getDescriptions()) {
		Locale locale = desc.getLocale();
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {
			locale.toString(),
			((AISServiceFeature) serviceFeature)
				.getNameForLocale(locale),
			desc.getDescription() });
	    }
	    localeColumn.pack();
	    nameColumn.pack();
	    descColumn.pack();
	    table.setRedraw(true);
	    table.redraw();
	}
    }

    /**
     * Adds the toolbar with the remove and add buttons to the given section
     * 
     * @param section
     *            {@link Section} to set the toolbar
     */
    private void createSectionAttributeToolbar(Section section) {
	// Create the toolbar
	ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
	ToolBar toolbar = toolBarManager.createControl(section);

	final Cursor handCursor = new Cursor(Display.getCurrent(),
		SWT.CURSOR_HAND);
	toolbar.setCursor(handCursor);
	toolbar.addDisposeListener(new DisposeListener() {
	    public void widgetDisposed(DisposeEvent e) {
		if ((handCursor != null) && (handCursor.isDisposed() == false)) {
		    handCursor.dispose();
		}
	    }
	});

	// Picture can be added also to the actions
	Action add = new Action("Add") {

	    @Override
	    public void run() {
		HashMap<String, String> values = new HashMap<String, String>();
		ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
			parentShell, null, null, null, values);
		if (dialog.open() == Window.OK) {
		    table.setRedraw(false);

		    // Get the values
		    String name = values.get("name");
		    String stringLocale = values.get("locale");
		    String desc = values.get("description");

		    // Create the table item
		    TableItem item = new TableItem(table, SWT.NONE);
		    item.setText(new String[] { stringLocale, name, desc });

		    // Create the things for the model
		    Locale locale = new Locale(stringLocale);
		    Name aisName = new Name();
		    aisName.setLocale(locale);
		    aisName.setName(name);

		    Description description = new Description();
		    description.setLocale(locale);
		    description.setDescription(desc);

		    displayed.addDescription(description);
		    displayed.addName(aisName);

		    localeColumn.pack();
		    nameColumn.pack();
		    descColumn.pack();

		    Model.getInstance().setAISDirty(true);
		    table.setRedraw(true);
		    table.redraw();
		}
	    }
	};
	add.setToolTipText("Add a new attributes for the Service Feature");

	// The remove action
	Action remove = new Action("Remove") {

	    @Override
	    public void run() {
		table.setRedraw(false);
		for (int itemIndex : table.getSelectionIndices()) {
		    displayed.getDescriptions().remove(itemIndex);
		    displayed.getNames().remove(itemIndex);
		    table.getItem(itemIndex).dispose();
		}

		localeColumn.pack();
		nameColumn.pack();
		descColumn.pack();

		table.setRedraw(true);
		table.redraw();
		Model.getInstance().setAISDirty(true);
	    }
	};
	remove.setToolTipText("Remove the selected attributes");

	// Add the actions to the toolbar
	toolBarManager.add(add);
	toolBarManager.add(remove);

	toolBarManager.update(true);
	section.setTextClient(toolbar);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse
     * .jface.viewers.DoubleClickEvent)
     */
    @Override
    public void doubleClick(DoubleClickEvent arg0) {
	if (table.getSelectionCount() == 1) {
	    int index = table.getSelectionIndex();
	    TableItem selected = table.getItem(index);
	    String oldLocale = selected.getText(0);
	    String odlName = selected.getText(1);
	    String oldDesc = selected.getText(2);

	    HashMap<String, String> values = new HashMap<String, String>();
	    ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
		    parentShell, oldLocale, odlName, oldDesc, values);
	    if (dialog.open() == Window.OK) {
		// Get the values
		String newName = values.get("name");
		String newLocale = values.get("locale");
		String newDesc = values.get("description");
		if (newName.equals(odlName) && newLocale.equals(oldLocale)
			&& newDesc.equals(oldDesc)) {
		    // do nothing
		} else {
		    table.setRedraw(false);

		    // Change it at the table
		    selected.setText(new String[] { newLocale, newName, newDesc });

		    displayed.getDescriptions().get(index)
			    .setDescription(newDesc);
		    displayed.getDescriptions().get(index)
			    .setLocale(new Locale(newLocale));
		    displayed.getNames().get(index).setName(newName);
		    displayed.getNames().get(index)
			    .setLocale(new Locale(newLocale));

		    localeColumn.pack();
		    nameColumn.pack();
		    descColumn.pack();

		    table.setRedraw(true);
		    table.redraw();

		    Model.getInstance().setAISDirty(true);
		}
	    }
	}
    }
}
