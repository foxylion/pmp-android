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
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.ImageDescriptor;
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

import de.unistuttgart.ipvs.pmp.editor.model.AisModel;
import de.unistuttgart.ipvs.pmp.editor.model.DownloadedRGModel;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.ServiceFeatureTreeProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.RequiredResourceGroupsDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.labelprovider.ServiceFeatureTreeLabelProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.tooltips.TooltipTreeListener;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.editor.xml.IssueTranslator;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Represents the {@link MasterDetailsBlock} with a tree of all service features
 * and all containing required Resource Groups
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureMasterBlock extends MasterDetailsBlock implements SelectionListener {
    
    /**
     * The {@link TreeViewer} of this block
     */
    private TreeViewer treeViewer;
    
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
     * Error decoration of the tree
     */
    private ControlDecoration treeDec;
    
    /**
     * The model of this editor
     */
    private final AisModel model;
    
    
    /**
     * Constructor to get the model instance
     * 
     * @param model
     *            {@link AisModel} of this {@link AisEditor} instance
     */
    public ServiceFeatureMasterBlock(AisModel model) {
        this.model = model;
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.
     * ui.forms.IManagedForm, org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
        this.parentShell = parent.getShell();
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = toolkit.createSection(parent, ExpandableComposite.CLIENT_INDENT
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
        treeViewer.setInput(this.model.getAis());
        
        // Add decoration
        this.treeDec = new ControlDecoration(treeViewer.getControl(), SWT.TOP | SWT.LEFT);
        this.treeDec.setImage(Images.IMG_DEC_FIELD_ERROR);
        
        validate();
        
        // Add all buttons
        Composite rgButtonsComp = toolkit.createComposite(compo);
        rgButtonsComp.setLayout(new FillLayout(SWT.VERTICAL));
        GridData buttonLayout = new GridData();
        buttonLayout.verticalAlignment = SWT.BEGINNING;
        rgButtonsComp.setLayoutData(buttonLayout);
        Button addSFButton = toolkit.createButton(rgButtonsComp, "Add Service Feature...", SWT.PUSH);
        addSFButton.setImage(Images.IMG_OBJ_ADD);
        addSFButton.addSelectionListener(this);
        this.addRGButton = toolkit.createButton(rgButtonsComp, "Add Resource Group...", SWT.PUSH);
        this.addRGButton.addSelectionListener(this);
        this.addRGButton.setEnabled(false);
        this.addRGButton.setImage(Images.IMG_OBJ_ADD);
        this.removeButton = toolkit.createButton(rgButtonsComp, "Remove", SWT.PUSH);
        this.removeButton.setEnabled(false);
        this.removeButton.setImage(Images.IMG_ETOOL_DELETE);
        this.removeButton.addSelectionListener(this);
        
        GridData treeLayout = new GridData();
        treeLayout.verticalAlignment = GridData.FILL;
        treeLayout.grabExcessVerticalSpace = true;
        treeLayout.horizontalAlignment = GridData.FILL;
        treeLayout.grabExcessHorizontalSpace = true;
        
        // Set the layout
        treeViewer.getControl().setLayoutData(treeLayout);
        
        final SectionPart spart = new SectionPart(section);
        managedForm.addPart(spart);
        this.sashForm.setOrientation(SWT.HORIZONTAL);
        managedForm.reflow(true);
        
        TooltipTreeListener tooltipListener = new TooltipTreeListener(treeViewer, this.parentShell);
        
        // Disable the normal tool tips
        treeViewer.getTree().setToolTipText("");
        
        treeViewer.getTree().addListener(SWT.Dispose, tooltipListener);
        treeViewer.getTree().addListener(SWT.KeyDown, tooltipListener);
        treeViewer.getTree().addListener(SWT.MouseMove, tooltipListener);
        treeViewer.getTree().addListener(SWT.MouseHover, tooltipListener);
        
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                managedForm.fireSelectionChanged(spart, event.getSelection());
                
                Tree tree = treeViewer.getTree();
                int selectionCount = tree.getSelectionCount();
                
                if (selectionCount > 0) {
                    ServiceFeatureMasterBlock.this.removeButton.setEnabled(true);
                } else {
                    ServiceFeatureMasterBlock.this.removeButton.setEnabled(false);
                }
                
                // Enable / disable the add rg button
                if (selectionCount == 1) {
                    ServiceFeatureMasterBlock.this.addRGButton.setEnabled(true);
                } else {
                    ServiceFeatureMasterBlock.this.addRGButton.setEnabled(false);
                }
            }
        });
        section.setClient(compo);
    }
    
    
    /**
     * Adds the decoration to the tree
     */
    private void validate() {
        // Set decoration
        this.treeDec.hide();
        
        if (model.getAis().hasIssueType(IssueType.NO_SF_EXISTS)) {
            this.treeDec.show();
            this.treeDec.setDescriptionText(new IssueTranslator()
                    .getTranslationWithoutParameters(IssueType.NO_SF_EXISTS));
        }
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
        detailsPart.registerPage(AISServiceFeature.class, new ServiceFeatureNameDetailsPage(this.model, treeViewer));
        detailsPart.registerPage(AISRequiredResourceGroup.class,
                new ServiceFeatureRGDetailsPage(this.model, treeViewer));
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
        
        final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
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
        Action refresh = new Action("Refresh Resource Group List from server",
                ImageDescriptor.createFromImage(Images.IMG_ELCL_SYNCED)) {
            
            @Override
            public void run() {
                DownloadedRGModel.getInstance().updateRgisListWithJob(ServiceFeatureMasterBlock.this.parentShell, true);
            }
        };
        refresh.setToolTipText("Refresh the Resource Group list from the server");
        
        // Add the actions to the toolbar
        toolBarManager.add(refresh);
        
        toolBarManager.update(true);
        section.setTextClient(toolbar);
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
            if (clicked.getText().equals("Add Service Feature...")) {
                // Show the input dialog
                InputDialog dialog = new InputDialog(this.parentShell, "Add Service Feature",
                        "Enter the identifier of the Service Feature", null, null);
                
                if (dialog.open() == Window.OK) {
                    // Add the service feature and set the dirty flag
                    this.model.setDirty(true);
                    String result = dialog.getValue();
                    this.model.getAis().addServiceFeature(new AISServiceFeature(result));
                    AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
                    validate();
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
                        serviceFeaturesToDel.put((AISServiceFeature) data, item);
                    }
                    
                    if (data instanceof IAISRequiredResourceGroup) {
                        requiredResourceGroupsToDel.put((AISRequiredResourceGroup) data, item);
                    }
                }
                
                // First delete all Service Features
                for (AISServiceFeature toDel : serviceFeaturesToDel.keySet()) {
                    this.model.getAis().getServiceFeatures().remove(toDel);
                    serviceFeaturesToDel.get(toDel).dispose();
                    deleted = true;
                }
                
                // Delete all resource groups that are selected
                for (AISRequiredResourceGroup toDel : requiredResourceGroupsToDel.keySet()) {
                    TreeItem selected = requiredResourceGroupsToDel.get(toDel);
                    if (!selected.isDisposed() && !selected.getParentItem().isDisposed()) {
                        AISServiceFeature parentSf = (AISServiceFeature) selected.getParentItem().getData();
                        parentSf.removeRequiredResourceGroup(toDel);
                        selected.dispose();
                        deleted = true;
                    }
                }
                
                // Mark the model as dirty, validate the SFs and refresh the
                // tree
                if (deleted) {
                    AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
                    validate();
                    this.model.setDirty(true);
                    treeViewer.refresh();
                }
            }
            
            // Add RG was clicked
            if (clicked.getText().equals("Add Resource Group...")) {
                List<RGIS> rgisList = null;
                rgisList = DownloadedRGModel.getInstance().getRgisList(this.parentShell);
                
                if (rgisList != null) {
                    HashMap<String, RGIS> resGroups = new HashMap<String, RGIS>();
                    
                    // Iterate through all set RGs of the Service Feature
                    Tree tree = treeViewer.getTree();
                    TreeItem[] selection = tree.getSelection();
                    
                    AISServiceFeature sf = null;
                    if (selection[0].getData() instanceof AISServiceFeature) {
                        sf = (AISServiceFeature) selection[0].getData();
                    } else {
                        sf = (AISServiceFeature) tree.getSelection()[0].getParentItem().getData();
                    }
                    
                    // Iterate through all available RGs of the server
                    for (RGIS rgis : rgisList) {
                        Boolean found = false;
                        for (IAISRequiredResourceGroup sfRg : sf.getRequiredResourceGroups()) {
                            // Add the RGs that aren't already at the SF
                            if (rgis.getIdentifier().equals(sfRg.getIdentifier())) {
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
                        MessageDialog.openInformation(this.parentShell, "No Resource Groups to add",
                                "You already added all Resource Groups of that are available.");
                    } else {
                        List<RGIS> list = new ArrayList<RGIS>(resGroups.values());
                        RequiredResourceGroupsDialog dialog = new RequiredResourceGroupsDialog(this.parentShell, list);
                        
                        // Get the results
                        if (dialog.open() == Window.OK && dialog.getResult().length > 0) {
                            
                            // Store them at the model
                            for (Object object : dialog.getResult()) {
                                AISRequiredResourceGroup required = (AISRequiredResourceGroup) object;
                                sf.addRequiredResourceGroup(required);
                            }
                            this.model.setDirty(true);
                            AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
                            validate();
                            treeViewer.refresh();
                        }
                    }
                }
            }
        }
    }
}
