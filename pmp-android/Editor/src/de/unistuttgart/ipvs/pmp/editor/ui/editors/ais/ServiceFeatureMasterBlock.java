package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.InputNotEmptyValidator;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.ServiceFeatureTreeProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.RequiredResourceGroupsDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.labelprovider.ServiceFeatureTreeLabelProvider;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Represents the {@link MasterDetailsBlock} with a tree of all service features
 * and all containing required Resource Groups
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureMasterBlock extends MasterDetailsBlock implements
	IDoubleClickListener {

    /**
     * The {@link TreeViewer} of this block
     */
    private static TreeViewer treeViewer;

    /**
     * {@link Shell} of the parent composite
     */
    private Shell parentShell;

    /**
     * The remove action
     */
    private Action remove;

    /**
     * The add {@link AISRequiredResourceGroup} action
     */
    private Action addRG;

    /**
     * Id of this component
     */
    private String ID = "service_feature_masterblock";

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.
     * ui.forms.IManagedForm, org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createMasterPart(final IManagedForm managedForm,
	    Composite parent) {
	parentShell = parent.getShell();
	FormToolkit toolkit = managedForm.getToolkit();
	Section section = toolkit.createSection(parent, Section.CLIENT_INDENT
		| Section.TITLE_BAR);
	section.setText("Service Features");
	section.setExpanded(true);
	creatSectionToolbar(section);

	Composite compo = toolkit.createComposite(section);
	compo.setLayout(new GridLayout(1, false));

	// Add tree
	treeViewer = new TreeViewer(compo);
	treeViewer.setContentProvider(new ServiceFeatureTreeProvider());
	treeViewer.setLabelProvider(new ServiceFeatureTreeLabelProvider());
	treeViewer.setInput(Model.getInstance().getAis());

	GridData treeLayout = new GridData();
	treeLayout.verticalAlignment = GridData.FILL;
	treeLayout.grabExcessVerticalSpace = true;
	treeLayout.horizontalAlignment = GridData.FILL;
	treeLayout.grabExcessHorizontalSpace = true;

	// Set the layout
	treeViewer.getControl().setLayoutData(treeLayout);

	final SectionPart spart = new SectionPart(section);
	managedForm.addPart(spart);
	sashForm.setOrientation(SWT.HORIZONTAL);
	managedForm.reflow(true);

	// Add buttons
	Composite buttonCompo = toolkit.createComposite(compo);
	GridData buttonLayout = new GridData();
	buttonLayout.verticalAlignment = SWT.BEGINNING;
	buttonCompo.setLayout(new FillLayout(SWT.VERTICAL));
	buttonCompo.setLayoutData(buttonLayout);

	treeViewer.addDoubleClickListener(this);
	treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

	    @Override
	    public void selectionChanged(SelectionChangedEvent event) {
		managedForm.fireSelectionChanged(spart, event.getSelection());

		Tree tree = treeViewer.getTree();
		TreeItem[] selection = tree.getSelection();
		int selectionCount = tree.getSelectionCount();

		// Enable / disable the add rg button
		if (selectionCount == 1) {
		    TreeItem item = selection[0];
		    if (item.getData() instanceof AISServiceFeature) {
			addRG.setEnabled(true);
		    } else {
			addRG.setEnabled(false);
		    }
		} else {
		    addRG.setEnabled(false);
		}

		Boolean enableSF = true;
		Boolean enableRG = true;

		// Check if all selections are on the service features
		for (int itr = 0; itr < selectionCount; itr++) {
		    TreeItem item = selection[itr];
		    Object data = item.getData();
		    if (!(data instanceof AISServiceFeature)) {
			enableSF = false;
		    }

		    /*
		     * Enable the remove button only if there are only Service
		     * Features enabled or only required Resourcegroups
		     */
		    if (!(data instanceof AISRequiredResourceGroup)) {
			enableRG = false;
		    }
		}

		// If yes then enable the delete button
		if (enableSF ^ enableRG) {
		    remove.setEnabled(true);
		} else {
		    // Else disable it
		    remove.setEnabled(false);
		}
	    }
	});

	section.setClient(compo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse
     * .ui.forms.IManagedForm)
     */
    @Override
    protected void createToolBarActions(IManagedForm arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.
     * forms.DetailsPart)
     */
    @Override
    protected void registerPages(DetailsPart detailsPart) {
	detailsPart.registerPage(AISServiceFeature.class,
		new ServiceFeatureNameDetailsPage());
	detailsPart.registerPage(AISRequiredResourceGroup.class,
		new ServiceFeatureRGDetailsPage());
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
	if (treeViewer.getTree().getSelectionCount() == 1) {
	    Object data = treeViewer.getTree().getSelection()[0].getData();
	    if (data instanceof AISServiceFeature) {
		AISServiceFeature sf = ((AISServiceFeature) data);
		// Show the input dialog
		InputDialog dialog = new InputDialog(parentShell,
			"Change Service Feature",
			"Enter the new identifier of the Service Feature",
			sf.getIdentifier(), new InputNotEmptyValidator(
				"Identifier"));

		if (dialog.open() == Window.OK) {
		    String result = dialog.getValue();

		    // Name changed, model is dirty
		    if (!result.equals(sf.getIdentifier())) {

			// Change the service feature and set the dirty flag
			Model.getInstance().setAISDirty(true);
			sf.setIdentifier(result);
			AISValidatorWrapper.getInstance()
				.validateServiceFeature(sf, true);
			treeViewer.refresh();
		    }
		}
	    }

	    if (data instanceof AISRequiredResourceGroup) {
		AISRequiredResourceGroup rg = (AISRequiredResourceGroup) data;
		InputDialog dialog = new InputDialog(
			parentShell,
			"Change minimal Revision",
			"Change the minimal required revision of the Resource Group",
			rg.getMinRevision(), new InputNotEmptyValidator(
				"Minimal revision"));
		if (dialog.open() == Window.OK) {
		    String result = dialog.getValue();

		    // The revision has changed
		    if (!result.equals(rg.getMinRevision())) {

			// Change the service feature and set the dirty flag
			Model.getInstance().setAISDirty(true);
			rg.setMinRevision(result);

			AISValidatorWrapper.getInstance()
				.validateServiceFeatures(
					Model.getInstance().getAis(), true);
			AISValidatorWrapper.getInstance()
				.validateRequiredResourceGroup(rg, true);
			treeViewer.refresh();
		    }
		}
	    }
	}
    }

    /**
     * Adds the toolbar with the remove and add buttons to the given section
     * 
     * @param section
     *            {@link Section} to set the toolbar
     */
    private void creatSectionToolbar(Section section) {
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
	Action addSF = new Action("Add Service Feature") {

	    @Override
	    public void run() {
		// Show the input dialog
		InputDialog dialog = new InputDialog(parentShell,
			"Add Service Feature",
			"Enter the identifier of the Service Feature", null,
			new InputNotEmptyValidator("Identifier"));

		if (dialog.open() == Window.OK) {
		    // Add the service feature and set the dirty flag
		    Model.getInstance().setAISDirty(true);
		    String result = dialog.getValue();
		    Model.getInstance().getAis()
			    .addServiceFeature(new AISServiceFeature(result));
		    AISValidatorWrapper.getInstance().validateServiceFeatures(
			    Model.getInstance().getAis(), true);
		    treeViewer.refresh();
		}
	    }
	};
	addSF.setToolTipText("Add a new Service Feature");

	// Picture can be added also to the actions
	addRG = new Action("Add Resource Group") {

	    @Override
	    public void run() {
		List<RGIS> rgisList = null;
		try {
		    rgisList = Model.getInstance().getRgisList();
		} catch (IOException e) {
		    IStatus status = new Status(IStatus.ERROR, ID,
			    "See details", e);
		    ErrorDialog
			    .openError(
				    parentShell,
				    "Error",
				    "A error happend while downloading the Resource Groups from the server.",
				    status);
		}

		if (rgisList != null) {
		    HashMap<String, RGIS> resGroups = new HashMap<String, RGIS>();

		    // Iterate through all set RGs of the Service Feature
		    Tree tree = treeViewer.getTree();
		    TreeItem[] selection = tree.getSelection();
		    AISServiceFeature sf = (AISServiceFeature) selection[0]
			    .getData();

		    // Iterate through all available RGs of the server
		    for (RGIS rgis : rgisList) {
			Boolean found = false;
			for (IAISRequiredResourceGroup sfRg : sf
				.getRequiredResourceGroups()) {
			    // Add the RGs that arent already at the SF
			    if (rgis.getIdentifier().equals(
				    sfRg.getIdentifier())) {
				found = true;
				break;
			    }

			}

			// Didn't found the RG at the sf
			if (!found) {
			    resGroups.put(rgis.getIdentifier(), rgis);
			}
		    }

		    // No RGs to add
		    if (resGroups.isEmpty()) {
			MessageDialog
				.openInformation(parentShell,
					"No Resource Groups to add",
					"You already added all Resource Groups of that are available.");
		    } else {
			List<RGIS> list = new ArrayList<RGIS>(
				resGroups.values());
			RequiredResourceGroupsDialog dialog = new RequiredResourceGroupsDialog(
				parentShell, list);

			// Get the results
			if (dialog.open() == Window.OK
				&& dialog.getResult().length > 0) {

			    // Store them at the model
			    for (Object object : dialog.getResult()) {
				AISRequiredResourceGroup required = (AISRequiredResourceGroup) object;
				AISValidatorWrapper.getInstance()
					.validateRequiredResourceGroup(
						required, true);
				sf.addRequiredResourceGroup(required);
			    }
			    Model.getInstance().setAISDirty(true);
			    AISValidatorWrapper.getInstance()
				    .validateServiceFeatures(
					    Model.getInstance().getAis(), true);
			    treeViewer.refresh();
			}
		    }
		}
	    }
	};
	addRG.setToolTipText("Add a new Resource Group");
	addRG.setEnabled(false);

	// The remove action
	remove = new Action("Remove") {

	    @Override
	    public void run() {
		Tree tree = treeViewer.getTree();
		TreeItem[] selection = tree.getSelection();
		int selectionCount = tree.getSelectionCount();
		Boolean deleted = false;

		// Check all selections
		for (int itr = 0; itr < selectionCount; itr++) {
		    TreeItem item = selection[itr];
		    Object data = item.getData();

		    // Check the type of the data, ServiceFeature could be
		    // deleted directly
		    if (data instanceof AISServiceFeature) {
			Model.getInstance().getAis().getServiceFeatures()
				.remove(data);
			item.dispose();
			deleted = true;
		    }

		    // Remove the resource group
		    if (data instanceof IAISRequiredResourceGroup) {
			IAISRequiredResourceGroup rg = (IAISRequiredResourceGroup) data;
			TreeItem parent = item.getParentItem();
			AISServiceFeature parentSf = (AISServiceFeature) parent
				.getData();
			parentSf.removeRequiredResourceGroup(rg);
			item.dispose();
			deleted = true;
		    }

		}
		if (deleted) {
		    detailsPart.selectionChanged(null, null);
		    treeViewer.refresh();
		    AISValidatorWrapper.getInstance().validateServiceFeatures(
			    Model.getInstance().getAis(), true);
		    Model.getInstance().setAISDirty(true);

		}

	    }
	};
	remove.setToolTipText("Remove the selected Service Features");

	// Add the actions to the toolbar
	toolBarManager.add(addSF);
	toolBarManager.add(addRG);
	remove.setEnabled(false);
	toolBarManager.add(remove);

	toolBarManager.update(true);
	section.setTextClient(toolbar);
    }
    
    public static void refreshTree(){
	treeViewer.refresh();
    }
}
