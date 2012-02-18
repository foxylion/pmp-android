package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localization.LocaleTable;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localization.LocaleTable.Type;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * General-Page of the RGIS-Editor
 * 
 * @author Patrick Strobel
 * 
 */
public class GeneralPage extends FormPage {

	private IManagedForm managedForm;
	public static final String ID = "rgis_general";

	public GeneralPage(FormEditor parent) {
		super(parent, ID, "General");
	}
	
	

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		this.managedForm = managedForm;
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

		// Create elements stored inside this section
		Composite client = toolkit.createComposite(section, SWT.WRAP);

		client.setLayout(new GridLayout(2, false));

		GridData textLayout = new GridData();
		textLayout.horizontalAlignment = GridData.FILL;
		textLayout.grabExcessHorizontalSpace = true;

		toolkit.createLabel(client, "Identifier");
		Text identifier = toolkit.createText(client, Model.getInstance()
				.getRgis().getIdentifier());
		identifier.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("D: "+managedForm.isDirty());
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		identifier.setLayoutData(textLayout);

		/*
		 * toolkit.createLabel(client, "Revision"); Text revision =
		 * toolkit.createText(client, "123");
		 * revision.setLayoutData(textLayout);
		 */

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
		section.setRedraw(true);
		
		RGIS rgis = Model.getInstance().getRgis();
		LocaleTable nameTable = new LocaleTable(client,rgis,Type.NAME,toolkit);
		nameTable.getComposite().setLayoutData(layoutData);
		//section.setClient(nameTable.getComposite());

		LocaleTable descTable = new LocaleTable(client,rgis,Type.DESCRIPTION,toolkit);
		descTable.getComposite().setLayoutData(layoutData);
		
		
		section.setClient(client);
		
/*
		// Prepare table
		RGIS rgis = Model.getInstance().getRgis();
		StoredInformation loc = new StoredInformation();

		// Add names and descriptions to table
		for (Name name : rgis.getNames()) {
			loc.addName(name.getLocale().getLanguage(), name.getName());
		}
		for (Description desc : rgis.getDescriptions()) {
			loc.addDescription(desc.getLocale().getLanguage(),
					desc.getDescription());
		}
		InformationTable table = new InformationTable(section, loc, toolkit);

		section.setClient(table.getControl());
		*/
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
		Section section = toolkit.createSection(parent, Section.TWISTIE
				| Section.TITLE_BAR);
		section.setText(title);
		section.setExpanded(true);

		GridData layoutData = new GridData();
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.grabExcessHorizontalSpace = true;

		section.setLayoutData(layoutData);

		return section;
	}

}
