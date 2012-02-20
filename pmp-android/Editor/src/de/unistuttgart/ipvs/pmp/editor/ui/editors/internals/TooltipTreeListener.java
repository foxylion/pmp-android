package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.internal.EncapsulatedString;
import de.unistuttgart.ipvs.pmp.editor.xml.IssueTranslator;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

/**
 * Creates a listener that will display the tool tips for the {@link TreeViewer}
 * 
 * @author Thorsten Berberich
 * 
 */
public class TooltipTreeListener implements Listener {

	/**
	 * Shell to display the tool tip
	 */
	Shell tip = null;

	/**
	 * Label that display the tool tip text
	 */
	Label label = null;

	/**
	 * The {@link TreeViewer} where to display the tool tips
	 */
	TreeViewer treeViewer;

	/**
	 * Shell of the calling method
	 */
	Shell shell;

	public TooltipTreeListener(TreeViewer treeViewer, Shell shell) {
		this.treeViewer = treeViewer;
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
			TreeItem item = treeViewer.getTree().getItem(
					new Point(event.x, event.y));
			if (item != null) {

				// Dispose a old tip if there is one
				if (tip != null && !tip.isDisposed()) {
					tip.dispose();
				}

				// Get data used to gather the issues from
				Object data = item.getData();
				if (data instanceof IIdentifierIS) {
					createTooltip((IIdentifierIS)data, item);
				} else if (data instanceof EncapsulatedString){
					IIdentifierIS identifier = ((EncapsulatedString)data).getPrivacySetting();
					createTooltip(identifier, item);
				}
				
				// Create a new tool tip if there are issues
				/*if (!identifier.getIssues().isEmpty()) {
					tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
					tip.setLayout(new FillLayout());

					// Create the label inside the tool tip
					label = new Label(tip, SWT.WRAP);
					label.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_INFO_FOREGROUND));
					label.setBackground(Display.getCurrent().getSystemColor(
							SWT.COLOR_INFO_BACKGROUND));
					label.setData("_TREEITEM", item);

					// Build the IssueString
					String issues = new IssueTranslator()
							.translateIssues(((IIssueLocation) item.getData())
									.getIssues());
					label.setText(issues);
					TooltipTreeLabelListener labelListener = new TooltipTreeLabelListener(
							treeViewer);
					label.addListener(SWT.MouseExit, labelListener);
					label.addListener(SWT.MouseDown, labelListener);
					Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
					Rectangle rect = item.getBounds(0);
					Point pt = treeViewer.getTree().toDisplay(rect.x, rect.y);
					tip.setBounds(pt.x, pt.y, size.x, size.y);
					tip.setVisible(true);
				}*/
			}
		}
		}
	}
	
	private void createTooltip(IIdentifierIS identifier, TreeItem item) {
		tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
		tip.setLayout(new FillLayout());

		// Create the label inside the tool tip
		label = new Label(tip, SWT.WRAP);
		label.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_INFO_FOREGROUND));
		label.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_INFO_BACKGROUND));
		label.setData("_TREEITEM", item);

		// Build the IssueString
		String issues = new IssueTranslator()
				.translateIssues(identifier.getIssues());
		label.setText(issues);
		TooltipTreeLabelListener labelListener = new TooltipTreeLabelListener(
				treeViewer);
		label.addListener(SWT.MouseExit, labelListener);
		label.addListener(SWT.MouseDown, labelListener);
		Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = item.getBounds(0);
		Point pt = treeViewer.getTree().toDisplay(rect.x, rect.y);
		tip.setBounds(pt.x, pt.y, size.x, size.y);
		tip.setVisible(true);
	}

}
