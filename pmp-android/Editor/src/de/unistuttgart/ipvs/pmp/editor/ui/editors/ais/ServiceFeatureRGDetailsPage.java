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
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.internal.Model;

import de.unistuttgart.ipvs.pmp.editor.model.AisModel;
import de.unistuttgart.ipvs.pmp.editor.model.DownloadedRGModel;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.RequiredPSContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.RequiredPrivacySettingChangeValueDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.RequiredPrivacySettingsDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.tooltips.TooltipTableListener;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.editor.xml.IssueTranslator;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Shows the table with the details to the resource groups
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureRGDetailsPage implements IDetailsPage, IDoubleClickListener, SelectionListener,
        FocusListener {
    
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
     * The model of this editor instance
     */
    private final AisModel model;
    
    /**
     * Required privacy setting {@link TableViewer}
     */
    private TableViewer psTableViewer;
    
    /**
     * The remove button
     */
    private Button removeButton;
    
    /**
     * Identifier column of the table
     */
    private TableColumn identifierColumn;
    
    /**
     * Value column of the table
     */
    private TableColumn valueColumn;
    
    /**
     * The {@link TreeViewer} of the parent view to refresh it
     */
    private TreeViewer parentTree;
    
    /**
     * The textfield of the identifier
     */
    private Text identifierField;
    
    /**
     * The decoration of the identifier label
     */
    private ControlDecoration identifierDec;
    
    /**
     * The textfield of the minimum revision
     */
    private Text revisionField;
    
    /**
     * The decoration of the minimal revision label
     */
    private ControlDecoration revisionDec;
    
    /**
     * The decoration of the privacy setting table
     */
    private ControlDecoration psTableDec;
    
    /**
     * Privacy {@link Section}
     */
    private Section psSection;
    
    /**
     * The {@link AISRequiredResourceGroup} that is selected at the tree
     */
    private AISRequiredResourceGroup displayed;
    
    
    /**
     * Constructor to get the model of this editor instance
     * 
     * @param model
     *            {@link Model} of this {@link AisEditor}
     * @param tree
     *            the {@link TreeViewer} of the parent view to refresh it
     */
    public ServiceFeatureRGDetailsPage(AisModel model, TreeViewer tree) {
        this.model = model;
        this.parentTree = tree;
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm
     * )
     */
    @Override
    public void initialize(IManagedForm arg0) {
        this.form = arg0;
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
        this.parentShell = parent.getShell();
        
        // Set parent's layout
        GridData parentLayout = new GridData();
        parentLayout.verticalAlignment = GridData.FILL;
        parentLayout.grabExcessVerticalSpace = true;
        parentLayout.horizontalAlignment = GridData.FILL;
        parentLayout.grabExcessHorizontalSpace = true;
        parent.setLayout(new GridLayout(1, false));
        
        // Attributes section
        FormToolkit toolkit = this.form.getToolkit();
        
        // The attribute layout data
        GridData attributeLayout = new GridData();
        attributeLayout.horizontalAlignment = GridData.FILL;
        attributeLayout.grabExcessHorizontalSpace = true;
        
        // The attribute section
        Section attributeSection = toolkit.createSection(parent, ExpandableComposite.CLIENT_INDENT
                | ExpandableComposite.TITLE_BAR);
        attributeSection.setText("Attributes");
        attributeSection.setLayout(new GridLayout(1, false));
        attributeSection.setExpanded(true);
        attributeSection.setLayoutData(attributeLayout);
        
        Composite attributeComp = toolkit.createComposite(attributeSection);
        GridLayout attributeGridLayout = new GridLayout(2, false);
        attributeGridLayout.horizontalSpacing = 7;
        attributeComp.setLayout(attributeGridLayout);
        
        GridData textLayout = new GridData();
        textLayout.horizontalAlignment = GridData.FILL;
        textLayout.grabExcessHorizontalSpace = true;
        
        Label identifierLabel = new Label(attributeComp, SWT.NONE);
        
        identifierLabel.setText("Identifier:");
        
        this.identifierField = new Text(attributeComp, SWT.BORDER);
        this.identifierField.setLayoutData(textLayout);
        
        this.identifierDec = new ControlDecoration(this.identifierField, SWT.TOP | SWT.LEFT);
        this.identifierDec.setImage(Images.IMG_DEC_FIELD_ERROR);
        
        // Store the field in the model when sth. was changed
        this.identifierField.addKeyListener(new org.eclipse.swt.events.KeyListener() {
            
            @Override
            public void keyReleased(org.eclipse.swt.events.KeyEvent arg0) {
                // The old value of this text field
                String oldValue = ServiceFeatureRGDetailsPage.this.displayed.getIdentifier();
                if (!ServiceFeatureRGDetailsPage.this.identifierField.getText().equals(oldValue)) {
                    ServiceFeatureRGDetailsPage.this.displayed
                            .setIdentifier(ServiceFeatureRGDetailsPage.this.identifierField.getText());
                    ServiceFeatureRGDetailsPage.this.parentTree.refresh();
                    ServiceFeatureRGDetailsPage.this.model.setDirty(true);
                }
            }
            
            
            @Override
            public void keyPressed(org.eclipse.swt.events.KeyEvent arg0) {
            }
        });
        
        this.identifierField.addFocusListener(this);
        this.identifierField.setData("_NAME", "identifier");
        
        // The minimum revision label and text
        Label minRevisionLabel = new Label(attributeComp, SWT.NONE);
        minRevisionLabel.setText("Minimal revision:");
        
        this.revisionField = new Text(attributeComp, SWT.BORDER);
        attributeSection.setClient(attributeComp);
        this.revisionField.setLayoutData(textLayout);
        
        this.revisionDec = new ControlDecoration(this.revisionField, SWT.TOP | SWT.LEFT);
        this.revisionDec.setImage(Images.IMG_DEC_FIELD_ERROR);
        
        // Store the field in the model when sth. was changed
        this.revisionField.addKeyListener(new org.eclipse.swt.events.KeyListener() {
            
            @Override
            public void keyReleased(org.eclipse.swt.events.KeyEvent arg0) {
                // The old value of this text field
                String oldValue = ServiceFeatureRGDetailsPage.this.displayed.getMinRevision();
                if (!ServiceFeatureRGDetailsPage.this.revisionField.getText().equals(oldValue)) {
                    ServiceFeatureRGDetailsPage.this.displayed
                            .setMinRevision(ServiceFeatureRGDetailsPage.this.revisionField.getText());
                    ServiceFeatureRGDetailsPage.this.model.setDirty(true);
                }
            }
            
            
            @Override
            public void keyPressed(org.eclipse.swt.events.KeyEvent arg0) {
            }
        });
        this.revisionField.addFocusListener(this);
        this.revisionField.setData("_NAME", "revision");
        
        // The name section
        this.psSection = toolkit.createSection(parent, ExpandableComposite.CLIENT_INDENT
                | ExpandableComposite.TITLE_BAR);
        this.psSection.setLayout(new GridLayout(1, false));
        this.psSection.setExpanded(true);
        this.psSection.setLayoutData(parentLayout);
        
        // Composite that is display in the description section
        Composite psComposite = toolkit.createComposite(this.psSection);
        psComposite.setLayout(new GridLayout(2, false));
        psComposite.setLayoutData(parentLayout);
        createPSTable(psComposite, toolkit);
        
        Composite psButtonsComp = toolkit.createComposite(psComposite);
        psButtonsComp.setLayout(new FillLayout(SWT.VERTICAL));
        GridData buttonLayout = new GridData();
        buttonLayout.verticalAlignment = SWT.BEGINNING;
        psButtonsComp.setLayoutData(buttonLayout);
        Button addButton = toolkit.createButton(psButtonsComp, "Add...", SWT.PUSH);
        addButton.addSelectionListener(this);
        addButton.setImage(Images.IMG_OBJ_ADD);
        
        this.removeButton = toolkit.createButton(psButtonsComp, "Remove", SWT.PUSH);
        this.removeButton.addSelectionListener(this);
        this.removeButton.setImage(Images.IMG_ETOOL_DELETE);
        this.removeButton.setEnabled(false);
        
        this.psSection.setClient(psComposite);
    }
    
    
    /**
     * Creates the resource group table
     * 
     * @param parent
     *            parent {@link Composite}
     * @param toolkit
     *            {@link FormToolkit}
     * @return the created {@link TableViewer}
     */
    private TableViewer createPSTable(Composite parent, FormToolkit toolkit) {
        // Use grid layout so that the table uses the whole screen width
        final GridData layoutData = new GridData();
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.verticalAlignment = GridData.FILL;
        layoutData.grabExcessVerticalSpace = true;
        
        // Workaround for SWT-Bug needed
        // (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
        layoutData.widthHint = 1;
        
        this.psTableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        this.psTableViewer.setContentProvider(new RequiredPSContentProvider());
        this.psTableViewer.addDoubleClickListener(this);
        
        this.psTableDec = new ControlDecoration(this.psTableViewer.getControl(), SWT.TOP | SWT.LEFT);
        this.psTableDec.setImage(Images.IMG_DEC_FIELD_ERROR);
        
        // Disable the default tool tips
        this.psTableViewer.getTable().setToolTipText("");
        
        TooltipTableListener tooltipListener = new TooltipTableListener(this.psTableViewer, this.parentShell);
        
        this.psTableViewer.getTable().addListener(SWT.Dispose, tooltipListener);
        this.psTableViewer.getTable().addListener(SWT.KeyDown, tooltipListener);
        this.psTableViewer.getTable().addListener(SWT.MouseMove, tooltipListener);
        this.psTableViewer.getTable().addListener(SWT.MouseHover, tooltipListener);
        
        // The identifier column with the LabelProvider
        TableViewerColumn identifierViewerColumn = new TableViewerColumn(this.psTableViewer, SWT.NULL);
        identifierViewerColumn.getColumn().setText("Identifier");
        identifierViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            
            @Override
            public String getText(Object element) {
                return ((AISRequiredPrivacySetting) element).getIdentifier();
            }
            
            
            @Override
            public Image getImage(Object element) {
                AISRequiredPrivacySetting item = (AISRequiredPrivacySetting) element;
                if (!item.getIssues().isEmpty()) {
                    return Images.ERROR16;
                }
                return null;
            }
        });
        this.identifierColumn = identifierViewerColumn.getColumn();
        
        // The value column with the LabelProvider
        TableViewerColumn valueViewerColumn = new TableViewerColumn(this.psTableViewer, SWT.NULL);
        valueViewerColumn.getColumn().setText("Value");
        valueViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            
            @Override
            public String getText(Object element) {
                if (((AISRequiredPrivacySetting) element) == null) {
                    return "";
                }
                return ((AISRequiredPrivacySetting) element).getValue();
            }
        });
        
        this.valueColumn = valueViewerColumn.getColumn();
        
        // Define the table's view
        Table psTable = this.psTableViewer.getTable();
        psTable.setLayoutData(layoutData);
        psTable.setHeaderVisible(true);
        psTable.setLinesVisible(true);
        
        // SelectionListener to enable and disable the remove button
        this.psTableViewer.getTable().addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (ServiceFeatureRGDetailsPage.this.psTableViewer.getTable().getSelectionCount() > 0) {
                    ServiceFeatureRGDetailsPage.this.removeButton.setEnabled(true);
                } else {
                    ServiceFeatureRGDetailsPage.this.removeButton.setEnabled(false);
                }
            }
            
            
            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
        
        markEmptyCells();
        return this.psTableViewer;
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
        this.psTableViewer.getTable().setRedraw(false);
        
        // Get the selected service feature and set the name
        TreePath[] path = ((TreeSelection) selection).getPaths();
        this.displayed = (AISRequiredResourceGroup) path[0].getLastSegment();
        this.psTableViewer.setInput(this.displayed);
        
        // Set the identifier and minimal revision
        this.identifierField.setText(this.displayed.getIdentifier());
        this.revisionField.setText(this.displayed.getMinRevision());
        
        // Update all decorations
        updateIdentifierDec();
        updateRevisionDec();
        updatePSTableDec();
        
        this.removeButton.setEnabled(false);
        
        /*
         * Set the title of the section. If the RGIS list from the server is
         * available then with the name, else with the identifier from the RG
         */
        Boolean found = false;
        if (DownloadedRGModel.getInstance().isRGListAvailable()) {
            for (RGIS rg : DownloadedRGModel.getInstance().getRgisList(this.parentShell)) {
                if (rg.getIdentifier().equals(this.displayed.getIdentifier())) {
                    this.psSection.setText("Required Privacy Settings: " + rg.getNameForLocale(new Locale("en")));
                    found = true;
                    break;
                }
            }
        }
        
        // RG not found or not available. Set the identifier
        if (!found) {
            this.psSection.setText("Required Privacy Settings: " + this.displayed.getIdentifier());
        }
        
        // Pack all columns
        this.identifierColumn.pack();
        this.valueColumn.pack();
        
        markEmptyCells();
        
        this.psTableViewer.getTable().setRedraw(true);
        this.psTableViewer.getTable().redraw();
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#isDirty()
     */
    @Override
    public boolean isDirty() {
        return this.model.isDirty();
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
        int selectionCount = this.psTableViewer.getTable().getSelectionCount();
        if (selectionCount == 1) {
            RGIS resGroup = null;
            
            if (DownloadedRGModel.getInstance().isRGListAvailable()) {
                // Get the resource groups from the server
                List<RGIS> rgList = DownloadedRGModel.getInstance().getRgisList(this.parentShell);
                if (rgList != null) {
                    for (RGIS rgis : rgList) {
                        if (rgis.getIdentifier().equals(this.displayed.getIdentifier())) {
                            resGroup = rgis;
                        }
                    }
                }
            }
            
            AISRequiredPrivacySetting selected = (AISRequiredPrivacySetting) this.psTableViewer.getTable()
                    .getSelection()[0].getData();
            
            String requiredValues = null;
            if (resGroup != null) {
                for (IRGISPrivacySetting ps : resGroup.getPrivacySettings()) {
                    if (ps.getIdentifier().equals(selected.getIdentifier())) {
                        requiredValues = ps.getValidValueDescription();
                        break;
                    }
                }
            }
            
            String message;
            if (requiredValues != null) {
                message = "Enter the value of the required Privacy Setting \"" + selected.getIdentifier()
                        + "\" \n Valid values are: " + requiredValues;
            } else {
                message = "Enter the value of the required Privacy Setting \"" + selected.getIdentifier() + "\"";
            }
            
            // Show the input dialog;
            String[] resultArray = new String[1];
            RequiredPrivacySettingChangeValueDialog dialog = new RequiredPrivacySettingChangeValueDialog(
                    this.parentShell, selected.getValue(), message, resultArray);
            
            if (dialog.open() == Window.OK) {
                
                String result = resultArray[0];
                
                if (result != null) {
                    if (!result.equals(selected.getValue())) {
                        selected.setValue(result);
                        
                        AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
                        
                        this.parentTree.refresh();
                        
                        // Update the view
                        this.psTableViewer.refresh();
                        this.psTableViewer.getTable().setRedraw(false);
                        this.identifierColumn.pack();
                        this.valueColumn.pack();
                        this.psTableViewer.getTable().redraw();
                        this.psTableViewer.getTable().setRedraw(true);
                        
                        markEmptyCells();
                        this.model.setDirty(true);
                    }
                } else {
                    // The value is not empty but nothing was entered
                    selected.setValue(null);
                    
                    AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
                    
                    this.parentTree.refresh();
                    
                    // Update the view
                    this.psTableViewer.refresh();
                    this.psTableViewer.getTable().setRedraw(false);
                    this.identifierColumn.pack();
                    this.valueColumn.pack();
                    this.psTableViewer.getTable().redraw();
                    this.psTableViewer.getTable().setRedraw(true);
                    
                    markEmptyCells();
                    this.model.setDirty(true);
                }
            }
        }
        
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
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
     * .swt.events.SelectionEvent)
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
     * .events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent widget) {
        if (widget.getSource() instanceof Button) {
            Button clicked = (Button) widget.getSource();
            
            // Remove the selected items
            if (clicked.getText().equals("Remove")) {
                TableItem[] selected = this.psTableViewer.getTable().getSelection();
                for (TableItem item : selected) {
                    AISRequiredPrivacySetting ps = (AISRequiredPrivacySetting) item.getData();
                    this.displayed.removeRequiredPrivacySetting(ps);
                }
                
                AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
                
                this.parentTree.refresh();
                
                // Update the view
                this.psTableViewer.refresh();
                this.psTableViewer.getTable().setRedraw(false);
                this.identifierColumn.pack();
                this.valueColumn.pack();
                this.psTableViewer.getTable().redraw();
                this.psTableViewer.getTable().setRedraw(true);
                updatePSTableDec();
                
                this.model.setDirty(true);
            }
            
            if (clicked.getText().equals("Add...")) {
                // Flag if an error happend while downloading
                Boolean error = false;
                RGIS resGroup = null;
                
                // Get the resource groups from the server
                List<RGIS> rgList = DownloadedRGModel.getInstance().getRgisList(this.parentShell);
                if (rgList != null) {
                    for (RGIS rgis : rgList) {
                        if (rgis.getIdentifier().equals(this.displayed.getIdentifier())) {
                            resGroup = rgis;
                        }
                    }
                } else {
                    error = true;
                }
                
                // Build a custom RGIS with the privacy settings that are
                // not set yet
                RGIS customRGIS = new RGIS();
                
                // Check if there are RGs from the server
                if (resGroup != null) {
                    HashMap<String, IRGISPrivacySetting> privacySettings = new HashMap<String, IRGISPrivacySetting>();
                    
                    // Iterate through all available PSs
                    for (IRGISPrivacySetting ps : resGroup.getPrivacySettings()) {
                        privacySettings.put(ps.getIdentifier(), ps);
                    }
                    
                    // Build a list with all added privacy settings
                    ArrayList<String> addedIdentifiers = new ArrayList<String>();
                    for (IAISRequiredPrivacySetting ps : this.displayed.getRequiredPrivacySettings()) {
                        addedIdentifiers.add(ps.getIdentifier());
                    }
                    
                    /*
                     * Iterate through the privacy settings of the resource
                     * group
                     */
                    for (IRGISPrivacySetting ps : resGroup.getPrivacySettings()) {
                        
                        /*
                         * Check if this ps is already added to RGIS list that /
                         * will be displayed
                         */
                        if (!customRGIS.getPrivacySettings().contains(ps)) {
                            
                            /*
                             * Check if this PS is already added to the
                             * resourcegroup
                             */
                            if (!addedIdentifiers.contains(ps.getIdentifier())) {
                                customRGIS.addPrivacySetting(ps);
                            }
                        }
                    }
                    
                    // If there are some PS to add
                    if (!customRGIS.getPrivacySettings().isEmpty()) {
                        
                        // Open the dialog
                        SelectionDialog dialog = new RequiredPrivacySettingsDialog(this.parentShell, customRGIS);
                        
                        // Get the results
                        if (dialog.open() == Window.OK && dialog.getResult().length > 0) {
                            
                            // Store them at the model
                            for (Object object : dialog.getResult()) {
                                AISRequiredPrivacySetting rps = (AISRequiredPrivacySetting) object;
                                this.displayed.addRequiredPrivacySetting(rps);
                            }
                            
                            AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
                            
                            this.parentTree.refresh();
                            // Update the view
                            this.psTableViewer.refresh();
                            markEmptyCells();
                            this.psTableViewer.getTable().setRedraw(false);
                            this.identifierColumn.pack();
                            this.valueColumn.pack();
                            this.psTableViewer.getTable().setRedraw(true);
                            this.psTableViewer.getTable().redraw();
                            this.model.setDirty(true);
                            updatePSTableDec();
                        }
                        
                        // There are no PS to add to this service feature
                    } else {
                        MessageDialog.openInformation(this.parentShell, "No Privacy Settings to add",
                                "You already added all Privacy Settings of this Resource Group");
                    }
                } else {
                    /*
                     * The Resource group wasn't found at the server and no
                     * error happen while downloading them, show the
                     * corresponding message
                     */
                    if (!error) {
                        MessageDialog.openInformation(this.parentShell, "Unknown Resource Group",
                                "This Resource Group was not found at the Resource Group server.\n"
                                        + "Therefore you can not add a Privacy Setting");
                    }
                }
            }
        }
    }
    
    
    /**
     * Marks the value cells that are should be an empty value
     */
    public void markEmptyCells() {
        TableItem[] items = this.psTableViewer.getTable().getItems();
        for (TableItem item : items) {
            AISRequiredPrivacySetting rps = (AISRequiredPrivacySetting) item.getData();
            if (rps.getValue() != null) {
                if (rps.getValue().isEmpty()) {
                    item.setBackground(1, Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
                }
            }
        }
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events
     * .FocusEvent)
     */
    @Override
    public void focusGained(FocusEvent arg0) {
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events
     * .FocusEvent)
     */
    @Override
    public void focusLost(FocusEvent event) {
        AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
        Text source = (Text) event.getSource();
        String name = (String) source.getData("_NAME");
        if (name.equals("identifier")) {
            updateIdentifierDec();
        }
        
        if (name.equals("revision")) {
            updateRevisionDec();
        }
    }
    
    
    /**
     * Update the identifier decoration and sets the message that is displayed
     */
    private void updateIdentifierDec() {
        this.identifierDec.hide();
        if (this.displayed.hasIssueType(IssueType.IDENTIFIER_MISSING)) {
            this.identifierDec.show();
            this.identifierDec.setDescriptionText(new IssueTranslator()
                    .getTranslationWithoutParameters(IssueType.IDENTIFIER_MISSING));
        }
    }
    
    
    /**
     * Update the minimal revision decoration and sets the message that is
     * displayed
     */
    private void updateRevisionDec() {
        this.revisionDec.hide();
        String message = "";
        if (this.displayed.hasIssueType(IssueType.MINREVISION_MISSING)) {
            message += new IssueTranslator().getTranslationWithoutParameters(IssueType.MINREVISION_MISSING);
        }
        if (this.displayed.hasIssueType(IssueType.MINREVISION_INVALID)) {
            if (!message.isEmpty()) {
                message += "\n" + new IssueTranslator().getTranslationWithoutParameters(IssueType.MINREVISION_INVALID);
            } else {
                message += new IssueTranslator().getTranslationWithoutParameters(IssueType.MINREVISION_INVALID);
            }
        }
        
        if (!message.isEmpty()) {
            this.revisionDec.show();
            this.revisionDec.setDescriptionText(message);
        }
    }
    
    
    /**
     * Updates the decoration of the privacy setting table
     */
    private void updatePSTableDec() {
        this.psTableDec.hide();
        if (this.displayed.hasIssueType(IssueType.NO_RPS_EXISTS)) {
            this.psTableDec.show();
            this.psTableDec.setDescriptionText(new IssueTranslator()
                    .getTranslationWithoutParameters(IssueType.NO_RPS_EXISTS));
        }
    }
}
