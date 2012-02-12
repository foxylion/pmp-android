package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.ServiceFeatureMasterBlock;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais.ServiceFeatureTable;

/**
 * Creates the preferences page for the {@link AisEditor}
 * 
 * @author Thorsten Berberich
 * 
 */
public class AISServiceFeaturesPage extends FormPage {

    /**
     * ID of this page
     */
    public static final String ID = "ais_service_features";

    public int width;
    public int height;

    /**
     * Constructor
     * 
     * @param editor
     */
    public AISServiceFeaturesPage(FormEditor editor) {
	super(editor, ID, "Service Features");
	width = editor.getSite().getShell().getSize().x;
	height = editor.getSite().getShell().getSize().y;
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
	ScrolledForm form = managedForm.getForm();
	FormToolkit toolkit = managedForm.getToolkit();
	form.setText("Define the Service Features");

	form.getBody().setLayout(new GridLayout(2, false));
	new ServiceFeatureMasterBlock().createContent(managedForm);
//	addServiceFeatureSection(form.getBody(), toolkit);
//	addAttributesSection(form.getBody(), toolkit);
    }

    private void addServiceFeatureSection(Composite parent, FormToolkit toolkit) {
	// Set the section's parameters
	Section section = createSectionWithDescription(parent,
		"Service Features", toolkit,
		"Defines the service features the ais.xml");

	// Create elements stored inside this section
	Composite client = toolkit.createComposite(section, SWT.NULL);
	client.setLayout(new FillLayout());

	new ServiceFeatureTable(client, toolkit, height);
	section.setClient(client);
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
	layoutData.widthHint = width / 3;

	section.setLayoutData(layoutData);

	return section;
    }
}
