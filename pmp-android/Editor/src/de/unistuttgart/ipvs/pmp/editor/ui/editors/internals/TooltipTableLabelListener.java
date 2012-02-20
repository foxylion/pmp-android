package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

/**
 * Listener to display the labels for the fake tool tips
 * 
 * @author Thorsten Berberich
 * 
 */
public class TooltipTableLabelListener implements
	org.eclipse.swt.widgets.Listener {

    /**
     * The {@link TableViewer} where the tool tips are placed
     */
    TableViewer tableViewer;

    /**
     * Creates a listener to display the labels
     * 
     * @param treeViewer
     *            {@link TableViewer} where the tool tips are displayed
     */
    public TooltipTableLabelListener(TableViewer table) {
	this.tableViewer = table;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
     * Event)
     */
    @Override
    public void handleEvent(Event event) {
	Label label = (Label) event.widget;
	Shell shell = label.getShell();
	switch (event.type) {
	case SWT.MouseDown:
	    Event e = new Event();
	    e.item = (TableItem) label.getData("_TABLEITEM");
	    tableViewer.getTable().setSelection((TableItem) e.item);
	    tableViewer.refresh();
	case SWT.MouseExit:
	    shell.dispose();
	    break;
	}

    }

}
