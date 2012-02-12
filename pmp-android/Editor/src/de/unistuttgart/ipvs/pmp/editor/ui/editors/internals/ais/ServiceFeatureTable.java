package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

/**
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureTable implements IDoubleClickListener {

    /**
     * {@link Shell} of the parent component, used to display dialogs
     */
    private Shell parentShell;

    /**
     * Creates the table for the service features with the add button
     * 
     * @param parent
     *            parent {@link Composite}
     * @param toolkit
     *            {@link FormToolkit}
     * @param height
     *            height of the editor
     * @param bounds
     *            bounds of the editor
     */
    public ServiceFeatureTable(final Composite parent, FormToolkit toolkit,
	    int height) {
	parentShell = parent.getShell();
	Composite composite = toolkit.createComposite(parent);
	composite.setLayout(new GridLayout(2, false));

	// Use grid layout so that the table uses the whole screen width
	final GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;

	// Workaround for SWT-Bug needed
	// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
	layoutData.widthHint = 1;

	// Set layout as we want to set a relative columns width
	Composite tableComposite = new Composite(composite, SWT.NONE);

	// Create the table viewer
	final TableViewer table = new TableViewer(tableComposite);
	table.addDoubleClickListener(this);
	// table.getTable().setLinesVisible(true);
	table.getTable().setHeaderVisible(true);

	// Set the content
	table.setLabelProvider(new LabelProvider());
	table.setContentProvider(new ServiceFeatureContentProvider());
	table.setInput(Model.getInstance().getAis().getServiceFeatures());

	// Create the identifier column
	TableColumn identifierColumn = new TableColumn(table.getTable(),
		SWT.NONE);
	identifierColumn.setText("Identifier");
	identifierColumn.setResizable(false);

	// Identifier column spans over the whole table
	TableColumnLayout tableColumnLayout = new TableColumnLayout();
	tableColumnLayout.setColumnData(identifierColumn, new ColumnWeightData(
		100));
	tableComposite.setLayout(tableColumnLayout);

	// Set the height of the table
	layoutData.heightHint = (height / 3) * 2;
	table.getTable().setLayoutData(layoutData);
	tableComposite.setLayoutData(layoutData);

	Composite buttonCompo = toolkit.createComposite(composite);
	buttonCompo.setLayout(new RowLayout());

	// Add button to allow the user to add a new entry
	Button addButton = toolkit.createButton(buttonCompo, "Add", SWT.PUSH);
	addButton.addSelectionListener(new SelectionAdapter() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
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

		    // Refresh view
		    table.refresh();
		    parent.getParent().layout();
		}

	    }

	});

	// Delete button to allow the user to delete a entry
	Button delButton = toolkit
		.createButton(buttonCompo, "Delete", SWT.PUSH);
	delButton.addSelectionListener(new SelectionAdapter() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {

		// Check if sth. is selected
		if (table.getTable().getSelectionCount() > 0) {
		    // Delete the entry out of the table and the model
		    int index = table.getTable().getSelectionIndex();
		    table.getTable().remove(index);
		    Model.getInstance().getAis().getServiceFeatures()
			    .remove(index);
		    
		    // Set the dirty flag
		    Model.getInstance().setAISDirty(true);
		    // Refresh view
		    table.refresh();
		    parent.getParent().layout();
		}
	    }

	});
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
	TableViewer viewer = (TableViewer) arg0.getSource();
	Table table = viewer.getTable();

	// Get the selected item
	int index = table.getSelectionIndex();
	TableItem item = table.getItem(index);
	InputDialog dialog = new InputDialog(parentShell,
		"Add Service Feature",
		"Enter the identifier of the Service Feature", item.getText(),
		new ServiceFeatureInputValidator());

	if (dialog.open() == Window.OK) {
	    String result = dialog.getValue();

	    // If it's not the same name than change it and set the dirty flag
	    if (!result.equals(item.getText())) {
		item.setText(result);
		Model.getInstance().getAis().getServiceFeatures().get(index)
			.setIdentifier(result);
		Model.getInstance().setAISDirty(true);
	    }
	}
    }
}
