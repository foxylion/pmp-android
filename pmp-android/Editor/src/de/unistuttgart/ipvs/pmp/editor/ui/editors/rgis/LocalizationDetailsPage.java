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
import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ISetDirtyAction;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.LocaleTable;
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
	//private InformationTable localizationTable;
	private RGISPrivacySetting privacySetting;
	private LocaleTable localeTable;
	private final LocaleTable.Type type;

	public LocalizationDetailsPage(PrivacySettingsBlock block, LocaleTable.Type type) {
		this.block = block;
		this.type = type;
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
		ISetDirtyAction dirtyAction = new ISetDirtyAction() {

			@Override
			public void doSetDirty(boolean dirty) {
				Model.getInstance().setRgisDirty(true);
				
			}
			
		};
		localeTable = new LocaleTable(section, type, dirtyAction, toolkit);
		//localizationTable = new InformationTable(section,
		//		new StoredInformation(), toolkit);
		section.setClient(localeTable.getComposite());

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		return localeTable.isDirty();
	}

	@Override
	public void commit(boolean onSave) {
		// Write data form locale-table into model
		/*privacySetting.getNames().clear();
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
		*/
		block.refresh();
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

		localeTable.setData(privacySetting);
		localeTable.refresh();
	}

	

}
