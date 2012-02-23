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
package de.unistuttgart.ipvs.pmp.editor.ui.preferences;

import java.io.IOException;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.unistuttgart.ipvs.pmp.editor.util.ServerProvider;

public class TestButton extends FieldEditor {

    private Label status;
    private Composite parent;

    public TestButton(String label, Composite parent) {
	this.parent = parent;
	createControl(parent);
	setLabelText(label);

    }

    @Override
    protected void adjustForNumColumns(int numColumns) {
	// TODO Auto-generated method stub

    }

    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
	GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	// gd.horizontalSpan = numColumns;
	parent.setLayoutData(gd);

	getLabelControl(parent);

	status = new Label(parent, SWT.NONE);
	status.setText("Unknown (Press \"Apply\" before testing/updating)");
	GridData layout = new GridData();
	layout.grabExcessHorizontalSpace = true;
	status.setLayoutData(layout);

	Button button = new Button(parent, SWT.PUSH);
	button.setText("Test/Update");
	button.addSelectionListener(new SelectionAdapter() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		updateStatusLabel("Connecting...", SWT.COLOR_WIDGET_FOREGROUND);

		new Thread() {
		    @Override
		    public void run() {
			ServerProvider server = ServerProvider.getInstance();
			try {
			    server.updateResourceGroupList();
			    updateStatusLabel("OK (List updated)",
				    SWT.COLOR_DARK_GREEN);
			} catch (IOException ioe) {
			    updateStatusLabel(
				    "Failed (" + ioe.getLocalizedMessage()
					    + ")", SWT.COLOR_RED);
			}

		    }
		}.start();

	    }
	});
    }

    private void updateStatusLabel(final String text, final int color) {
	parent.getDisplay().syncExec(new Runnable() {

	    @Override
	    public void run() {
		status.setText(text);
		status.setForeground(Display.getCurrent().getSystemColor(color));
		status.getParent().layout();
	    }

	});
    }

    @Override
    protected void doLoad() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void doLoadDefault() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void doStore() {
	// TODO Auto-generated method stub

    }

    @Override
    public int getNumberOfControls() {
	return 3;
    }

}
