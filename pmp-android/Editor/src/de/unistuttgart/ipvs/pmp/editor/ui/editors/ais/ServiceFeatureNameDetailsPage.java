package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import java.util.HashMap;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.ServiceFeatureDescriptionDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ILocaleTableAction;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.LocaleTable;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

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
     * The model of this editor
     */
    private Model model = AisEditor.getModel();

    /**
     * {@link Shell} of the parent
     */
    private Shell parentShell;


    LocaleTable descriptionTable;
    LocaleTable nameTable;

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

	FormToolkit toolkit = form.getToolkit();

	// The name section
	Section nameSection = toolkit.createSection(parent,
		ExpandableComposite.CLIENT_INDENT
			| ExpandableComposite.TITLE_BAR);
	nameSection.setText("Names");
	nameSection.setLayout(new GridLayout(1, false));
	nameSection.setExpanded(true);
	nameSection.setLayoutData(parentLayout);
	createNameSectionAttributeToolbar(nameSection);

	// The description section
	Section descriptionSection = toolkit.createSection(parent,
		ExpandableComposite.CLIENT_INDENT
			| ExpandableComposite.TITLE_BAR);
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
	// Build localization table
	ILocaleTableAction dirtyAction = new ILocaleTableAction() {

	    @Override
	    public void doSetDirty(boolean dirty) {
		AisEditor.getModel().setAISDirty(true);

	    }

	    @Override
	    public void doValidate() {
		AISValidatorWrapper.getInstance().validateServiceFeature(
			displayed, true);
		ServiceFeatureMasterBlock.refreshTree();
	    }

	};
	descriptionTable = new LocaleTable(descriptionComposite,
		LocaleTable.Type.DESCRIPTION, dirtyAction, toolkit);

	descriptionTable.getComposite().setLayoutData(parentLayout);

	// Composite that is display in the name section
	Composite nameComposite = toolkit.createComposite(nameSection);
	nameComposite.setLayout(new GridLayout(1, false));
	nameComposite.setLayoutData(parentLayout);
	nameTable = new LocaleTable(nameComposite, LocaleTable.Type.NAME,
		dirtyAction, toolkit);

	nameTable.getComposite().setLayoutData(parentLayout);

	// Set the composites
	descriptionSection.setClient(descriptionComposite);
	nameSection.setClient(nameComposite);
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
	// Get the selected service feature and set the name
	TreePath[] path = ((TreeSelection) selection).getPaths();
	displayed = (AISServiceFeature) path[0].getFirstSegment();
	descriptionTable.setData(displayed);
	descriptionTable.refresh();

	nameTable.setData(displayed);
	nameTable.refresh();
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
	    @Override
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
		
	    }
	};
	add.setToolTipText("Add a new name for the Service Feature");

	// The remove action
	Action remove = new Action("Remove") {

	    @Override
	    public void run() {
	    }
	};
	remove.setToolTipText("Remove the selected names");

	// Add the actions to the toolbar
	toolBarManager.add(add);
	toolBarManager.add(remove);

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
	    @Override
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

		}
	    }
	};
	add.setToolTipText("Add a new description for the Service Feature");

	// The remove action
	Action remove = new Action("Remove") {

	    @Override
	    public void run() {

	    }
	};
	remove.setToolTipText("Remove the selected descriptions");

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
     * @see org.eclipse.ui.forms.IFormPart#isDirty()
     */
    @Override
    public boolean isDirty() {
	return model.isAisDirty();
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
