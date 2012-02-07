package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;



import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;


public class GeneralPage extends FormPage {
	
	public static final String ID = "rgis_general";

	public GeneralPage(FormEditor parent) {
		super(parent, ID, "General");
		// TODO Auto-generated constructor stub
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
		Text identifier = toolkit.createText(client, "123");
		identifier.setLayoutData(textLayout);
		
		toolkit.createLabel(client, "Revision");
		Text revision = toolkit.createText(client, "123");
		revision.setLayoutData(textLayout);
		
		section.setClient(client);
	}
	
	private void addLocalizationSection(Composite parent, FormToolkit toolkit) {
		// Set the section's parameters
		Section section = createSection(parent, "Localization", toolkit);
		
		// Create elements stored inside this section
		Composite client = toolkit.createComposite(section, SWT.WRAP);

		client.setLayout(new GridLayout(2, false));
		
		GridData textLayout = new GridData();
		textLayout.horizontalAlignment = GridData.FILL;
		textLayout.grabExcessHorizontalSpace = true;
		
		toolkit.createLabel(client, "Put in table here");
		
		section.setClient(client);
	}
	
	/**
	 * Creates a default section which spans over the whole editor
	 * @param parent
	 * @param title
	 * @param toolkit
	 * @return
	 */
	private Section createSection(Composite parent, String title, FormToolkit toolkit) {
		Section section = toolkit.createSection(parent, Section.TWISTIE | Section.SHORT_TITLE_BAR);
		section.setText(title);
		section.setExpanded(true);
		
		GridData layoutData = new GridData();
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.grabExcessHorizontalSpace = true;
		
		section.setLayoutData(layoutData);
		
		return section;
	}
	
	







}
