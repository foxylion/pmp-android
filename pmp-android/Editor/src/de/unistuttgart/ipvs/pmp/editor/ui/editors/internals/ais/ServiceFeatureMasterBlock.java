package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.InputDialog;
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
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

/**
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureMasterBlock extends MasterDetailsBlock implements
	IDoubleClickListener {

    /**
     * The {@link TreeViewer} of this block
     */
    private TreeViewer treeViewer;

    /**
     * {@link Shell} of the parent composite
     */
    private Shell parentShell;

    /**
     * The remove action
     */
    private Action remove;

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
	Section section = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR);
	section.setText("Privacy Settings");
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

		Boolean enable = true;

		// Check if all selections are on the service features
		for (int itr = 0; itr < selectionCount; itr++) {
		    TreeItem item = selection[itr];
		    Object data = item.getData();
		    if (!(data instanceof AISServiceFeature)) {
			enable = false;
		    }
		}

		// If yes then enable the delete button
		if (enable) {
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
    protected void registerPages(DetailsPart arg0) {
	// TODO Auto-generated method stub

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
			sf.getIdentifier(), new ServiceFeatureInputValidator());

		if (dialog.open() == Window.OK) {
		    String result = dialog.getValue();

		    // Name changed, model is dirty
		    if (!result.equals(sf.getIdentifier())) {

			// Change the service feature and set the dirty flag
			Model.getInstance().setAISDirty(true);
			sf.setIdentifier(result);
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
	Action add = new Action("Add") {

	    @Override
	    public void run() {
		// Show the input dialog
		InputDialog dialog = new InputDialog(parentShell,
			"Add Service Feature",
			"Enter the identifier of the Service Feature", null,
			new ServiceFeatureInputValidator());

		if (dialog.open() == Window.OK) {
		    // Add the service feature and set the dirty flag
		    Model.getInstance().setAISDirty(true);
		    String result = dialog.getValue();
		    Model.getInstance().getAis()
			    .addServiceFeature(new AISServiceFeature(result));
		    treeViewer.refresh();
		}
	    }
	};
	add.setToolTipText("Add a new Service Feature");

	// The remove action
	remove = new Action("Remove") {
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
		}
		if (deleted) {
		    treeViewer.refresh();
		    Model.getInstance().setAISDirty(true);

		}

	    }
	};
	remove.setToolTipText("Remove the selected Service Features");

	// Add the actions to the toolbar
	toolBarManager.add(add);
	remove.setEnabled(false);
	toolBarManager.add(remove);

	toolBarManager.update(true);
	section.setTextClient(toolbar);
    }

}
