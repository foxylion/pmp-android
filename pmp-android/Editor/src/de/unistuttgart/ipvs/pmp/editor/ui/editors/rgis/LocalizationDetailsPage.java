package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import java.util.Locale;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Information;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.InformationTable;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.StoredInformation;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.common.LocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Defines the details page of the privacy setting that allows the user to set
 * the privacy setting's localization
 * 
 * @author Patrick Strobel
 */
public class LocalizationDetailsPage implements IDetailsPage {

    private final PrivacySettingsBlock block;
    private IManagedForm form;
    private InformationTable localizationTable;
    private RGISPrivacySetting privacySetting;

    public LocalizationDetailsPage(PrivacySettingsBlock block) {
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

	// Build view
	FormToolkit toolkit = form.getToolkit();
	Section section = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR);
	section.setText("Localization");
	section.setExpanded(true);
	section.setLayoutData(parentLayout);

	// Build localization table
	localizationTable = new InformationTable(section,
		new StoredInformation(), toolkit);
	section.setClient(localizationTable.getControl());

    }

    @Override
    public void dispose() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean isDirty() {
	return localizationTable.isDirty();
    }

    @Override
    public void commit(boolean onSave) {
	// Write data form locale-table into model
	privacySetting.getNames().clear();
	privacySetting.getDescriptions().clear();

	for (Information info : localizationTable.getStoredInformation()
		.getMap().values()) {
	    ILocalizedString name = new LocalizedString();
	    name.setLocale(new Locale(info.getLocale()));
	    name.setString(info.getName());
	    privacySetting.addName(name);
	}

	for (Information info : localizationTable.getStoredInformation()
		.getMap().values()) {
	    LocalizedString desc = new LocalizedString();
	    desc.setLocale(new Locale(info.getLocale()));
	    desc.setString(info.getDescription());
	    privacySetting.addDescription(desc);
	}

	block.refresh();
	localizationTable.setDirty(false);
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

    }

    @Override
    public void selectionChanged(IFormPart part, ISelection selection) {
	// Get parent element (PS-Object)
	TreePath[] path = ((TreeSelection) selection).getPaths();

	// This happens if the user deletes the default name/desc
	if (path.length < 1) {
	    return;
	}
	privacySetting = (RGISPrivacySetting) path[0].getFirstSegment();

	update();
    }

    private void update() {
	StoredInformation localization = localizationTable
		.getStoredInformation();
	localization.clear();

	// Fill table with data from ps-object
	for (ILocalizedString name : privacySetting.getNames()) {
	    localization.addName(name.getLocale().getLanguage(),
		    name.getString());
	}

	for (ILocalizedString desc : privacySetting.getDescriptions()) {
	    localization.addDescription(desc.getLocale().getLanguage(),
		    desc.getString());
	}

	localizationTable.refresh();
    }

}
