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

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.RgisModel;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable.ILocaleTableAction;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable.LocaleTable;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable.LocaleTable.Type;
import de.unistuttgart.ipvs.pmp.editor.xml.IssueTranslator;
import de.unistuttgart.ipvs.pmp.editor.xml.RGISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * General-Page of the RGIS-Editor
 * 
 * @author Patrick Strobel
 * 
 */
public class GeneralPage extends FormPage {

    public static final String ID = "rgis_general";
    private ControlDecoration classnameDec;
    private ControlDecoration iconDec;
    private ControlDecoration identifierDec;
    private final RgisModel model;

    public GeneralPage(FormEditor parent, RgisModel model) {
	super(parent, ID, "General");
	this.model = model;
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
	ScrolledForm form = managedForm.getForm();
	FormToolkit toolkit = managedForm.getToolkit();
	form.setText("Defines general information");

	form.getBody().setLayout(new GridLayout(1, false));

	addPropertiesSection(form.getBody(), toolkit);
	addLocalizationSection(form.getBody(), toolkit);

    }

    private void addPropertiesSection(Composite parent, FormToolkit toolkit) {
	// Set the section's parameters
	Section section = createSection(parent, "Preferences", toolkit);
	IssueTranslator it = new IssueTranslator();

	// Create elements stored inside this section
	Composite client = toolkit.createComposite(section, SWT.WRAP);

	client.setLayout(new GridLayout(2, false));

	GridData textLayout = new GridData();
	textLayout.horizontalAlignment = GridData.FILL;
	textLayout.grabExcessHorizontalSpace = true;

	final IRGIS rgis = model.getRgis();

	// Classname
	toolkit.createLabel(client, "Class name");
	final Text classname = toolkit.createText(client, rgis.getClassName());
	classname.setLayoutData(textLayout);
	classname.addFocusListener(new FocusListener() {

	    private String before;

	    @Override
	    public void focusLost(FocusEvent e) {
		String input = classname.getText();
		if (!input.equals(before)) {
		    rgis.setClassName(input);
		    setDirty(true);
		    validatePreferences();
		}
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		before = classname.getText();

	    }
	});
	classnameDec = new ControlDecoration(classname, SWT.TOP | SWT.RIGHT);
	classnameDec.setImage(Images.ERROR_DEC);
	classnameDec.setDescriptionText(it
		.getTranslationWithoutParameters(IssueType.CLASSNAME_MISSING));

	// Icon
	toolkit.createLabel(client, "Icon");
	final Text icon = toolkit.createText(client, rgis.getIconLocation());
	icon.setLayoutData(textLayout);
	icon.addFocusListener(new FocusListener() {

	    private String before;

	    @Override
	    public void focusLost(FocusEvent e) {
		String input = icon.getText();
		if (!input.equals(before)) {
		    rgis.setIconLocation(input);
		    setDirty(true);
		    validatePreferences();
		}
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		before = icon.getText();

	    }
	});
	iconDec = new ControlDecoration(icon, SWT.TOP | SWT.RIGHT);
	iconDec.setImage(Images.ERROR_DEC);
	iconDec.setDescriptionText(it.getTranslationWithoutParameters(IssueType.ICON_MISSING));

	// Identifier
	toolkit.createLabel(client, "Identifier");
	final Text identifier = toolkit
		.createText(client, rgis.getIdentifier());
	identifier.addFocusListener(new FocusListener() {

	    private String before;

	    @Override
	    public void focusLost(FocusEvent e) {
		String input = identifier.getText();
		if (!input.equals(before)) {
		    rgis.setIdentifier(input);
		    setDirty(true);
		    validatePreferences();
		}
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		before = identifier.getText();

	    }
	});
	identifier.setLayoutData(textLayout);
	identifierDec = new ControlDecoration(identifier, SWT.TOP | SWT.RIGHT);
	identifierDec.setImage(Images.ERROR_DEC);
	identifierDec.setDescriptionText(it
		.getTranslationWithoutParameters(IssueType.IDENTIFIER_MISSING));

	validatePreferences();

	section.setClient(client);
    }

    private void addLocalizationSection(Composite parent, FormToolkit toolkit) {
	// Set the section's parameters
	Section section = createSection(parent, "Localization", toolkit);

	// Create elements stored inside this section
	Composite client = toolkit.createComposite(section);

	client.setLayout(new GridLayout(2, false));

	GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.verticalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;
	layoutData.grabExcessVerticalSpace = true;

	client.setLayoutData(layoutData);
	section.setLayoutData(layoutData);

	// Defines action that should be done when tables are dirty
	ILocaleTableAction dirtyAction = new ILocaleTableAction() {

	    @Override
	    public void doSetDirty(boolean dirty) {
		setDirty(true);
	    }

	    @Override
	    public void doValidate() {
		RGISValidatorWrapper validator = RGISValidatorWrapper
			.getInstance();
		validator.validateRGIS(model.getRgis(), true);
	    }
	};
	IRGIS rgis = model.getRgis();
	LocaleTable nameTable = new LocaleTable(client, rgis, Type.NAME,
		dirtyAction, toolkit);
	nameTable.getComposite().setLayoutData(layoutData);
	// section.setClient(nameTable.getComposite());

	LocaleTable descTable = new LocaleTable(client, rgis, Type.DESCRIPTION,
		dirtyAction, toolkit);
	descTable.getComposite().setLayoutData(layoutData);

	section.setClient(client);
    }

    /**
     * Creates a default section which spans over the whole editor
     * 
     * @param parent
     * @param title
     * @param toolkit
     * @return
     */
    private Section createSection(Composite parent, String title,
	    FormToolkit toolkit) {
	Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE
		| ExpandableComposite.TITLE_BAR);
	section.setText(title);
	section.setExpanded(true);

	GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;

	section.setLayoutData(layoutData);

	return section;
    }

    private void setDirty(boolean dirty) {
	model.setDirty(dirty);
    }

    private void validatePreferences() {
	RGISValidatorWrapper validator = RGISValidatorWrapper.getInstance();
	IRGIS rgis = model.getRgis();
	validator.validateRGIS(rgis, true);

	// Remove error images if set
	classnameDec.hide();
	iconDec.hide();
	identifierDec.hide();

	// Set error images if there is an issue
	for (IIssue i : rgis.getIssues()) {
	    switch (i.getType()) {
	    case CLASSNAME_MISSING:
		classnameDec.show();
		break;
	    case ICON_MISSING:
		iconDec.show();
		break;
	    case IDENTIFIER_MISSING:
		identifierDec.show();
		break;
	    }
	}
    }

}
