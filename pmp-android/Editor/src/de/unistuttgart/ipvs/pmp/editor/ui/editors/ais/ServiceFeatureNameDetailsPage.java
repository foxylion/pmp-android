package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
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
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.DescriptionContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.NameContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.ServiceFeatureDescriptionDialog;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;

/**
 * Shows the lists for the names and the descriptions of a service feature
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureNameDetailsPage implements IDetailsPage {

    /**
     * ID of this page
     */
    public static final String ID = "ais_service_feature_names";

    /**
     * Given form
     */
    private IManagedForm form;
    
    /**
     * {@link Shell} of the parent
     */
    private Shell parentShell;

    /**
     * {@link TableViewer} for the names table
     */
    private TableViewer nameTableViewer;

    /**
     * The columns of the names table
     */
    private TableColumn nameColumn;
    private TableColumn localeNameColumn;

    /**
     * {@link TableViewer} for the description table
     */
    private TableViewer descriptionTableViewer;

    /**
     * The columns of the names table
     */
    private TableColumn descColumn;
    private TableColumn localeDescColumn;

    /**
     * The displayed ServiceFeature
     */
    AISServiceFeature displayed;

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
	parentShell = parent.getShell();
	
	// Set parent's layout
	GridData parentLayout = new GridData();
	parentLayout.verticalAlignment = GridData.FILL;
	parentLayout.grabExcessVerticalSpace = true;
	parentLayout.horizontalAlignment = GridData.FILL;
	parentLayout.grabExcessHorizontalSpace = true;
	parent.setLayout(new GridLayout(1, false));

	// Attributes section
	FormToolkit toolkit = form.getToolkit();

	// The name section
	Section nameSection = toolkit.createSection(parent,
		Section.CLIENT_INDENT | Section.TITLE_BAR);
	nameSection.setText("Names");
	nameSection.setLayout(new GridLayout(1, false));
	nameSection.setExpanded(true);
	nameSection.setLayoutData(parentLayout);
	createNameSectionAttributeToolbar(nameSection);

	// The description section
	Section descriptionSection = toolkit.createSection(parent,
		Section.CLIENT_INDENT | Section.TITLE_BAR);
	descriptionSection.setText("Descriptions");
	descriptionSection.setLayout(new GridLayout(1, false));
	descriptionSection.setExpanded(true);
	descriptionSection.setLayoutData(parentLayout);

	// Composite that is display in the description section
	Composite descriptionComposite = toolkit
		.createComposite(descriptionSection);
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

    private TableViewer createNameTable(Composite parent, FormToolkit toolkit) {
	// Use grid layout so that the table uses the whole screen width
	final GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;
	layoutData.verticalAlignment = GridData.FILL;
	layoutData.grabExcessVerticalSpace = true;

	// Workaround for SWT-Bug needed
	// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
	layoutData.widthHint = 1;

	nameTableViewer = new TableViewer(parent, SWT.BORDER
		| SWT.FULL_SELECTION | SWT.MULTI);
	nameTableViewer.setContentProvider(new NameContentProvider());

	// The locale column with the LabelProvider
	TableViewerColumn localeColumn = new TableViewerColumn(nameTableViewer,
		SWT.NULL);
	localeColumn.getColumn().setText("Locale");
	localeColumn.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((Name) element).getLocale().toString();
	    }

	    @Override
	    public Image getImage(Object element) {
		// Add the check if the entry is correct
		return null;
	    }
	});

	this.localeNameColumn = localeColumn.getColumn();

	// The description column with the LabelProvider
	TableViewerColumn nameColumn = new TableViewerColumn(nameTableViewer,
		SWT.NULL);
	nameColumn.getColumn().setText("Name");
	nameColumn.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((Name) element).getName();
	    }
	});

	this.nameColumn = nameColumn.getColumn();

	// Define the table's view
	Table descriptionTable = nameTableViewer.getTable();
	descriptionTable.setLayoutData(layoutData);
	descriptionTable.setHeaderVisible(true);
	descriptionTable.setLinesVisible(true);

	return nameTableViewer;
    }

    public TableViewer createDescriptionTable(Composite parent,
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

	descriptionTableViewer = new TableViewer(parent, SWT.BORDER
		| SWT.FULL_SELECTION | SWT.MULTI);
	descriptionTableViewer
		.setContentProvider(new DescriptionContentProvider());

	// The locale column with the LabelProvider
	TableViewerColumn localeColumn = new TableViewerColumn(
		descriptionTableViewer, SWT.NULL);
	localeColumn.getColumn().setText("Locale");
	localeColumn.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((Description) element).getLocale().toString();
	    }

	    @Override
	    public Image getImage(Object element) {
		// Add the check if the entry is correct
		return null;
	    }
	});

	this.localeDescColumn = localeColumn.getColumn();

	// The name column with the LabelProvider
	TableViewerColumn descColumn = new TableViewerColumn(
		descriptionTableViewer, SWT.NULL);
	descColumn.getColumn().setText("Description");
	descColumn.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((Description) element).getDescription();
	    }
	});

	this.descColumn = descColumn.getColumn();

	// Define the table's view
	Table descriptionTable = descriptionTableViewer.getTable();
	descriptionTable.setLayoutData(layoutData);
	descriptionTable.setHeaderVisible(true);
	descriptionTable.setLinesVisible(true);

	return descriptionTableViewer;
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
	// Disable redrawing of the table
	nameTableViewer.getTable().setRedraw(false);
	descriptionTableViewer.getTable().setRedraw(false);

	// Get the selected service feature and set the name
	TreePath[] path = ((TreeSelection) selection).getPaths();
	displayed = (AISServiceFeature) path[0].getFirstSegment();
	nameTableViewer.setInput(displayed);
	localeNameColumn.pack();
	nameColumn.pack();

	// Set the description
	descriptionTableViewer.setInput(displayed);
	descColumn.pack();
	localeDescColumn.pack();

	// Enable redrawing
	nameTableViewer.getTable().setRedraw(true);
	descriptionTableViewer.getTable().setRedraw(true);
    }
    
    /**
     * Adds the toolbar with the remove and add buttons to the given section
     * 
     * @param section
     *            {@link Section} to set the toolbar
     */
    private void createNameSectionAttributeToolbar(Section section) {
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
			parentShell, null, null, values, "Name");
		if (dialog.open() == Window.OK) {
		    nameTableViewer.getTable().setRedraw(false);

		    // Get the values
		    String stringLocale = values.get("locale");
		    String stringName = values.get("Name");

		    // Create the things for the model
		    Locale locale = new Locale(stringLocale);

		    Name name = new Name();
		    name.setLocale(locale);
		    name.setName(stringName);

		    displayed.addName(name);

		    Model.getInstance().setAISDirty(true);
		    nameTableViewer.refresh();
		    
		    nameColumn.pack();
		    localeNameColumn.pack();
		    
		    nameTableViewer.getTable().redraw();
		    nameTableViewer.getTable().setRedraw(true);		    
		}
	    }
	};
	add.setToolTipText("Add a new name for the Service Feature");

	// The remove action
	Action remove = new Action("Remove") {

	    @Override
	    public void run() {
		nameTableViewer.getTable().setRedraw(false);
		int[] selections = nameTableViewer.getTable().getSelectionIndices();

		// Delete it reverse out of the model
		for (int itr = selections.length - 1; itr >= 0; itr--) {
		    displayed.getDescriptions().remove(selections[itr]);
		    displayed.getNames().remove(selections[itr]);
		}
		nameColumn.pack();
		localeNameColumn.pack();

		nameTableViewer.getTable().setRedraw(true);
		nameTableViewer.refresh();
		Model.getInstance().setAISDirty(true);
	    }
	};
	remove.setToolTipText("Remove the selected names");

	// Add the actions to the toolbar
	toolBarManager.add(add);
	toolBarManager.add(remove);

	toolBarManager.update(true);
	section.setTextClient(toolbar);
    }

}
