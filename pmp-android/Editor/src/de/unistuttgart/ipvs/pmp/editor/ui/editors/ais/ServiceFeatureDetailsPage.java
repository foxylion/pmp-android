package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

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
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.ServiceFeatureDescriptionDialog;
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
     * The displayed description table
     */
    private Table descriptionTable;
    
    /**
     * The displayed name table
     */
    private Table nameTable;

    /**
     * The displayed section
     */
    Section descriptionSection;
    
    /**
     * The displayed section
     */
    Section nameSection;

    /**
     * The description Columns to resize
     */
    private TableColumn localeColumnDescription;
    private TableColumn descColumnDescription;
    
    /**
     * The name Columns to resize
     */
    private TableColumn localeColumnName;
    private TableColumn nameColumnName;

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
	parent.setLayout(new GridLayout(2, false));

	// Attributes section
	FormToolkit toolkit = form.getToolkit();
	
	// The name section
	nameSection = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR);
	nameSection.setText("Names");
	nameSection.setLayout(new GridLayout(1, false));
	nameSection.setExpanded(true);
	nameSection.setLayoutData(parentLayout);
	createSectionAttributeToolbar(nameSection);

	// The description section
	descriptionSection = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR);
	descriptionSection.setText("Descriptions");
	descriptionSection.setLayout(new GridLayout(1, false));
	descriptionSection.setExpanded(true);
	descriptionSection.setLayoutData(parentLayout);
	createSectionAttributeToolbar(descriptionSection);

	// The Resourcegroup section
	Section rgSection = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR);
	rgSection.setText("Resourcegroups");
	rgSection.setLayout(new GridLayout(1, false));
	rgSection.setExpanded(true);
	rgSection.setLayoutData(parentLayout);

	// Composite that is display in the description section
	Composite descriptionComposite = toolkit.createComposite(descriptionSection);
	descriptionComposite.setLayout(new GridLayout(1, false));
	descriptionComposite.setLayoutData(parentLayout);
	createDescriptionTable(descriptionComposite, toolkit);

	// Composite that is display in the name section
	Composite nameComposite = toolkit.createComposite(nameSection);
	nameComposite.setLayout(new GridLayout(1, false));
	nameComposite.setLayoutData(parentLayout);
	createNameTable(nameComposite, toolkit);
	
	// Set the composites
	descriptionSection.setClient(descriptionComposite);
	nameSection.setClient(nameComposite);
    }

    /**
     * Creates the attributes table
     * 
     * @param parent
     *            parent {@link Composite}
     * @param toolkit
     *            {@link FormToolkit}
     * @return the created {@link TableViewer}
     */
    private TableViewer createDescriptionTable(Composite parent,
	    FormToolkit toolkit) {
	// Use grid layout so that the table uses the whole screen width
	final GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;
	layoutData.verticalAlignment = GridData.FILL;
	layoutData.grabExcessVerticalSpace = true;

	// Workaround for SWT-Bug needed
	// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
	layoutData.widthHint = 1;

	TableViewer tv = new TableViewer(parent, SWT.BORDER
		| SWT.FULL_SELECTION | SWT.MULTI);
	tv.addDoubleClickListener(this);

	// Define the table's view
	descriptionTable = tv.getTable();
	descriptionTable.setLayoutData(layoutData);
	descriptionTable.setHeaderVisible(true);
	descriptionTable.setLinesVisible(true);

	localeColumnDescription = new TableColumn(descriptionTable, SWT.LEFT);
	descColumnDescription = new TableColumn(descriptionTable, SWT.LEFT);
	localeColumnDescription.setText("Locale");
	descColumnDescription.setText("Description");

	return tv;
    }
    
    /**
     * Creates the attributes table
     * 
     * @param parent
     *            parent {@link Composite}
     * @param toolkit
     *            {@link FormToolkit}
     * @return the created {@link TableViewer}
     */
    private TableViewer createNameTable(Composite parent,
	    FormToolkit toolkit) {
	// Use grid layout so that the table uses the whole screen width
	final GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;
	layoutData.verticalAlignment = GridData.FILL;
	layoutData.grabExcessVerticalSpace = true;

	// Workaround for SWT-Bug needed
	// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
	layoutData.widthHint = 1;

	TableViewer tv = new TableViewer(parent, SWT.BORDER
		| SWT.FULL_SELECTION | SWT.MULTI);
	tv.addDoubleClickListener(this);

	// Define the table's view
	nameTable = tv.getTable();
	nameTable.setLayoutData(layoutData);
	nameTable.setHeaderVisible(true);
	nameTable.setLinesVisible(true);

	localeColumnName = new TableColumn(nameTable, SWT.LEFT);
	nameColumnName = new TableColumn(nameTable, SWT.LEFT);
	localeColumnName.setText("Locale");
	nameColumnName.setText("Name");
	nameColumnName.pack();
	localeColumnName.pack();

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
	if (arg0 != null && selection != null) {
	    
	    // Stop redrawing the list
	    descriptionSection.setVisible(true);
	    descriptionTable.setRedraw(false);
	    nameTable.setRedraw(false);
	    
	    // Remove the old entries
	    descriptionTable.removeAll();
	    nameTable.removeAll();
	    
	    
	    // Get the selected service feature
	    TreePath[] path = ((TreeSelection) selection).getPaths();
	    AISServiceFeature serviceFeature = (AISServiceFeature) path[0]
		    .getFirstSegment();
	    displayed = serviceFeature;
	    if (serviceFeature instanceof AISServiceFeature) {
		for (Description desc : serviceFeature.getDescriptions()) {
		    Locale locale = desc.getLocale();
		    TableItem item = new TableItem(descriptionTable, SWT.NONE);
		    item.setText(new String[] { locale.toString(),
			    desc.getDescription() });
		}
		
		for (Name name : serviceFeature.getNames()){
		    Locale locale = name.getLocale();
		    TableItem item = new TableItem(nameTable, SWT.NONE);
		    item.setText(new String[] {locale.toString(), name.getName()});
		}
		
		// Pack all columns
		localeColumnDescription.pack();
		descColumnDescription.pack();
		nameColumnName.pack();
		localeColumnName.pack();
		
		nameTable.setRedraw(true);
		nameTable.redraw();
		descriptionTable.setRedraw(true);
		descriptionTable.redraw();
	    } else {
		descriptionSection.setVisible(false);
	    }
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
			parentShell, null, null, values, "Name", "Add");
		if (dialog.open() == Window.OK) {
		    descriptionTable.setRedraw(false);

		    // Get the values
		    String stringLocale = values.get("locale");
		    String desc = values.get("Name");

		    // Create the table item
		    TableItem item = new TableItem(descriptionTable, SWT.NONE);
		    item.setText(new String[] { stringLocale, desc });

		    // Create the things for the model
		    Locale locale = new Locale(stringLocale);

		    Description description = new Description();
		    description.setLocale(locale);
		    description.setDescription(desc);

		    displayed.addDescription(description);

		    localeColumnDescription.pack();
		    descColumnDescription.pack();

		    Model.getInstance().setAISDirty(true);
		    descriptionTable.setRedraw(true);
		    descriptionTable.redraw();
		}
	    }
	};
	add.setToolTipText("Add a new attributes for the Service Feature");

	// The remove action
	Action remove = new Action("Remove") {

	    @Override
	    public void run() {
		descriptionTable.setRedraw(false);
		int[] selections = descriptionTable.getSelectionIndices();

		// Delete it reverse out of the model
		for (int itr = selections.length - 1; itr >= 0; itr--) {
		    displayed.getDescriptions().remove(selections[itr]);
		    displayed.getNames().remove(selections[itr]);
		}

		for (TableItem item : descriptionTable.getSelection()) {
		    item.dispose();
		}

		localeColumnDescription.pack();
		descColumnDescription.pack();

		descriptionTable.setRedraw(true);
		descriptionTable.redraw();
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
	if (descriptionTable.getSelectionCount() == 1) {
	    int index = descriptionTable.getSelectionIndex();
	    TableItem selected = descriptionTable.getItem(index);
	    String oldLocale = selected.getText(0);
	    String oldDesc = selected.getText(1);

	    HashMap<String, String> values = new HashMap<String, String>();
	    ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
		    parentShell, oldLocale, oldDesc, values, "Description", "Change");
	    if (dialog.open() == Window.OK) {
		// Get the values
		String newLocale = values.get("locale");
		String newDesc = values.get("Description");
		if (newLocale.equals(oldLocale) && newDesc.equals(oldDesc)) {
		    // do nothing
		} else {
		    descriptionTable.setRedraw(false);

		    // Change it at the table
		    selected.setText(new String[] { newLocale, newDesc });

		    displayed.getDescriptions().get(index)
			    .setDescription(newDesc);
		    displayed.getDescriptions().get(index)
			    .setLocale(new Locale(newLocale));
		    displayed.getNames().get(index)
			    .setLocale(new Locale(newLocale));

		    localeColumnDescription.pack();
		    descColumnDescription.pack();

		    descriptionTable.setRedraw(true);
		    descriptionTable.redraw();

		    Model.getInstance().setAISDirty(true);
		}
	    }
	}
    }
}
