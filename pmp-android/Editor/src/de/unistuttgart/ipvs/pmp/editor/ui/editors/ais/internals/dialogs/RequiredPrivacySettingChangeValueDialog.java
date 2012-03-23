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

import java.lang.reflect.Array;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.ipvs.pmp.editor.util.I18N;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;

/**
 * Lets the user change the value of a {@link AISRequiredPrivacySetting}
 * 
 * @author Thorsten Berberich
 * 
 */
public class RequiredPrivacySettingChangeValueDialog extends Dialog implements ModifyListener, SelectionListener {
    
    /**
     * Locale field that was entered
     */
    private Text valueText;
    
    /**
     * The empty value check button
     */
    private Button checked;
    
    /**
     * The Strings that could be set
     */
    private String value;
    
    /**
     * Flag to indicate the empty value of the dialog
     */
    private Boolean emptyValue;
    
    /**
     * The result
     */
    private Object[] result;
    
    /**
     * Message that is shown
     */
    private String message;
    
    
    /**
     * Creates the dialog
     * 
     * @param parentShell
     *            {@link Shell} to display
     * @param value
     *            value to display
     * @param message
     *            message to display
     * @param result
     *            String {@link Array} to return the result
     * @param emptyValue
     *            true if the empty value was set
     */
    public RequiredPrivacySettingChangeValueDialog(Shell parentShell, String value, String message, Object[] result,
            Boolean emptyValue) {
        super(parentShell);
        this.message = message;
        this.value = value;
        this.result = result;
        this.emptyValue = emptyValue;
    }
    
    
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(I18N.editor_ais_sf_pschangevaluedialog_title);
    }
    
    
    @Override
    protected Control createDialogArea(Composite parent) {
        // create composite
        Composite composite = (Composite) super.createDialogArea(parent);
        
        Label label = new Label(composite, SWT.WRAP);
        label.setText(this.message);
        
        /*
         * Layout the message with many things
         */
        GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_CENTER);
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        label.setLayoutData(data);
        label.setFont(parent.getFont());
        
        // Create a new composite with a gridlayout
        Composite textComposite = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout(3, false);
        gridLayout.verticalSpacing = 9;
        textComposite.setLayout(gridLayout);
        
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        textComposite.setLayoutData(data);
        
        // The label and text field for the value
        new Label(textComposite, SWT.FILL).setText(I18N.general_value + ":"); //$NON-NLS-1$
        this.valueText = new Text(textComposite, SWT.SINGLE | SWT.BORDER);
        this.valueText.addModifyListener(this);
        this.valueText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        
        // The button for the empty value
        this.checked = new Button(textComposite, SWT.CHECK);
        this.checked.setText(I18N.editor_ais_sf_pschangevaluedialog_emptyvalue);
        this.checked.addSelectionListener(this);
        
        applyDialogFont(composite);
        return composite;
    }
    
    
    @Override
    public void okPressed() {
        // The empty value boolean
        result[0] = checked.getSelection();
        
        // Set the value to an empty string if the empty value is checked
        if (checked.getSelection()) {
            String empty = ""; //$NON-NLS-1$
            result[1] = empty;
        } else {
            // Set the value as result
            result[1] = valueText.getText();
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
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        
        /*
         * Add the value to the dialog, or enable the empty value checkbox and
         * disable the text field
         */
        if (emptyValue) {
            this.checked.setSelection(true);
            this.valueText.setEnabled(false);
        } else if (this.value != null) {
            this.valueText.setFocus();
        }
        
        if (this.value != null) {
            this.valueText.setText(this.value);
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
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
     * .swt.events.SelectionEvent)
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
     * .events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent arg0) {
        
        // Dis- and enables the texfield
        if (this.checked.getSelection()) {
            this.valueText.setEnabled(false);
        } else {
            this.valueText.setEnabled(true);
            this.valueText.setFocus();
        }
    }
}
