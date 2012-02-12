package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

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
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.InformationTable;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.StoredInformation;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Defines the details page of the privacy setting that allows
 * the user to set the privacy setting's localization
 * 
 * @author Patrick Strobel
 */
public class LocalizationDetailsPage implements IDetailsPage {

	private IManagedForm form;
	private InformationTable localizationTable;
	
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
		Section section = toolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
		section.setText("Localization");
		section.setExpanded(true);
		section.setLayoutData(parentLayout);
		
		// Build localization table
		localizationTable = new InformationTable(section, new StoredInformation(), toolkit);
		section.setClient(localizationTable.getControl());
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void commit(boolean onSave) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setFormInput(Object input) {
		System.out.println("Set Input");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		// Get parent element (PS-Object)
		TreePath[] path = ((TreeSelection)selection).getPaths();
		RGISPrivacySetting ps = (RGISPrivacySetting)path[0].getFirstSegment();
		StoredInformation localization = localizationTable.getStoredInformation();

		// Fill table with data from ps-object
		for (Name name : ps.getNames()) {
			localization.addName(name.getLocale().getLanguage(), name.getName());
		}
		
		for (Description desc : ps.getDescriptions()) {
			localization.addDescription(desc.getLocale().getLanguage(), desc.getDescription());
		}
		
		localizationTable.refresh();
		
	}


	
}
