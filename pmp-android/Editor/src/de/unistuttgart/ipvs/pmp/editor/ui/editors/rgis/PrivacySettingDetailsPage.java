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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Defines the details page that allows the user to edit the privacy setting's
 * identifier and valid values
 * 
 * @author Patrick Strobel
 */
public class PrivacySettingDetailsPage implements IDetailsPage {

    private final PrivacySettingsBlock block;
    private IManagedForm form;
    private Text identifier;
    private Text values;
    private boolean dirty = false;
    private RGISPrivacySetting privacySetting;

    public PrivacySettingDetailsPage(PrivacySettingsBlock block) {
	this.block = block;
    }

    @Override
    public void initialize(IManagedForm form) {
	this.form = form;
    }

    @Override
    public void createContents(Composite parent) {
	// Set parent's layout
	GridData parentLayout = new GridData();
	parentLayout.verticalAlignment = GridData.FILL;
	parentLayout.grabExcessVerticalSpace = true;
	parentLayout.horizontalAlignment = GridData.FILL;
	parentLayout.grabExcessHorizontalSpace = true;
	parent.setLayout(new GridLayout());
	// parent.setLayoutData(parentLayout);

	// Build view
	// System.out.println("Draw");
	FormToolkit toolkit = form.getToolkit();
	Section section = toolkit.createSection(parent,
		ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR);
	section.setText("Privacy Setting");
	section.setExpanded(true);
	section.setLayoutData(parentLayout);

	// Add Textfields
	Composite compo = toolkit.createComposite(section);
	compo.setLayout(new GridLayout(2, false));
	GridData textLayout = new GridData();
	textLayout.horizontalAlignment = GridData.FILL;
	textLayout.grabExcessHorizontalSpace = true;
	toolkit.createLabel(compo, "Identifier");
	identifier = toolkit.createText(compo, "Value");
	identifier.setLayoutData(textLayout);
	identifier.addFocusListener(new FocusListener() {

	    private String before;

	    @Override
	    public void focusLost(FocusEvent e) {
		// Mark as dirty and staled when text has been changed
		if (!before.equals(identifier.getText())) {
		    dirty = true;
		}
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		before = identifier.getText();
	    }
	});
	toolkit.createLabel(compo, "Valid values");
	values = toolkit.createText(compo, "True/False");
	values.setLayoutData(textLayout);
	values.addFocusListener(new FocusListener() {

	    private String before;

	    @Override
	    public void focusLost(FocusEvent e) {
		// Mark as dirty and staled when text has been changed
		if (!before.equals(values.getText())) {
		    dirty = true;
		}
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		before = values.getText();
	    }
	});
	section.setClient(compo);

    }

    @Override
    public void dispose() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean isDirty() {
	return dirty;
    }

    @Override
    public void commit(boolean onSave) {

	privacySetting.setIdentifier(identifier.getText());
	privacySetting.setValidValueDescription(values.getText());
	block.refresh();
	dirty = false;

	// Mark page as dirty
	block.setDirty(true);

    }

    @Override
    public boolean setFormInput(Object input) {
	return false;
    }

    @Override
    public void setFocus() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean isStale() {
	return false;
    }

    @Override
    public void refresh() {
	update();
    }

    @Override
    public void selectionChanged(IFormPart part, ISelection selection) {
	privacySetting = (RGISPrivacySetting) ((TreeSelection) selection)
		.getFirstElement();
	update();
    }

    private void update() {
	identifier.setText(privacySetting.getIdentifier());
	values.setText(privacySetting.getValidValueDescription());
    }

}
