package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * Privacy Settings page of the RGIS-Editor
 * 
 * @author Patrick Strobel
 */
public class PrivacySettingsPage extends FormPage {

    public static final String ID = "rgis_ps";
    private boolean dirty = false;
    private PrivacySettingsBlock privacySettingsBlock = new PrivacySettingsBlock(
	    this);

    public PrivacySettingsPage(FormEditor parent) {
	super(parent, ID, "Privacy Settings");

    }

    public void setDirty(boolean dirty) {
	this.dirty = dirty;
	System.out.println("PS-Page dirty = " + dirty);
    }

    @Override
    public boolean isDirty() {
	return dirty;
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
	ScrolledForm form = managedForm.getForm();
	form.setText("Defines the Privacy Setting");

	privacySettingsBlock.createContent(managedForm);
    }

}
