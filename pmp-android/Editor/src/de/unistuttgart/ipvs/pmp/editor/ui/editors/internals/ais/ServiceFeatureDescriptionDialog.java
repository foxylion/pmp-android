package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
     * Name field that was entered
     */
    Text nameText;

    /**
     * Description field that was entered
     */
    Text descText;

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
    String name;
    String description;
    
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
	    String name, String desc, HashMap<String, String> values) {
	super(parentShell);
	this.locale = locale;
	this.name = name;
	this.description = desc;
	this.values = values;
    }

    @Override
    protected void configureShell(Shell shell) {
	super.configureShell(shell);
	shell.setText("Add Attribute");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
	// create composite
	Composite composite = (Composite) super.createDialogArea(parent);

	// Create message
	String message = "Add the attributes for the Service Feature";

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

	new Label(textComposite, SWT.FILL).setText("Name: ");
	nameText = new Text(textComposite, SWT.SINGLE | SWT.BORDER);
	nameText.addModifyListener(this);
	nameText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		| GridData.HORIZONTAL_ALIGN_FILL));

	new Label(textComposite, SWT.FILL).setText("Description: ");
	descText = new Text(textComposite, SWT.SINGLE | SWT.BORDER);
	descText.addModifyListener(this);
	descText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
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

	if (nameText.getText() != null) {
	    values.put("name", nameText.getText());
	}

	if (descText.getText() != null) {
	    values.put("description", descText.getText());
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

	if (name != null) {
	    nameText.setText(name);
	}

	if (description != null) {
	    descText.setText(description);
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
	if (localeText.getText().isEmpty() && nameText.getText().isEmpty()
		&& descText.getText().isEmpty()) {
	    errorMessageText.setVisible(true);
	    okButton.setEnabled(false);
	} else {
	    errorMessageText.setVisible(false);
	    okButton.setEnabled(true);
	}
    }
}
