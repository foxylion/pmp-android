package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.DescriptionContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.NameContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.ServiceFeatureDescriptionDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.TooltipTableListener;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.common.LocalizedString;

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
     * Action of the remove description
     */
    private Action removeDesc;

    /**
     * The remove name action
     */
    private Action removeName;

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
	createDescriptionSectionAttributeToolbar(descriptionSection);

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

	// Disable the default tool tips
	nameTableViewer.getTable().setToolTipText("");

	TooltipTableListener tooltipListener = new TooltipTableListener(
		nameTableViewer, parentShell);

	nameTableViewer.getTable().addListener(SWT.Dispose, tooltipListener);
	nameTableViewer.getTable().addListener(SWT.KeyDown, tooltipListener);
	nameTableViewer.getTable().addListener(SWT.MouseMove, tooltipListener);
	nameTableViewer.getTable().addListener(SWT.MouseHover, tooltipListener);

	nameTableViewer.getTable().addSelectionListener(
		new SelectionListener() {

		    @Override
		    public void widgetSelected(SelectionEvent arg0) {
			if (nameTableViewer.getTable().getSelectionCount() > 0) {
			    removeName.setEnabled(true);
			} else {
			    removeName.setEnabled(false);
			}
		    }

		    @Override
		    public void widgetDefaultSelected(SelectionEvent arg0) {
		    }
		});

	// The locale column with the LabelProvider
	TableViewerColumn localeColumn = new TableViewerColumn(nameTableViewer,
		SWT.NULL);
	localeColumn.getColumn().setText("Locale");
	localeColumn.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((LocalizedString) element).getLocale().toString();
	    }

	    @Override
	    public Image getImage(Object element) {
		ILocalizedString item = (LocalizedString) element;
		if (!item.getIssues().isEmpty()) {
		    return Images.ERROR16;
		}
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
		return ((LocalizedString) element).getString();
	    }
	});

	this.nameColumn = nameColumn.getColumn();

	// Define the table's view
	Table descriptionTable = nameTableViewer.getTable();
	descriptionTable.setLayoutData(layoutData);
	descriptionTable.setHeaderVisible(true);
	descriptionTable.setLinesVisible(true);

	// Add the double click listener
	nameTableViewer.addDoubleClickListener(new IDoubleClickListener() {

	    @Override
	    public void doubleClick(DoubleClickEvent arg0) {
		if (nameTableViewer.getTable().getSelectionCount() == 1) {
		    LocalizedString oldName = (LocalizedString) nameTableViewer
			    .getTable().getSelection()[0].getData();

		    // Show the dialog
		    HashMap<String, String> values = new HashMap<String, String>();
		    ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
			    parentShell, oldName.getLocale().toString(),
			    oldName.getString(), values, "Name", "Change");
		    if (dialog.open() == Window.OK) {
			String newName = values.get("Name");
			String newLocale = values.get("locale");

			// Sth. has changed
			if (!(newName.equals(oldName.getString()) && newLocale
				.equals(oldName.getLocale().toString()))) {
			    nameTableViewer.getTable().setRedraw(false);

			    Locale locale = new Locale(newLocale);

			    oldName.setLocale(locale);
			    oldName.setString(newName);

			    Model.getInstance().setAISDirty(true);
			    AISValidatorWrapper.getInstance()
				    .validateServiceFeature(displayed, true);
			    ServiceFeatureMasterBlock.refreshTree();
			    nameTableViewer.refresh();

			    // Redraw the table
			    ServiceFeatureNameDetailsPage.this.nameColumn
				    .pack();
			    localeNameColumn.pack();

			    nameTableViewer.getTable().redraw();
			    nameTableViewer.getTable().setRedraw(true);
			}
		    }
		}

	    }
	});

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

	TooltipTableListener descTooltipListener = new TooltipTableListener(
		descriptionTableViewer, parentShell);

	// Disable the default tool tips
	descriptionTableViewer.getTable().setToolTipText("");

	descriptionTableViewer.getTable().addListener(SWT.Dispose,
		descTooltipListener);
	descriptionTableViewer.getTable().addListener(SWT.KeyDown,
		descTooltipListener);
	descriptionTableViewer.getTable().addListener(SWT.MouseMove,
		descTooltipListener);
	descriptionTableViewer.getTable().addListener(SWT.MouseHover,
		descTooltipListener);

	descriptionTableViewer
		.setContentProvider(new DescriptionContentProvider());

	// The locale column with the LabelProvider
	TableViewerColumn localeColumn = new TableViewerColumn(
		descriptionTableViewer, SWT.NULL);
	localeColumn.getColumn().setText("Locale");
	localeColumn.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((LocalizedString) element).getLocale().toString();
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
		return ((LocalizedString) element).getString();
	    }
	});

	this.descColumn = descColumn.getColumn();

	// Define the table's view
	Table descriptionTable = descriptionTableViewer.getTable();
	descriptionTable.setLayoutData(layoutData);
	descriptionTable.setHeaderVisible(true);
	descriptionTable.setLinesVisible(true);

	descriptionTable.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent arg0) {
		if (descriptionTableViewer.getTable().getSelectionCount() > 0) {
		    removeDesc.setEnabled(true);
		} else {
		    removeDesc.setEnabled(false);
		}
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent arg0) {
	    }
	});

	// Add the double click listener
	descriptionTableViewer
		.addDoubleClickListener(new IDoubleClickListener() {

		    @Override
		    public void doubleClick(DoubleClickEvent arg0) {
			if (descriptionTableViewer.getTable()
				.getSelectionCount() == 1) {
			    LocalizedString oldDesc = (LocalizedString) descriptionTableViewer
				    .getTable().getSelection()[0].getData();

			    // Show the dialog
			    HashMap<String, String> values = new HashMap<String, String>();
			    ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
				    parentShell,
				    oldDesc.getLocale().toString(), oldDesc
					    .getString(), values,
				    "Description", "Change");
			    if (dialog.open() == Window.OK) {
				String newDesc = values.get("Description");
				String newLocale = values.get("locale");

				// Sth. has changed
				if (!(newDesc.equals(oldDesc.getString()) && newLocale
					.equals(oldDesc.getLocale().toString()))) {
				    descriptionTableViewer.getTable()
					    .setRedraw(false);

				    Locale locale = new Locale(newLocale);

				    oldDesc.setLocale(locale);
				    oldDesc.setString(newDesc);

				    Model.getInstance().setAISDirty(true);
				    AISValidatorWrapper.getInstance()
					    .validateServiceFeature(displayed,
						    true);
				    ServiceFeatureMasterBlock.refreshTree();

				    descriptionTableViewer.refresh();

				    // Redraw the table
				    ServiceFeatureNameDetailsPage.this.descColumn
					    .pack();
				    localeDescColumn.pack();

				    descriptionTableViewer.getTable()
					    .setRedraw(true);
				    descriptionTableViewer.getTable().redraw();
				}
			    }
			}
		    }
		});

	return descriptionTableViewer;
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

	/*
	 * Disable the remove buttons because if the selection was changed no
	 * line is selected
	 */
	removeName.setEnabled(false);
	removeDesc.setEnabled(false);

	// Set the description
	descriptionTableViewer.setInput(displayed);
	descColumn.pack();
	localeDescColumn.pack();

	// Enable redrawing
	nameTableViewer.getTable().setRedraw(true);
	descriptionTableViewer.getTable().setRedraw(true);
    }

    /**
     * Adds the toolbar with the remove and add buttons to the name section
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
			parentShell, null, null, values, "Name", "Add");
		if (dialog.open() == Window.OK) {
		    nameTableViewer.getTable().setRedraw(false);

		    // Get the values
		    String stringLocale = values.get("locale");
		    String stringName = values.get("Name");

		    // Create the things for the model
		    Locale locale = new Locale(stringLocale);

		    LocalizedString name = new LocalizedString();
		    name.setLocale(locale);
		    name.setString(stringName);

		    displayed.addName(name);

		    Model.getInstance().setAISDirty(true);
		    AISValidatorWrapper.getInstance().validateServiceFeature(
			    displayed, true);
		    ServiceFeatureMasterBlock.refreshTree();

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
	removeName = new Action("Remove") {

	    @Override
	    public void run() {
		nameTableViewer.getTable().setRedraw(false);
		TableItem[] selection = nameTableViewer.getTable()
			.getSelection();

		// Delete it out of the model
		for (TableItem item : selection) {
		    displayed.removeName((LocalizedString) item.getData());
		}
		nameColumn.pack();
		localeNameColumn.pack();
		AISValidatorWrapper.getInstance().validateServiceFeature(
			displayed, true);
		ServiceFeatureMasterBlock.refreshTree();

		nameTableViewer.getTable().setRedraw(true);
		nameTableViewer.refresh();
		Model.getInstance().setAISDirty(true);
	    }
	};
	removeName.setToolTipText("Remove the selected names");
	removeName.setEnabled(false);

	// Add the actions to the toolbar
	toolBarManager.add(add);
	toolBarManager.add(removeName);

	toolBarManager.update(true);
	section.setTextClient(toolbar);
    }

    /**
     * Adds the toolbar with the remove and add buttons to the description
     * section
     * 
     * @param section
     *            {@link Section} to set the toolbar
     */
    private void createDescriptionSectionAttributeToolbar(Section section) {
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
			parentShell, null, null, values, "Description", "Add");
		if (dialog.open() == Window.OK) {
		    descriptionTableViewer.getTable().setRedraw(false);

		    // Get the values
		    String stringLocale = values.get("locale");
		    String stringDesc = values.get("Description");

		    // Create the things for the model
		    Locale locale = new Locale(stringLocale);

		    LocalizedString desc = new LocalizedString();
		    desc.setLocale(locale);
		    desc.setString(stringDesc);

		    displayed.addDescription(desc);

		    Model.getInstance().setAISDirty(true);
		    AISValidatorWrapper.getInstance().validateServiceFeature(
			    displayed, true);
		    ServiceFeatureMasterBlock.refreshTree();

		    descriptionTableViewer.refresh();

		    descColumn.pack();
		    localeDescColumn.pack();

		    descriptionTableViewer.getTable().redraw();
		    descriptionTableViewer.getTable().setRedraw(true);
		}
	    }
	};
	add.setToolTipText("Add a new description for the Service Feature");

	// The remove action
	removeDesc = new Action("Remove") {

	    @Override
	    public void run() {
		descriptionTableViewer.getTable().setRedraw(false);
		TableItem[] selections = descriptionTableViewer.getTable()
			.getSelection();

		// Delete it out of the model
		for (TableItem item : selections) {
		    displayed.removeDescription((LocalizedString) item
			    .getData());
		}

		descColumn.pack();
		localeDescColumn.pack();

		descriptionTableViewer.getTable().setRedraw(true);
		AISValidatorWrapper.getInstance().validateServiceFeature(
			displayed, true);
		ServiceFeatureMasterBlock.refreshTree();

		descriptionTableViewer.refresh();
		Model.getInstance().setAISDirty(true);
	    }
	};
	removeDesc.setToolTipText("Remove the selected descriptions");
	removeDesc.setEnabled(false);

	// Add the actions to the toolbar
	toolBarManager.add(add);
	toolBarManager.add(removeDesc);

	toolBarManager.update(true);
	section.setTextClient(toolbar);
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
}
