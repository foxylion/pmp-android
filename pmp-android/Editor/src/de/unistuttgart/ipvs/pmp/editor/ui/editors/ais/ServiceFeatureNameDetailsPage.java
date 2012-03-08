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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.AisModel;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ILocaleTableAction;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.LocaleTable;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

/**
 * Shows the lists for the names and the descriptions of a service feature
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureNameDetailsPage implements IDetailsPage {

    /**
     * ID of this page
     */
    public static final String ID = "ais_service_feature_names";

    /**
     * Given form
     */
    private IManagedForm form;

    /**
     * The model of this editor instance
     */
    private final AisModel model;

    /**
     * The {@link LocaleTable} for the descriptions
     */
    private LocaleTable descriptionTable;

    /**
     * The {@link LocaleTable} for the names
     */
    private LocaleTable nameTable;

    /**
     * The tree of the {@link ServiceFeatureMasterBlock}
     */
    private TreeViewer parentTree;

    /**
     * The displayed ServiceFeature
     */
    AISServiceFeature displayed;

    /**
     * Constructor to get the model and the tree to refresh it
     * 
     * @param model
     *            model of this instance
     * @param parentTree
     *            the {@link TreeViewer} of the parent view to refresh it
     */
    public ServiceFeatureNameDetailsPage(AisModel model, TreeViewer parentTree) {
	this.model = model;
	this.parentTree = parentTree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm
     * )
     */
    @Override
    public void initialize(IManagedForm arg0) {
	form = arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createContents(Composite parent) {

	// Set parent's layout
	GridData parentLayout = new GridData();
	parentLayout.verticalAlignment = GridData.FILL;
	parentLayout.grabExcessVerticalSpace = true;
	parentLayout.horizontalAlignment = GridData.FILL;
	parentLayout.grabExcessHorizontalSpace = true;
	parent.setLayout(new GridLayout(1, false));

	FormToolkit toolkit = form.getToolkit();

	// The name section
	Section nameSection = toolkit.createSection(parent,
		ExpandableComposite.CLIENT_INDENT
			| ExpandableComposite.TITLE_BAR);
	nameSection.setText("Names");
	nameSection.setLayout(new GridLayout(1, false));
	nameSection.setExpanded(true);
	nameSection.setLayoutData(parentLayout);

	// The description section
	Section descriptionSection = toolkit.createSection(parent,
		ExpandableComposite.CLIENT_INDENT
			| ExpandableComposite.TITLE_BAR);
	descriptionSection.setText("Descriptions");
	descriptionSection.setLayout(new GridLayout(1, false));
	descriptionSection.setExpanded(true);
	descriptionSection.setLayoutData(parentLayout);

	// Composite that is display in the description section
	Composite descriptionComposite = toolkit
		.createComposite(descriptionSection);
	descriptionComposite.setLayout(new GridLayout(1, false));
	descriptionComposite.setLayoutData(parentLayout);

	// Build localization table
	ILocaleTableAction dirtyAction = new ILocaleTableAction() {

	    @Override
	    public void doSetDirty(boolean dirty) {
		model.setDirty(true);

	    }

	    @Override
	    public void doValidate() {
		AISValidatorWrapper.getInstance().validateServiceFeature(
			displayed, true);
		parentTree.refresh();
	    }

	};
	descriptionTable = new LocaleTable(descriptionComposite,
		LocaleTable.Type.DESCRIPTION, dirtyAction, toolkit);

	descriptionTable.getComposite().setLayoutData(parentLayout);

	// Composite that is display in the name section
	Composite nameComposite = toolkit.createComposite(nameSection);
	nameComposite.setLayout(new GridLayout(1, false));
	nameComposite.setLayoutData(parentLayout);
	nameTable = new LocaleTable(nameComposite, LocaleTable.Type.NAME,
		dirtyAction, toolkit);

	nameTable.getComposite().setLayoutData(parentLayout);

	// Set the composites
	descriptionSection.setClient(descriptionComposite);
	nameSection.setClient(nameComposite);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse
     * .ui.forms.IFormPart, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IFormPart arg0, ISelection selection) {
	// Get the selected service feature and set the name
	TreePath[] path = ((TreeSelection) selection).getPaths();
	displayed = (AISServiceFeature) path[0].getFirstSegment();
	descriptionTable.setData(displayed);
	descriptionTable.refresh();

	nameTable.setData(displayed);
	nameTable.refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
     */
    @Override
    public void commit(boolean arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#dispose()
     */
    @Override
    public void dispose() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#isDirty()
     */
    @Override
    public boolean isDirty() {
	return model.isDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#isStale()
     */
    @Override
    public boolean isStale() {
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#refresh()
     */
    @Override
    public void refresh() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#setFocus()
     */
    @Override
    public void setFocus() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
     */
    @Override
    public boolean setFormInput(Object arg0) {
	return false;
    }
}
