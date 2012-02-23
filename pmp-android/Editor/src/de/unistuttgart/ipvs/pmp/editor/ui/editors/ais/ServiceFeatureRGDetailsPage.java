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

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.InputNotEmptyValidator;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.RequiredPSContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.RequiredPrivacySettingsDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.TooltipTableListener;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Shows the table with the details to the resource groups
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureRGDetailsPage implements IDetailsPage,
	IDoubleClickListener, SelectionListener {

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
     * The model of this editor
     */
    private Model model = AisEditor.getModel();

    /**
     * Required privacy setting {@link TableViewer}
     */
    private TableViewer psTableViewer;

    /**
     * The remove button
     */
    private Button removeButton;

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
	Section psSection = toolkit.createSection(parent,
		ExpandableComposite.CLIENT_INDENT
			| ExpandableComposite.TITLE_BAR);
	psSection.setText("Required Privacy Setting");
	psSection.setLayout(new GridLayout(1, false));
	psSection.setExpanded(true);
	psSection.setLayoutData(parentLayout);

	// Composite that is display in the description section
	Composite psComposite = toolkit.createComposite(psSection);
	psComposite.setLayout(new GridLayout(2, false));
	psComposite.setLayoutData(parentLayout);
	createRGTable(psComposite, toolkit);

	Composite psButtonsComp = toolkit.createComposite(psComposite);
	psButtonsComp.setLayout(new FillLayout(SWT.VERTICAL));
	GridData buttonLayout = new GridData();
	buttonLayout.verticalAlignment = SWT.BEGINNING;
	psButtonsComp.setLayoutData(buttonLayout);
	Button addButton = toolkit.createButton(psButtonsComp, "Add", SWT.PUSH);
	addButton.addSelectionListener(this);
	removeButton = toolkit.createButton(psButtonsComp, "Remove", SWT.PUSH);
	removeButton.addSelectionListener((SelectionListener) this);
	removeButton.setEnabled(false);

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
	psTableViewer.setContentProvider(new RequiredPSContentProvider());
	psTableViewer.addDoubleClickListener(this);

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

	removeButton.setEnabled(false);

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
	return model.isAisDirty();
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

	    if (model.isRGListAvailable()) {
		// Get the resource groups from the server
		List<RGIS> rgList = model.getRgisList(parentShell);
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

	    // Show the input dialog
	    InputDialog dialog = new InputDialog(parentShell,
		    "Change the value of the required Privacy Setting",
		    message, selected.getValue(), new InputNotEmptyValidator(
			    "Value"));

	    if (dialog.open() == Window.OK) {
		String result = dialog.getValue();

		if (!result.equals(selected.getValue())) {
		    selected.setValue(result);

		    AISValidatorWrapper.getInstance()
			    .validateRequiredPrivacySetting(selected, true);
		    AISValidatorWrapper.getInstance()
			    .validateRequiredResourceGroup(displayed, true);
		    AISValidatorWrapper.getInstance().validateServiceFeatures(
			    model.getAis(), true);

		    ServiceFeatureMasterBlock.refreshTree();

		    // Update the view
		    psTableViewer.refresh();
		    psTableViewer.getTable().setRedraw(false);
		    identifierColumn.pack();
		    valueColumn.pack();
		    psTableViewer.getTable().redraw();
		    psTableViewer.getTable().setRedraw(true);

		    model.setAISDirty(true);
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

		AISValidatorWrapper.getInstance()
			.validateRequiredResourceGroup(displayed, true);
		AISValidatorWrapper.getInstance().validateServiceFeatures(
			model.getAis(), true);

		ServiceFeatureMasterBlock.refreshTree();

		// Update the view
		psTableViewer.refresh();
		psTableViewer.getTable().setRedraw(false);
		identifierColumn.pack();
		valueColumn.pack();
		psTableViewer.getTable().redraw();
		psTableViewer.getTable().setRedraw(true);

		model.setAISDirty(true);
	    }

	    if (clicked.getText().equals("Add")) {
		// Flag if an error happend while downloading
		Boolean error = false;
		RGIS resGroup = null;

		// Get the resource groups from the server
		List<RGIS> rgList = model.getRgisList(parentShell);
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
		     * Iterate through the set of required Privacy Settings
		     */
		    for (IAISRequiredPrivacySetting requiredPS : displayed
			    .getRequiredPrivacySettings()) {

			/*
			 * Iterate through the privacy settings of the resource
			 * group
			 */
			for (IRGISPrivacySetting ps : resGroup
				.getPrivacySettings()) {
			    if (!ps.getIdentifier().equals(
				    requiredPS.getIdentifier())
				    && !addedIdentifiers.contains(ps
					    .getIdentifier())) {
				if (!customRGIS.getPrivacySettings().contains(
					ps)) {
				    customRGIS.addPrivacySetting(ps);
				}
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

				AISValidatorWrapper.getInstance()
					.validateRequiredPrivacySetting(rps,
						true);
			    }

			    AISValidatorWrapper.getInstance()
				    .validateRequiredResourceGroup(displayed,
					    true);
			    AISValidatorWrapper.getInstance()
				    .validateServiceFeatures(model.getAis(),
					    true);

			    ServiceFeatureMasterBlock.refreshTree();
			    // Update the view
			    psTableViewer.refresh();
			    psTableViewer.getTable().setRedraw(false);
			    identifierColumn.pack();
			    valueColumn.pack();
			    psTableViewer.getTable().setRedraw(true);
			    psTableViewer.getTable().redraw();
			    model.setAISDirty(true);
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
}
