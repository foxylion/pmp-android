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

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

/**
 * The dialog that lets the user enter the name, locale and description for the
 * {@link AISServiceFeature}
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureDescriptionDialog extends Dialog implements
	ModifyListener {

    /**
     * Locale field that was entered
     */
    Text localeText;

    /**
     * Description field that was entered
     */
    Text fieldText;

    /**
     * The error text
     */
    Text errorMessageText;

    /**
     * The okButton
     */
    Button okButton;

    /**
     * The Strings that could be set
     */
    String locale;
    String description;
    String title;
    
    /**
     * Change or add
     */
    String type;

    /**
     * The entered values
     */
    HashMap<String, String> values;

    /**
     * The Constructor for this dialog
     * 
     * @param parentShell
     *            {@link Shell} to display
     */
    public ServiceFeatureDescriptionDialog(Shell parentShell, String locale,
	    String desc, HashMap<String, String> values, String title, String type) {
	super(parentShell);
	this.locale = locale;
	this.description = desc;
	this.values = values;
	this.title = title;
	this.type = type;
    }

    @Override
    protected void configureShell(Shell shell) {
	super.configureShell(shell);
	shell.setText(type +" " + title);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
	// create composite
	Composite composite = (Composite) super.createDialogArea(parent);

	// Create message
	String message = type +" the " +  title.toLowerCase() + " for the Service Feature";

	Label label = new Label(composite, SWT.WRAP);
	label.setText(message);

	/*
	 * Layout the message with many things
	 */
	GridData data = new GridData(GridData.GRAB_HORIZONTAL
		| GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
		| GridData.VERTICAL_ALIGN_CENTER);
	data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
	label.setLayoutData(data);
	label.setFont(parent.getFont());

	// Create a new composite with a gridlayout
	Composite textComposite = new Composite(parent, SWT.NULL);
	GridLayout gridLayout = new GridLayout(2, false);
	gridLayout.verticalSpacing = 9;
	textComposite.setLayout(gridLayout);

	// Add a label and a textfield for the 3 attributes
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
	textComposite.setLayoutData(data);

	new Label(textComposite, SWT.FILL).setText("Locale: ");
	localeText = new Text(textComposite, SWT.SINGLE | SWT.BORDER);
	localeText.addModifyListener(this);
	localeText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		| GridData.HORIZONTAL_ALIGN_FILL));

	new AutoCompleteField(localeText, new TextContentAdapter(),
		Locale.getISOLanguages());

	new Label(textComposite, SWT.FILL).setText(title);
	fieldText = new Text(textComposite, SWT.SINGLE | SWT.BORDER);
	fieldText.addModifyListener(this);
	fieldText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		| GridData.HORIZONTAL_ALIGN_FILL));

	// The error message
	new Label(textComposite, SWT.NULL).setVisible(false);
	errorMessageText = new Text(textComposite, SWT.READ_ONLY | SWT.WRAP);
	errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		| GridData.HORIZONTAL_ALIGN_FILL));
	errorMessageText.setBackground(errorMessageText.getDisplay()
		.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
	errorMessageText.setText("All fields could not be empty");

	applyDialogFont(composite);
	return composite;
    }

    @Override
    public void okPressed() {
	if (localeText.getText() != null) {
	    values.put("locale", localeText.getText());
	}

	if (fieldText.getText() != null) {
	    values.put(title, fieldText.getText());
	}
	close();
    }

    /*
     * (non-Javadoc) @see
     * org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar
     * (org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
	// create OK and Cancel buttons by default
	okButton = createButton(parent, IDialogConstants.OK_ID,
		IDialogConstants.OK_LABEL, true);
	okButton.setEnabled(false);
	createButton(parent, IDialogConstants.CANCEL_ID,
		IDialogConstants.CANCEL_LABEL, false);
	localeText.setFocus();

	if (locale != null) {
	    localeText.setText(locale);
	}

	if (description != null) {
	    fieldText.setText(description);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
     * .ModifyEvent)
     */
    @Override
    public void modifyText(ModifyEvent arg0) {
	if (localeText.getText().isEmpty() && fieldText.getText().isEmpty()) {
	    errorMessageText.setVisible(true);
	    okButton.setEnabled(false);
	} else {
	    errorMessageText.setVisible(false);
	    okButton.setEnabled(true);
	}
    }
}
