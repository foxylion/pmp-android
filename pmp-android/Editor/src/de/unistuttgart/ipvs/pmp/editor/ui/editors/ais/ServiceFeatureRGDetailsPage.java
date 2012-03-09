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
public class ServiceFeatureRGDetailsPage implements IDetailsPage,
	IDoubleClickListener, SelectionListener, FocusListener {

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

	// The attribute layout data
	GridData attributeLayout = new GridData();
	attributeLayout.horizontalAlignment = GridData.FILL;
	attributeLayout.grabExcessHorizontalSpace = true;

	// The attribute section
	Section attributeSection = toolkit.createSection(parent,
		ExpandableComposite.CLIENT_INDENT
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

	identifierField = new Text(attributeComp, SWT.BORDER);
	identifierField.setLayoutData(textLayout);

	identifierDec = new ControlDecoration(identifierField, SWT.TOP
		| SWT.LEFT);
	identifierDec.setImage(Images.IMG_DEC_FIELD_ERROR);

	// Store the field in the model when sth. was changed
	identifierField
		.addKeyListener(new org.eclipse.swt.events.KeyListener() {

		    @Override
		    public void keyReleased(org.eclipse.swt.events.KeyEvent arg0) {
			// The old value of this text field
			String oldValue = displayed.getIdentifier();
			if (!identifierField.getText().equals(oldValue)) {
			    displayed.setIdentifier(identifierField.getText());
			    parentTree.refresh();
			    model.setDirty(true);
			}
		    }

		    @Override
		    public void keyPressed(org.eclipse.swt.events.KeyEvent arg0) {
		    }
		});

	identifierField.addFocusListener(this);
	identifierField.setData("_NAME", "identifier");

	// The minimum revision label and text
	Label minRevisionLabel = new Label(attributeComp, SWT.NONE);
	minRevisionLabel.setText("Minimal revision:");

	revisionField = new Text(attributeComp, SWT.BORDER);
	attributeSection.setClient(attributeComp);
	revisionField.setLayoutData(textLayout);

	revisionDec = new ControlDecoration(revisionField, SWT.TOP | SWT.LEFT);
	revisionDec.setImage(Images.IMG_DEC_FIELD_ERROR);

	// Store the field in the model when sth. was changed
	revisionField.addKeyListener(new org.eclipse.swt.events.KeyListener() {

	    @Override
	    public void keyReleased(org.eclipse.swt.events.KeyEvent arg0) {
		// The old value of this text field
		String oldValue = displayed.getMinRevision();
		if (!revisionField.getText().equals(oldValue)) {
		    displayed.setMinRevision(revisionField.getText());
		    model.setDirty(true);
		}
	    }

	    @Override
	    public void keyPressed(org.eclipse.swt.events.KeyEvent arg0) {
	    }
	});
	revisionField.addFocusListener(this);
	revisionField.setData("_NAME", "revision");

	// The name section
	psSection = toolkit.createSection(parent,
		ExpandableComposite.CLIENT_INDENT
			| ExpandableComposite.TITLE_BAR);
	psSection.setLayout(new GridLayout(1, false));
	psSection.setExpanded(true);
	psSection.setLayoutData(parentLayout);

	// Composite that is display in the description section
	Composite psComposite = toolkit.createComposite(psSection);
	psComposite.setLayout(new GridLayout(2, false));
	psComposite.setLayoutData(parentLayout);
	createPSTable(psComposite, toolkit);

	Composite psButtonsComp = toolkit.createComposite(psComposite);
	psButtonsComp.setLayout(new FillLayout(SWT.VERTICAL));
	GridData buttonLayout = new GridData();
	buttonLayout.verticalAlignment = SWT.BEGINNING;
	psButtonsComp.setLayoutData(buttonLayout);
	Button addButton = toolkit.createButton(psButtonsComp, "Add...",
		SWT.PUSH);
	addButton.addSelectionListener(this);
	addButton.setImage(Images.IMG_OBJ_ADD);

	removeButton = toolkit.createButton(psButtonsComp, "Remove", SWT.PUSH);
	removeButton.addSelectionListener(this);
	removeButton.setImage(Images.IMG_ETOOL_DELETE);
	removeButton.setEnabled(false);

	psSection.setClient(psComposite);
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

	psTableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION
		| SWT.MULTI);
	psTableViewer.setContentProvider(new RequiredPSContentProvider());
	psTableViewer.addDoubleClickListener(this);

	psTableDec = new ControlDecoration(psTableViewer.getControl(), SWT.TOP
		| SWT.LEFT);
	psTableDec.setImage(Images.IMG_DEC_FIELD_ERROR);

	// Disable the default tool tips
	psTableViewer.getTable().setToolTipText("");

	TooltipTableListener tooltipListener = new TooltipTableListener(
		psTableViewer, parentShell);

	psTableViewer.getTable().addListener(SWT.Dispose, tooltipListener);
	psTableViewer.getTable().addListener(SWT.KeyDown, tooltipListener);
	psTableViewer.getTable().addListener(SWT.MouseMove, tooltipListener);
	psTableViewer.getTable().addListener(SWT.MouseHover, tooltipListener);

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
		AISRequiredPrivacySetting item = (AISRequiredPrivacySetting) element;
		if (!item.getIssues().isEmpty()) {
		    return Images.ERROR16;
		}
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
		if (((AISRequiredPrivacySetting) element) == null) {
		    return "";
		}
		return ((AISRequiredPrivacySetting) element).getValue();
	    }
	});

	valueColumn = valueViewerColumn.getColumn();

	// Define the table's view
	Table psTable = psTableViewer.getTable();
	psTable.setLayoutData(layoutData);
	psTable.setHeaderVisible(true);
	psTable.setLinesVisible(true);

	// SelectionListener to enable and disable the remove button
	psTableViewer.getTable().addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent arg0) {
		if (psTableViewer.getTable().getSelectionCount() > 0) {
		    removeButton.setEnabled(true);
		} else {
		    removeButton.setEnabled(false);
		}
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent arg0) {
	    }
	});

	markEmptyCells();
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

	// Set the identifier and minimal revision
	identifierField.setText(displayed.getIdentifier());
	revisionField.setText(displayed.getMinRevision());

	// Update all decorations
	updateIdentifierDec();
	updateRevisionDec();
	updatePSTableDec();

	removeButton.setEnabled(false);

	/*
	 * Set the title of the section. If the RGIS list from the server is
	 * available then with the name, else with the identifier from the RG
	 */
	Boolean found = false;
	if (DownloadedRGModel.getInstance().isRGListAvailable()) {
	    for (RGIS rg : DownloadedRGModel.getInstance().getRgisList(
		    parentShell)) {
		if (rg.getIdentifier().equals(displayed.getIdentifier())) {
		    psSection.setText("Required Privacy Settings: "
			    + rg.getNameForLocale(new Locale("en")));
		    found = true;
		    break;
		}
	    }
	}

	// RG not found or not available. Set the identifier
	if (!found) {
	    psSection.setText("Required Privacy Settings: "
		    + displayed.getIdentifier());
	}

	// Pack all columns
	identifierColumn.pack();
	valueColumn.pack();

	markEmptyCells();

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
	return model.isDirty();
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
	int selectionCount = psTableViewer.getTable().getSelectionCount();
	if (selectionCount == 1) {
	    RGIS resGroup = null;

	    if (DownloadedRGModel.getInstance().isRGListAvailable()) {
		// Get the resource groups from the server
		List<RGIS> rgList = DownloadedRGModel.getInstance()
			.getRgisList(parentShell);
		if (rgList != null) {
		    for (RGIS rgis : rgList) {
			if (rgis.getIdentifier().equals(
				displayed.getIdentifier())) {
			    resGroup = rgis;
			}
		    }
		}
	    }

	    AISRequiredPrivacySetting selected = (AISRequiredPrivacySetting) psTableViewer
		    .getTable().getSelection()[0].getData();

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
		message = "Enter the value of the required Privacy Setting \""
			+ selected.getIdentifier() + "\" \n Valid values are: "
			+ requiredValues;
	    } else {
		message = "Enter the value of the required Privacy Setting \""
			+ selected.getIdentifier() + "\"";
	    }

	    // Show the input dialog;
	    String[] resultArray = new String[1];
	    RequiredPrivacySettingChangeValueDialog dialog = new RequiredPrivacySettingChangeValueDialog(
		    parentShell, selected.getValue(), message, resultArray);

	    if (dialog.open() == Window.OK) {

		String result = resultArray[0];

		if (result != null) {
		    if (!result.equals(selected.getValue())) {
			selected.setValue(result);

			AISValidatorWrapper.getInstance().validateAIS(
				model.getAis(), true);

			parentTree.refresh();

			// Update the view
			psTableViewer.refresh();
			psTableViewer.getTable().setRedraw(false);
			identifierColumn.pack();
			valueColumn.pack();
			psTableViewer.getTable().redraw();
			psTableViewer.getTable().setRedraw(true);

			markEmptyCells();
			model.setDirty(true);
		    }
		} else {
		    // The value is not empty but nothing was entered
		    selected.setValue(null);

		    AISValidatorWrapper.getInstance().validateAIS(
			    model.getAis(), true);

		    parentTree.refresh();

		    // Update the view
		    psTableViewer.refresh();
		    psTableViewer.getTable().setRedraw(false);
		    identifierColumn.pack();
		    valueColumn.pack();
		    psTableViewer.getTable().redraw();
		    psTableViewer.getTable().setRedraw(true);

		    markEmptyCells();
		    model.setDirty(true);
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
		TableItem[] selected = psTableViewer.getTable().getSelection();
		for (TableItem item : selected) {
		    AISRequiredPrivacySetting ps = (AISRequiredPrivacySetting) item
			    .getData();
		    displayed.removeRequiredPrivacySetting(ps);
		}

		AISValidatorWrapper.getInstance().validateAIS(model.getAis(),
			true);

		parentTree.refresh();

		// Update the view
		psTableViewer.refresh();
		psTableViewer.getTable().setRedraw(false);
		identifierColumn.pack();
		valueColumn.pack();
		psTableViewer.getTable().redraw();
		psTableViewer.getTable().setRedraw(true);
		updatePSTableDec();

		model.setDirty(true);
	    }

	    if (clicked.getText().equals("Add...")) {
		// Flag if an error happend while downloading
		Boolean error = false;
		RGIS resGroup = null;

		// Get the resource groups from the server
		List<RGIS> rgList = DownloadedRGModel.getInstance()
			.getRgisList(parentShell);
		if (rgList != null) {
		    for (RGIS rgis : rgList) {
			if (rgis.getIdentifier().equals(
				displayed.getIdentifier())) {
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
		    for (IAISRequiredPrivacySetting ps : displayed
			    .getRequiredPrivacySettings()) {
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
			SelectionDialog dialog = new RequiredPrivacySettingsDialog(
				parentShell, customRGIS);

			// Get the results
			if (dialog.open() == Window.OK
				&& dialog.getResult().length > 0) {

			    // Store them at the model
			    for (Object object : dialog.getResult()) {
				AISRequiredPrivacySetting rps = (AISRequiredPrivacySetting) object;
				displayed.addRequiredPrivacySetting(rps);
			    }

			    AISValidatorWrapper.getInstance().validateAIS(
				    model.getAis(), true);

			    parentTree.refresh();
			    // Update the view
			    psTableViewer.refresh();
			    markEmptyCells();
			    psTableViewer.getTable().setRedraw(false);
			    identifierColumn.pack();
			    valueColumn.pack();
			    psTableViewer.getTable().setRedraw(true);
			    psTableViewer.getTable().redraw();
			    model.setDirty(true);
			    updatePSTableDec();
			}

			// There are no PS to add to this service feature
		    } else {
			MessageDialog
				.openInformation(parentShell,
					"No Privacy Settings to add",
					"You already added all Privacy Settings of this Resource Group");
		    }
		} else {
		    /*
		     * The Resource group wasn't found at the server and no
		     * error happen while downloading them, show the
		     * corresponding message
		     */
		    if (!error) {
			MessageDialog
				.openInformation(
					parentShell,
					"Unknown Resource Group",
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
	TableItem[] items = psTableViewer.getTable().getItems();
	for (TableItem item : items) {
	    AISRequiredPrivacySetting rps = (AISRequiredPrivacySetting) item
		    .getData();
	    if (rps.getValue() != null) {
		if (rps.getValue().isEmpty()) {
		    item.setBackground(
			    1,
			    Display.getCurrent().getSystemColor(
				    SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
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
	AISValidatorWrapper.getInstance().validateAIS(model.getAis(), true);
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
	identifierDec.hide();
	if (displayed.hasIssueType(IssueType.IDENTIFIER_MISSING)) {
	    identifierDec.show();
	    identifierDec
		    .setDescriptionText(new IssueTranslator()
			    .getTranslationWithoutParameters(IssueType.IDENTIFIER_MISSING));
	}
    }

    /**
     * Update the minimal revision decoration and sets the message that is
     * displayed
     */
    private void updateRevisionDec() {
	revisionDec.hide();
	String message = "";
	if (displayed.hasIssueType(IssueType.MINREVISION_MISSING)) {
	    message += new IssueTranslator()
		    .getTranslationWithoutParameters(IssueType.MINREVISION_MISSING);
	}
	if (displayed.hasIssueType(IssueType.MINREVISION_INVALID)) {
	    if (!message.isEmpty()) {
		message += "\n"
			+ new IssueTranslator()
				.getTranslationWithoutParameters(IssueType.MINREVISION_INVALID);
	    } else {
		message += new IssueTranslator()
			.getTranslationWithoutParameters(IssueType.MINREVISION_INVALID);
	    }
	}

	if (!message.isEmpty()) {
	    revisionDec.show();
	    revisionDec.setDescriptionText(message);
	}
    }

    /**
     * Updates the decoration of the privacy setting table
     */
    private void updatePSTableDec() {
	psTableDec.hide();
	if (displayed.hasIssueType(IssueType.NO_RPS_EXISTS)) {
	    psTableDec.show();
	    psTableDec.setDescriptionText(new IssueTranslator()
		    .getTranslationWithoutParameters(IssueType.NO_RPS_EXISTS));
	}
    }
}
