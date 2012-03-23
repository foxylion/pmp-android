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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.tooltips;

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
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;

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
                if (this.tip == null) {
                    break;
                }
                this.tip.dispose();
                this.tip = null;
                this.label = null;
                break;
            }
            case SWT.MouseHover: {
                // Get the tree item where the mouse is
                TreeItem item = this.treeViewer.getTree().getItem(new Point(event.x, event.y));
                
                // Dispose a old tip if there is one
                if (this.tip != null && !this.tip.isDisposed()) {
                    this.tip.dispose();
                }
                
                if (item != null) {
                    // Get data used to gather the issues from
                    Object data = item.getData();
                    if (data instanceof IIdentifierIS) {
                        if (!((IIdentifierIS) data).getIssues().isEmpty()) {
                            createTooltip((IIdentifierIS) data, item);
                        }
                    } else if (data instanceof EncapsulatedString) {
                        IIdentifierIS identifier = ((EncapsulatedString) data).getPrivacySetting();
                        if (!identifier.getIssues().isEmpty()) {
                            createTooltip(identifier, item);
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * Creates a new tool tip, call this only if the {@link List} of {@link Issue}s is not empty
     * 
     * @param identifier
     *            the item with the issues list
     * @param item
     *            the {@link TreeItem}
     */
    private void createTooltip(IIdentifierIS identifier, TreeItem item) {
        this.tip = new Shell(this.shell, SWT.ON_TOP | SWT.TOOL);
        this.tip.setLayout(new FillLayout());
        
        // Create the label inside the tool tip
        this.label = new Label(this.tip, SWT.WRAP);
        this.label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        this.label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        this.label.setData("_TREEITEM", item); //$NON-NLS-1$
        
        // Build the IssueString
        String issues = new IssueTranslator().translateIssues(identifier.getIssues());
        this.label.setText(issues);
        TooltipTreeLabelListener labelListener = new TooltipTreeLabelListener(this.treeViewer);
        this.label.addListener(SWT.MouseExit, labelListener);
        this.label.addListener(SWT.MouseDown, labelListener);
        Point size = this.tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Rectangle rect = item.getBounds(0);
        Point pt = this.treeViewer.getTree().toDisplay(rect.x, rect.y);
        this.tip.setBounds(pt.x, pt.y, size.x, size.y);
        this.tip.setVisible(true);
    }
    
}
