package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.IDirtyAction;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localization.LocaleTable;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localization.LocaleTable.Type;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;

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
		final Text identifier = toolkit.createText(client, Model.getInstance()
				.getRgis().getIdentifier());

		identifier.addFocusListener(new FocusListener() {
			
			private String before;

			@Override
			public void focusLost(FocusEvent e) {
				if (!identifier.getText().equals(before)) {
					setDirty(true);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				before = identifier.getText();

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

		// Defines action that should be done when tables are dirty
		IDirtyAction dirtyAction = new IDirtyAction() {
			
			@Override
			public void setDirty(boolean dirty) {
				setDirty(true);
			}
		};
		IRGIS rgis = Model.getInstance().getRgis();
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
	
	private void setDirty(boolean dirty) {
		Model.getInstance().setRgisDirty(dirty);
	}

}
