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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.PrivacySettingsDialogContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.labelprovider.PrivacySettingsDialogLabelProvider;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Shows a dialog where the user can check the {@link RGISPrivacySetting}s that
 * he wants to add to the Service Feature
 * 
 * @author Thorsten Berberich
 * 
 */
public class RequiredPrivacySettingsDialog extends SelectionDialog implements
	ISelectionChangedListener, ICheckStateListener {

    /**
     * The {@link RGISPrivacySetting}s to display
     */
    private RGIS toDisplay;

    /**
     * The text that is display on the left hand side
     */
    private StyledText text;
    /**
     * The list with the PrivacySettings
     */
    private CheckboxTableViewer listViewer;

    /**
     * Stores the entered value
     */
    private HashMap<String, String> values = new HashMap<String, String>();

    /**
     * Stores if the value is intended to be empty
     */
    private HashMap<String, Boolean> valuesEmpty = new HashMap<String, Boolean>();

    /**
     * The value text field
     */
    private Text valueText;

    /**
     * The checkbox that indicates if the value should be empty
     */
    Button emptyValue;

    /**
     * Sizing constants
     */
    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;
    private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

    /**
     * Constructr
     * 
     * @param parentShell
     *            {@link Shell} to display the dialog
     * @param toDisplay
     *            one {@link RGIS} to display the {@link RGISPrivacySetting} out
     *            of
     */
    public RequiredPrivacySettingsDialog(Shell parentShell, RGIS toDisplay) {
	super(parentShell);
	this.toDisplay = toDisplay;
    }

    @Override
    protected void configureShell(Shell shell) {
	super.configureShell(shell);
	shell.setText("Select the required Privacy Settings");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
	// create composite
	Composite composite = (Composite) super.createDialogArea(parent);
	composite.setLayout(new GridLayout(2, false));

	Label psLabel = new Label(composite, SWT.NULL);
	psLabel.setText("Choose the required Privacy Settings:");

	Label descLabel = new Label(composite, SWT.NULL);
	descLabel.setText("Information:");

	// The CheckboxTableViewer for the PrivacySettings
	listViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER
		| SWT.SINGLE | SWT.FULL_SELECTION);

	listViewer.addCheckStateListener(this);

	// Set the content provider and the label provider
	PrivacySettingsDialogContentProvider contentProvider = new PrivacySettingsDialogContentProvider();
	listViewer.setContentProvider(contentProvider);
	listViewer.setLabelProvider(new PrivacySettingsDialogLabelProvider());
	listViewer.setInput(toDisplay);
	listViewer.addSelectionChangedListener(this);

	GridData data = new GridData(GridData.FILL_BOTH);
	data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
	data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 2);
	listViewer.getTable().setLayoutData(data);

	// The text that displays the description of the PS and the required
	// Value
	text = new StyledText(composite, SWT.BORDER | SWT.H_SCROLL
		| SWT.V_SCROLL | SWT.WRAP);
	text.setLayoutData(data);

	data = new GridData(GridData.FILL_HORIZONTAL);
	data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;

	// Composite that holds the value label and text f
	Composite valueComp = new Composite(composite, SWT.BORDER);
	valueComp.setLayout(new GridLayout(2, false));
	valueComp.setLayoutData(data);

	Label valueLabel = new Label(valueComp, SWT.NULL);
	valueLabel.setText("Value:");
	valueLabel.pack();

	valueText = new Text(valueComp, SWT.BORDER);
	valueText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		| GridData.HORIZONTAL_ALIGN_FILL));
	valueText.pack();

	// FocusListener to store the entered value
	valueText.addFocusListener(new org.eclipse.swt.events.FocusListener() {

	    @Override
	    public void focusLost(org.eclipse.swt.events.FocusEvent arg0) {

		// Store the value out of the value field
		RGISPrivacySetting ps = (RGISPrivacySetting) listViewer
			.getTable().getSelection()[0].getData();
		if (!valueText.getText().isEmpty()) {
		    values.put(ps.getIdentifier(), valueText.getText());
		}
	    }

	    @Override
	    public void focusGained(org.eclipse.swt.events.FocusEvent arg0) {
	    }
	});

	new Label(valueComp, SWT.None).setVisible(false);

	emptyValue = new Button(valueComp, SWT.CHECK);
	emptyValue.setText("empty value");
	emptyValue.addListener(SWT.Selection, new Listener() {

	    @Override
	    public void handleEvent(Event event) {
		// Store the value out of the value field
		RGISPrivacySetting ps = (RGISPrivacySetting) listViewer
			.getTable().getSelection()[0].getData();
		if (emptyValue.getSelection()) {
		    valuesEmpty.put(ps.getIdentifier(), true);
		    valueText.setEnabled(false);
		} else {
		    valuesEmpty.put(ps.getIdentifier(), false);
		    valueText.setEnabled(true);
		}
	    }
	});

	// Set the initial selection and update the text
	if (toDisplay.getPrivacySettings().size() > 0) {
	    listViewer.getTable().select(0);
	    updateText();
	}

	applyDialogFont(composite);
	return composite;
    }

    @Override
    protected void okPressed() {

	Object[] children = listViewer.getCheckedElements();

	// Build a list of selected children.
	if (children != null) {
	    ArrayList<AISRequiredPrivacySetting> list = new ArrayList<AISRequiredPrivacySetting>();
	    for (int i = 0; i < children.length; ++i) {

		RGISPrivacySetting element = (RGISPrivacySetting) children[i];
		if (listViewer.getChecked(element)) {
		    String value = null;
		    // Add the entered values
		    Boolean empty = valuesEmpty.get(element.getIdentifier());

		    // Check if the empty value was set
		    if (empty != null) {
			if (empty) {
			    value = "";
			} else if (values.get(element.getIdentifier()) != null) {
			    if (!values.get(element.getIdentifier()).isEmpty()) {
				value = values.get(element.getIdentifier());
			    }
			}
		    }
		    list.add(new AISRequiredPrivacySetting(element
			    .getIdentifier(), value));
		}
	    }
	    setResult(list);
	}
	super.okPressed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
     * org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event) {
	RGISPrivacySetting ps = (RGISPrivacySetting) listViewer.getTable()
		.getSelection()[0].getData();
	Boolean empty = valuesEmpty.get(ps.getIdentifier());

	// Check if the empty button was checked
	if (empty == null || !empty) {
	    emptyValue.setSelection(false);
	    valueText.setEnabled(true);
	    updateText();
	} else {
	    emptyValue.setSelection(true);
	    valueText.setEnabled(false);
	    updateText();
	}
    }

    /**
     * Updates the description and valid value {@link StyledText}
     */
    private void updateText() {
	RGISPrivacySetting ps = (RGISPrivacySetting) listViewer.getTable()
		.getSelection()[0].getData();
	Locale enLocale = new Locale("en");

	// Set the value text field
	if (values.get(ps.getIdentifier()) != null) {
	    valueText.setText(values.get(ps.getIdentifier()));
	} else {
	    valueText.setText("");
	}

	String descString = ps.getDescriptionForLocale(enLocale);
	int descLength = 0;
	if (descString == null) {
	    descString = "No description available";
	    descLength = descString.length();
	} else {
	    descLength = ps.getDescriptionForLocale(enLocale).length();
	}

	String validvalueString = ps.getValidValueDescription();
	if (validvalueString.isEmpty()) {
	    validvalueString = "No valid value description available";
	}

	text.setText("Description:\n" + descString + "\n\nValid Values:\n"
		+ validvalueString);

	// Set the text styles
	StyleRange style = new StyleRange();
	style.start = 0;
	style.length = 12;
	style.fontStyle = SWT.BOLD;
	text.setStyleRange(style);

	style = new StyleRange();
	style.start = 13 + descLength;
	style.length = 15;
	style.fontStyle = SWT.BOLD;
	text.setStyleRange(style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged(org.eclipse
     * .jface.viewers.CheckStateChangedEvent)
     */
    @Override
    public void checkStateChanged(CheckStateChangedEvent event) {
	RGISPrivacySetting checkedElement = (RGISPrivacySetting) event
		.getElement();

	// Search the item at the list that was checked
	for (int itr = 0; itr < listViewer.getTable().getItemCount(); itr++) {
	    TableItem item = listViewer.getTable().getItem(itr);

	    // Select the table item and update the text
	    if (item.getData().equals(checkedElement)) {
		listViewer.getTable().select(itr);
		updateText();
	    }
	}
    }
}
