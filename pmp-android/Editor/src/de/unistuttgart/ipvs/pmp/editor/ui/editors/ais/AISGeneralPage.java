/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import org.eclipse.swt.SWT;
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

import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.InformationTable;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.StoredInformation;

/**
 * Creates the "General" page for the {@link AisEditor}
 * 
 * @author Thorsten Berberich
 * 
 */
public class AISGeneralPage extends FormPage {

    /**
     * ID of this page
     */
    public static final String ID = "ais_general";

    private StoredInformation localization = new StoredInformation();

    /**
     * Constructor
     * 
     * @param editor
     */
    public AISGeneralPage(FormEditor editor) {
	super(editor, ID, "General");
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
	ScrolledForm form = managedForm.getForm();
	FormToolkit toolkit = managedForm.getToolkit();
	form.setText("Define general information");

	form.getBody().setLayout(new GridLayout(1, false));
	addPropertiesSection(form.getBody(), toolkit);
	addLocalizationSection(form.getBody(), toolkit);
    }

    /**
     * Adds the properties section to the page
     * 
     * @param parent
     *            parent {@link Composite}
     * @param toolkit
     *            {@link FormToolkit}
     */
    private void addPropertiesSection(Composite parent, FormToolkit toolkit) {
	// Set the section's parameters
	Section section = createSectionWithDescription(parent, "Preferences",
		toolkit, "Defines general informations about the ais.xml");

	// Create elements stored inside this section
	Composite client = toolkit.createComposite(section, SWT.WRAP);

	client.setLayout(new GridLayout(2, false));

	GridData textLayout = new GridData();
	textLayout.horizontalAlignment = GridData.FILL;
	textLayout.grabExcessHorizontalSpace = true;

	toolkit.createLabel(client, "Identifier");
	Text identifier = toolkit.createText(client, null);
	identifier.setLayoutData(textLayout);

	toolkit.createLabel(client, "Revision");
	Text revision = toolkit.createText(client, null);
	revision.setLayoutData(textLayout);

	section.setClient(client);
    }

    /**
     * Adds the localization section to the page with the table
     * 
     * @param parent
     *            parent {@link Composite}
     * @param toolkit
     *            {@link FormToolkit}
     */
    private void addLocalizationSection(Composite parent, FormToolkit toolkit) {
	// Set the section's parameters
	Section section = createSectionWithDescription(parent, "Localization",
		toolkit, "Defines the localized informations about the ais.xml");

	// Create elements stored inside this section
	Composite client = toolkit.createComposite(section, SWT.None);

	client.setLayout(new GridLayout(1, true));

	GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;

	// Prepare table
	InformationTable table = new InformationTable(section, localization,
		toolkit);

	section.setClient(table.getControl());
    }

    /**
     * Creates a default section which spans over the whole editor
     * 
     * @param parent
     *            parent {@link Composite}
     * @param title
     *            the title
     * @param toolkit
     *            {@link FormToolkit}
     * @param desc
     *            description of the Section
     * @return
     */
    private Section createSectionWithDescription(Composite parent,
	    String title, FormToolkit toolkit, String desc) {
	Section section = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR | Section.DESCRIPTION);
	section.setDescription(desc);
	section.setText(title);
	section.setExpanded(true);

	GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;

	section.setLayoutData(layoutData);

	return section;
    }

    /**
     * Returns all stored information of the table
     * 
     * @return {@link StoredInformation}
     */
    public StoredInformation getLocalization() {
	return localization;
    }

}
