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

import java.util.GregorianCalendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.unistuttgart.ipvs.pmp.editor.util.I18N;

/**
 * Lets the user select a date and a time
 * 
 * @author Thorsten Berberich
 * 
 */
public class DateTimeDialog extends Dialog {
    
    /**
     * Array to return the result
     */
    private Long[] result;
    
    /**
     * Calendar to pick a date
     */
    private DateTime calendar;
    
    /**
     * Timepicker
     */
    private DateTime time;
    
    
    /**
     * Creates the dialog
     * 
     * @param parentShell
     *            shell to display a dialog
     * @param result
     *            an Integer array where the result is added to the 0 place
     */
    public DateTimeDialog(Shell parentShell, Long[] result) {
        super(parentShell);
        this.result = result;
    }
    
    
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(I18N.general_datetimedialog_title);
    }
    
    
    @Override
    protected Control createDialogArea(Composite parent) {
        // Create the components
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.verticalSpacing = 9;
        parent.setLayout(gridLayout);
        
        new Label(parent, SWT.NONE).setText(I18N.general_datetimedialog_text);
        
        // Calendar to pick a date
        calendar = new DateTime(parent, SWT.CALENDAR);
        
        // Time picker
        time = new DateTime(parent, SWT.TIME);
        return parent;
    }
    
    
    @Override
    public void okPressed() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(calendar.getYear(), calendar.getMonth(), calendar.getDay(), time.getHours(), time.getMinutes(),
                time.getSeconds());
        result[0] = cal.getTimeInMillis();
        this.close();
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
    }
    
}
