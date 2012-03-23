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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.ResourceGroupsDialogContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.labelprovider.ResourceGroupDialogLabelProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Shows a dialog where the user can check the required Resource Groups that he
 * wants to add to the Service Feature
 * 
 * @author Thorsten Berberich
 * 
 */
public class RequiredResourceGroupsDialog extends SelectionDialog implements ISelectionChangedListener,
        ICheckStateListener {
    
    /**
     * The {@link RGISPrivacySetting}s to display
     */
    private List<RGIS> toDisplay;
    
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
    
    private Shell shell;
    
    /**
     * The value text field
     */
    private Text valueText;
    
    /**
     * Sizing constants
     */
    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;
    private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;
    
    
    /**
     * Constructor
     * 
     * @param parentShell
     *            {@link Shell} to display the dialog
     * @param toDisplay
     *            {@link List} with {@link RGIS} that are displayed
     */
    public RequiredResourceGroupsDialog(Shell parentShell, List<RGIS> toDisplay) {
        super(parentShell);
        this.toDisplay = toDisplay;
        this.shell = parentShell;
    }
    
    
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Select the required Resource Groups");
    }
    
    
    @Override
    protected Control createDialogArea(final Composite parent) {
        // create composite
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new GridLayout(2, false));
        
        Label psLabel = new Label(composite, SWT.NULL);
        psLabel.setText("Choose the required Resource Groups:");
        
        Label descLabel = new Label(composite, SWT.NULL);
        descLabel.setText("Information:");
        
        // The CheckboxTableViewer for the PrivacySettings
        this.listViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
        
        this.listViewer.addCheckStateListener(this);
        
        // Set the content provider and the label provider
        this.listViewer.setContentProvider(new ResourceGroupsDialogContentProvider());
        this.listViewer.setLabelProvider(new ResourceGroupDialogLabelProvider());
        this.listViewer.setInput(this.toDisplay);
        this.listViewer.addSelectionChangedListener(this);
        
        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 2);
        this.listViewer.getTable().setLayoutData(data);
        
        // The text that displays the description of the PS and the required
        // Value
        this.text = new StyledText(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
        this.text.setLayoutData(data);
        
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
        
        // Composite that holds the value label and text f
        Composite valueComp = new Composite(composite, SWT.BORDER);
        valueComp.setLayout(new GridLayout(3, false));
        valueComp.setLayoutData(data);
        
        Label valueLabel = new Label(valueComp, SWT.NULL);
        valueLabel.setToolTipText("Format: milliseconds since 1970, or a date with the following format:\n"
                + "yyyy-MM-dd HH:mm:ss:SSS z");
        valueLabel.setText("Minimal revision:");
        valueLabel.pack();
        
        this.valueText = new Text(valueComp, SWT.BORDER);
        this.valueText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        this.valueText.pack();
        
        Button picker = new Button(valueComp, SWT.ICON);
        picker.setToolTipText("Show a calendar to choose a date");
        picker.setImage(Images.getImageDescriptor("icons", "history_working_set_obj.gif").createImage());
        picker.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                Long[] result = new Long[1];
                
                // Open a dialog with the time and date picker
                if (new DateTimeDialog(shell, result).open() == Dialog.OK) {
                    
                    // Get the display rg
                    RGIS ps = (RGIS) RequiredResourceGroupsDialog.this.listViewer.getTable().getSelection()[0]
                            .getData();
                    
                    // Store it at the hash map
                    RequiredResourceGroupsDialog.this.values.put(ps.getIdentifier(),
                            XMLConstants.REVISION_DATE_FORMAT.format(new Date(result[0])));
                    updateText();
                }
            }
            
            
            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                
            }
        });
        
        // FocusListener to store the entered value
        this.valueText.addFocusListener(new org.eclipse.swt.events.FocusListener() {
            
            @Override
            public void focusLost(org.eclipse.swt.events.FocusEvent arg0) {
                
                // Store the value out of the value field
                RGIS ps = (RGIS) RequiredResourceGroupsDialog.this.listViewer.getTable().getSelection()[0].getData();
                if (!RequiredResourceGroupsDialog.this.valueText.getText().isEmpty()) {
                    RequiredResourceGroupsDialog.this.values.put(ps.getIdentifier(),
                            RequiredResourceGroupsDialog.this.valueText.getText());
                }
            }
            
            
            @Override
            public void focusGained(org.eclipse.swt.events.FocusEvent arg0) {
            }
        });
        
        // Set the initial selection and update the text
        if (this.toDisplay.size() > 0) {
            this.listViewer.getTable().select(0);
            updateText();
        }
        
        applyDialogFont(composite);
        return composite;
    }
    
    
    @Override
    protected void okPressed() {
        
        Object[] children = this.listViewer.getCheckedElements();
        
        // Build a list of selected children.
        if (children != null) {
            ArrayList<AISRequiredResourceGroup> list = new ArrayList<AISRequiredResourceGroup>();
            
            for (int i = 0; i < children.length; ++i) {
                RGIS element = (RGIS) children[i];
                if (this.listViewer.getChecked(element)) {
                    String value = "";
                    // Add the entered values
                    if (this.values.get(element.getIdentifier()) != null) {
                        if (!this.values.get(element.getIdentifier()).isEmpty()) {
                            
                            try {
                                Date parsed = XMLConstants.REVISION_DATE_FORMAT.parse(this.values.get(element
                                        .getIdentifier()));
                                value = String.valueOf(parsed.getTime());
                            } catch (ParseException e) {
                                value = this.values.get(element.getIdentifier());
                            }
                            
                        }
                    }
                    AISRequiredResourceGroup required = new AISRequiredResourceGroup(element.getIdentifier(), value);
                    list.add(required);
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
        updateText();
    }
    
    
    /**
     * Updates the description and valid value {@link StyledText}
     */
    private void updateText() {
        RGIS rg = (RGIS) this.listViewer.getTable().getSelection()[0].getData();
        Locale enLocale = new Locale("en");
        
        // Set the value text field
        if (this.values.get(rg.getIdentifier()) != null) {
            this.valueText.setText(this.values.get(rg.getIdentifier()));
        } else {
            this.valueText.setText("");
        }
        
        String nameString = rg.getNameForLocale(enLocale);
        int nameLength = 0;
        if (nameString == null || nameString.isEmpty()) {
            nameString = "No name available";
            nameLength = nameString.length();
        } else {
            nameLength = nameString.length();
        }
        
        String descString = rg.getDescriptionForLocale(enLocale);
        if (descString == null || nameString.isEmpty()) {
            descString = "No description available";
        }
        
        this.text.setText("Name:\n" + nameString + "\n\nDescription:\n" + descString);
        
        // Set the text styles
        StyleRange style = new StyleRange();
        style.start = 0;
        style.length = 4;
        style.fontStyle = SWT.BOLD;
        this.text.setStyleRange(style);
        
        style = new StyleRange();
        style.start = 6 + nameLength;
        style.length = 15;
        style.fontStyle = SWT.BOLD;
        this.text.setStyleRange(style);
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
        RGIS checkedElement = (RGIS) event.getElement();
        
        // Search the item at the list that was checked
        for (int itr = 0; itr < this.listViewer.getTable().getItemCount(); itr++) {
            TableItem item = this.listViewer.getTable().getItem(itr);
            
            // Select the table item and update the text
            if (item.getData().equals(checkedElement)) {
                this.listViewer.getTable().select(itr);
                updateText();
            }
        }
    }
}
