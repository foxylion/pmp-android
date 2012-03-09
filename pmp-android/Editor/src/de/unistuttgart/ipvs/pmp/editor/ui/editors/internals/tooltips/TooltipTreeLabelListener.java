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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Listener to display the labels for the fake tool tips
 * 
 * @author Thorsten Berberich
 * 
 */
public class TooltipTreeLabelListener implements
	org.eclipse.swt.widgets.Listener {

    /**
     * The {@link TreeViewer} where the tool tips are placed
     */
    TreeViewer treeViewer;

    /**
     * Creates a listener to display the labels
     * 
     * @param treeViewer
     *            {@link TreeViewer} where the tool tips are displayed
     */
    public TooltipTreeLabelListener(TreeViewer tree) {
	this.treeViewer = tree;
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
	    e.item = (TreeItem) label.getData("_TREEITEM");
	    treeViewer.getTree().setSelection((TreeItem) e.item);
	    treeViewer.refresh();
	case SWT.MouseExit:
	    shell.dispose();
	    break;
	}

    }

}
