package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import de.unistuttgart.ipvs.pmp.editor.xml.IssueTranslator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

/**
 * Creates a listener that will display the tool tips for the
 * {@link TableViewer}
 * 
 * @author Thorsten Berberich
 * 
 */
public class TooltipTableListener implements Listener {

    /**
     * Shell to display the tool tip
     */
    Shell tip = null;

    /**
     * Label that display the tool tip text
     */
    Label label = null;

    /**
     * The {@link TableViewer} where to display the tool tips
     */
    TableViewer tableViewer;

    /**
     * Shell of the calling method
     */
    Shell shell;

    public TooltipTableListener(TableViewer tableViewer, Shell shell) {
	this.tableViewer = tableViewer;
	this.shell = shell;
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
	switch (event.type) {
	case SWT.Dispose:
	case SWT.KeyDown:
	case SWT.MouseMove: {

	    // Dispose the tool tip when the mouse is moved
	    if (tip == null) {
		break;
	    }
	    tip.dispose();
	    tip = null;
	    label = null;
	    break;
	}
	case SWT.MouseHover: {

	    // Get the tree item where the mouse is
	    TableItem item = tableViewer.getTable().getItem(
		    new Point(event.x, event.y));

	    // Dispose a old tip if there is one
	    if (tip != null && !tip.isDisposed()) {
		tip.dispose();
	    }

	    if (item != null) {

		// Create a new tool tip if there are issues
		if (!((IIssueLocation) item.getData()).getIssues().isEmpty()) {
		    tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
		    tip.setLayout(new FillLayout());

		    // Create the label inside the tool tip
		    label = new Label(tip, SWT.WRAP);
		    label.setForeground(Display.getCurrent().getSystemColor(
			    SWT.COLOR_INFO_FOREGROUND));
		    label.setBackground(Display.getCurrent().getSystemColor(
			    SWT.COLOR_INFO_BACKGROUND));
		    label.setData("_TABLEITEM", item);

		    // Build the IssueString
		    String issues = new IssueTranslator()
			    .translateIssues(((IIssueLocation) item.getData())
				    .getIssues());
		    label.setText(issues);
		    TooltipTableLabelListener labelListener = new TooltipTableLabelListener(
			    tableViewer);
		    label.addListener(SWT.MouseExit, labelListener);
		    label.addListener(SWT.MouseDown, labelListener);
		    Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		    Rectangle rect = item.getBounds(0);
		    Point pt = tableViewer.getTable().toDisplay(rect.x, rect.y);
		    tip.setBounds(pt.x, pt.y, size.x, size.y);
		    tip.setVisible(true);
		}
	    }
	}
	}
    }

}
