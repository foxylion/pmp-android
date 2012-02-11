package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class PrivacySettingsPage extends FormPage {

	public static final String ID = "rgis_ps";
	
	private PrivacySettingsBlock privacySettingsBlock = new PrivacySettingsBlock(this);
	
	public PrivacySettingsPage(FormEditor parent) {
		super(parent, ID, "Privacy Settings");

		;
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("PS");
		
		privacySettingsBlock.createContent(managedForm);
		
		
	}

}
