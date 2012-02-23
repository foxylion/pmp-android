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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.InputNotEmptyValidator;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.ServiceFeatureTreeProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.RequiredResourceGroupsDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.labelprovider.ServiceFeatureTreeLabelProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.TooltipTreeListener;
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
	IDoubleClickListener, SelectionListener {

    /**
     * The {@link TreeViewer} of this block
     */
    private static TreeViewer treeViewer;

    /**
     * {@link Shell} of the parent composite
     */
    private Shell parentShell;

    /**
     * The remove button
     */
    private Button removeButton;

    /**
     * The add resource group button
     */
    private Button addRGButton;

    /**
     * The model of this editor
     */
    private Model model = AisEditor.getModel();

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
	Section section = toolkit.createSection(parent,
		ExpandableComposite.CLIENT_INDENT
			| ExpandableComposite.TITLE_BAR);
	section.setText("Service Features");
	section.setExpanded(true);
	creatSectionToolbar(section);

	Composite compo = toolkit.createComposite(section);
	compo.setLayout(new GridLayout(2, false));

	// Add tree
	treeViewer = new TreeViewer(compo);
	treeViewer.setContentProvider(new ServiceFeatureTreeProvider());
	treeViewer.setLabelProvider(new ServiceFeatureTreeLabelProvider());
	treeViewer.setInput(model.getAis());

	// Add all buttons
	Composite rgButtonsComp = toolkit.createComposite(compo);
	rgButtonsComp.setLayout(new FillLayout(SWT.VERTICAL));
	GridData buttonLayout = new GridData();
	buttonLayout.verticalAlignment = SWT.BEGINNING;
	rgButtonsComp.setLayoutData(buttonLayout);
	Button addSFButton = toolkit.createButton(rgButtonsComp,
		"Add Service Feature", SWT.PUSH);
	addSFButton.addSelectionListener(this);
	addRGButton = toolkit.createButton(rgButtonsComp, "Add Resource Group",
		SWT.PUSH);
	addRGButton.addSelectionListener(this);
	addRGButton.setEnabled(false);
	removeButton = toolkit.createButton(rgButtonsComp, "Remove", SWT.PUSH);
	removeButton.setEnabled(false);
	removeButton.addSelectionListener(this);

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

	TooltipTreeListener tooltipListener = new TooltipTreeListener(
		treeViewer, parentShell);

	// Disable the normal tool tips
	treeViewer.getTree().setToolTipText("");

	treeViewer.getTree().addListener(SWT.Dispose, tooltipListener);
	treeViewer.getTree().addListener(SWT.KeyDown, tooltipListener);
	treeViewer.getTree().addListener(SWT.MouseMove, tooltipListener);
	treeViewer.getTree().addListener(SWT.MouseHover, tooltipListener);

	treeViewer.addDoubleClickListener(this);
	treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

	    @Override
	    public void selectionChanged(SelectionChangedEvent event) {
		managedForm.fireSelectionChanged(spart, event.getSelection());

		Tree tree = treeViewer.getTree();
		int selectionCount = tree.getSelectionCount();

		if (selectionCount > 0) {
		    removeButton.setEnabled(true);
		} else {
		    removeButton.setEnabled(false);
		}

		// Enable / disable the add rg button
		if (selectionCount == 1) {
		    addRGButton.setEnabled(true);
		} else {
		    addRGButton.setEnabled(false);
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
			model.setAISDirty(true);
			sf.setIdentifier(result);
			AISValidatorWrapper.getInstance()
				.validateServiceFeatures(model.getAis(), true);
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
			model.setAISDirty(true);
			rg.setMinRevision(result);

			AISValidatorWrapper.getInstance()
				.validateRequiredResourceGroup(rg, true);
			AISValidatorWrapper.getInstance()
				.validateServiceFeatures(model.getAis(), true);
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
	    @Override
	    public void widgetDisposed(DisposeEvent e) {
		if ((handCursor != null) && (handCursor.isDisposed() == false)) {
		    handCursor.dispose();
		}
	    }
	});

	// Picture can be added also to the actions
	Action refresh = new Action("Refresh Resource Group List from server") {

	    @Override
	    public void run() {
		AisEditor.getModel().updateRgisListWithJob(parentShell, true);
	    }
	};
	refresh.setToolTipText("Refresh the Resource Group list from the server");

	// Add the actions to the toolbar
	toolBarManager.add(refresh);

	toolBarManager.update(true);
	section.setTextClient(toolbar);
    }

    /**
     * Refreshs the tree
     */
    public static void refreshTree() {
	treeViewer.refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
     * .swt.events.SelectionEvent)
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
     * .events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent selectionEvent) {
	if (selectionEvent.getSource() instanceof Button) {
	    Button clicked = (Button) selectionEvent.getSource();

	    // Add SF was clicked
	    if (clicked.getText().equals("Add Service Feature")) {
		// Show the input dialog
		InputDialog dialog = new InputDialog(parentShell,
			"Add Service Feature",
			"Enter the identifier of the Service Feature", null,
			new InputNotEmptyValidator("Identifier"));

		if (dialog.open() == Window.OK) {
		    // Add the service feature and set the dirty flag
		    model.setAISDirty(true);
		    String result = dialog.getValue();
		    model.getAis().addServiceFeature(
			    new AISServiceFeature(result));
		    AISValidatorWrapper.getInstance().validateServiceFeatures(
			    model.getAis(), true);
		    treeViewer.refresh();
		}
	    }

	    // Remove was clicked
	    if (clicked.getText().equals("Remove")) {
		Tree tree = treeViewer.getTree();
		TreeItem[] selection = tree.getSelection();
		int selectionCount = tree.getSelectionCount();
		Boolean deleted = false;

		HashMap<AISServiceFeature, TreeItem> serviceFeaturesToDel = new HashMap<AISServiceFeature, TreeItem>();
		HashMap<AISRequiredResourceGroup, TreeItem> requiredResourceGroupsToDel = new HashMap<AISRequiredResourceGroup, TreeItem>();

		// Check all selections
		for (int itr = 0; itr < selectionCount; itr++) {
		    TreeItem item = selection[itr];
		    Object data = item.getData();

		    if (data instanceof AISServiceFeature) {
			serviceFeaturesToDel
				.put((AISServiceFeature) data, item);
		    }

		    if (data instanceof IAISRequiredResourceGroup) {
			requiredResourceGroupsToDel.put(
				(AISRequiredResourceGroup) data, item);
		    }
		}

		// First delete all Service Features
		for (AISServiceFeature toDel : serviceFeaturesToDel.keySet()) {
		    model.getAis().getServiceFeatures().remove(toDel);
		    serviceFeaturesToDel.get(toDel).dispose();
		    deleted = true;
		}

		// Delete all resource groups that are selected
		for (AISRequiredResourceGroup toDel : requiredResourceGroupsToDel
			.keySet()) {
		    TreeItem selected = requiredResourceGroupsToDel.get(toDel);
		    if (!selected.isDisposed()
			    && !selected.getParentItem().isDisposed()) {
			AISServiceFeature parentSf = (AISServiceFeature) selected
				.getParentItem().getData();
			parentSf.removeRequiredResourceGroup(toDel);
			selected.dispose();
			deleted = true;
		    }
		}

		// Mark the model as dirty, validate the SFs and refresh the
		// tree
		if (deleted) {
		    AISValidatorWrapper.getInstance().validateServiceFeatures(
			    AisEditor.getModel().getAis(), true);
		    AisEditor.getModel().setAISDirty(true);
		    treeViewer.refresh();
		}
	    }

	    // Add RG was clicked
	    if (clicked.getText().equals("Add Resource Group")) {
		List<RGIS> rgisList = null;
		rgisList = model.getRgisList(parentShell);

		if (rgisList != null) {
		    HashMap<String, RGIS> resGroups = new HashMap<String, RGIS>();

		    // Iterate through all set RGs of the Service Feature
		    Tree tree = treeViewer.getTree();
		    TreeItem[] selection = tree.getSelection();

		    AISServiceFeature sf = null;
		    if (selection[0].getData() instanceof AISServiceFeature) {
			sf = (AISServiceFeature) selection[0].getData();
		    } else {
			sf = (AISServiceFeature) tree.getSelection()[0]
				.getParentItem().getData();
		    }

		    // Iterate through all available RGs of the server
		    for (RGIS rgis : rgisList) {
			Boolean found = false;
			for (IAISRequiredResourceGroup sfRg : sf
				.getRequiredResourceGroups()) {
			    // Add the RGs that aren't already at the SF
			    if (rgis.getIdentifier().equals(
				    sfRg.getIdentifier())) {
				found = true;
				break;
			    }

			}

			// Didn't found the RG at the Service Feature
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
				sf.addRequiredResourceGroup(required);
				AISValidatorWrapper.getInstance()
					.validateRequiredResourceGroup(
						required, true);
			    }
			    model.setAISDirty(true);
			    AISValidatorWrapper.getInstance()
				    .validateServiceFeatures(model.getAis(),
					    true);
			    treeViewer.refresh();
			}
		    }
		}
	    }
	}
    }
}
