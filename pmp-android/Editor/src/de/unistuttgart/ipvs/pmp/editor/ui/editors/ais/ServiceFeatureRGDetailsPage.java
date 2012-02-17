package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import java.io.IOException;
import java.util.HashMap;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.RequiredPrivacySettingsDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.RequiredRGContentProvider;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Shows the table with the details to the resource groups
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureRGDetailsPage implements IDetailsPage {

    /**
     * ID of this page
     */
    public static final String ID = "ais_service_feature_required_rgs";

    /**
     * Given form
     */
    private IManagedForm form;

    /**
     * {@link Shell} of the parent
     */
    private Shell parentShell;

    /**
     * Required privacy setting {@link TableViewer}
     */
    private TableViewer psTableViewer;

    /**
     * Columns of the table
     */
    private TableColumn identifierColumn;
    private TableColumn valueColumn;

    private AISRequiredResourceGroup displayed;

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
	Section psSection = toolkit.createSection(parent, Section.CLIENT_INDENT
		| Section.TITLE_BAR);
	psSection.setText("Required Privacy Setting");
	psSection.setLayout(new GridLayout(1, false));
	psSection.setExpanded(true);
	psSection.setLayoutData(parentLayout);
	createPrivacySettingSectionAttributeToolbar(psSection);

	// Composite that is display in the description section
	Composite psComposite = toolkit.createComposite(psSection);
	psComposite.setLayout(new GridLayout(1, false));
	psComposite.setLayoutData(parentLayout);
	createRGTable(psComposite, toolkit);

	psSection.setClient(psComposite);
    }

    private TableViewer createRGTable(Composite parent, FormToolkit toolkit) {
	// Use grid layout so that the table uses the whole screen width
	final GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;
	layoutData.verticalAlignment = GridData.FILL;
	layoutData.grabExcessVerticalSpace = true;

	// Workaround for SWT-Bug needed
	// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
	layoutData.widthHint = 1;

	psTableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION
		| SWT.MULTI);
	psTableViewer.setContentProvider(new RequiredRGContentProvider());

	// The identifier column with the LabelProvider
	TableViewerColumn identifierViewerColumn = new TableViewerColumn(
		psTableViewer, SWT.NULL);
	identifierViewerColumn.getColumn().setText("Identifier");
	identifierViewerColumn.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((AISRequiredPrivacySetting) element).getIdentifier();
	    }

	    @Override
	    public Image getImage(Object element) {
		// Add the check if the entry is correct
		return null;
	    }
	});
	identifierColumn = identifierViewerColumn.getColumn();

	// The value column with the LabelProvider
	TableViewerColumn valueViewerColumn = new TableViewerColumn(
		psTableViewer, SWT.NULL);
	valueViewerColumn.getColumn().setText("Value");
	valueViewerColumn.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		return ((AISRequiredPrivacySetting) element).getValue();
	    }

	    @Override
	    public Image getImage(Object element) {
		return null;
	    }
	});

	valueColumn = valueViewerColumn.getColumn();

	// Define the table's view
	Table psTable = psTableViewer.getTable();
	psTable.setLayoutData(layoutData);
	psTable.setHeaderVisible(true);
	psTable.setLinesVisible(true);

	return psTableViewer;
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
	psTableViewer.getTable().setRedraw(false);

	// Get the selected service feature and set the name
	TreePath[] path = ((TreeSelection) selection).getPaths();
	displayed = (AISRequiredResourceGroup) path[0].getLastSegment();
	psTableViewer.setInput(displayed);

	// Pack all columns
	identifierColumn.pack();
	valueColumn.pack();

	psTableViewer.getTable().setRedraw(true);
	psTableViewer.getTable().redraw();
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

    /**
     * Adds the toolbar with the remove and add buttons to the description
     * section
     * 
     * @param section
     *            {@link Section} to set the toolbar
     */
    private void createPrivacySettingSectionAttributeToolbar(Section section) {
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
		RGIS resGroups = null;

		// Get the resource groups from the server
		try {
		    for (RGIS rgis : Model.getInstance().getRgisList()) {
			if (rgis.getIdentifier().equals(
				displayed.getIdentifier())) {
			    resGroups = rgis;
			}
		    }
		} catch (IOException e) {
		    MessageDialog.openError(
			    parentShell,
			    "Error",
			    "A error happend while downloading the Resource Groups from the server. \n\n "
				    + "The error message was: \n \""
				    + e.getMessage() + "\"");
		}

		// Build a custom RGIS with the privacy settings that are
		// not set yet
		RGIS customRGIS = resGroups;

		// Check if there are RGs from the server
		if (resGroups != null) {
		    HashMap<String, RGISPrivacySetting> privacySettings = new HashMap<String, RGISPrivacySetting>();

		    // Iterate through all available PSs
		    for (RGISPrivacySetting ps : resGroups.getPrivacySettings()) {
			privacySettings.put(ps.getIdentifier(), ps);
		    }

		    /*
		     * Iterate through the set of required Privacy Settings and
		     * delete the ones that are already added
		     */
		    for (AISRequiredPrivacySetting requiredPS : displayed
			    .getRequiredPrivacySettings()) {
			if (privacySettings.containsKey(requiredPS
				.getIdentifier())) {
			    customRGIS.removePrivacySetting(privacySettings
				    .get(requiredPS.getIdentifier()));
			}

		    }

		    // If there are some PS to add
		    if (!customRGIS.getPrivacySettings().isEmpty()) {
			
			// Open the dialog
			SelectionDialog dialog = new RequiredPrivacySettingsDialog(
				parentShell, customRGIS);
			dialog.open();
			
			// Get the results
			if (dialog.getResult().length > 0) {
			    
			    // Store them at the model
			    for (Object object : dialog.getResult()) {
				AISRequiredPrivacySetting rps = (AISRequiredPrivacySetting) object;
				displayed.addRequiredPrivacySetting(rps);
			    }
			    
			    // Update the view
			    psTableViewer.refresh();
			    psTableViewer.getTable().setRedraw(false);
			    identifierColumn.pack();
			    valueColumn.pack();
			    psTableViewer.getTable().redraw();
			    psTableViewer.getTable().setRedraw(true);
			    Model.getInstance().setAISDirty(true);
			}
			
			// There are no PS to add to this service feature
		    } else {
			MessageDialog
				.openInformation(parentShell,
					"No Privacy Settings to add",
					"You already added all Privacy Settings of this Resource Group");
		    }
		}

	    }
	};
	add.setToolTipText("Add a new required Privacy Setting for the Service Feature");

	// The remove action
	Action remove = new Action("Remove") {

	    @Override
	    public void run() {
		// TODO
	    }
	};
	remove.setToolTipText("Remove the selected required Privacy Setting");

	// Add the actions to the toolbar
	toolBarManager.add(add);
	toolBarManager.add(remove);

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
