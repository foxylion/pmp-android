package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;

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
	form.setText("Define the Service Features");

	form.getBody().setLayout(new GridLayout(2, false));
	new ServiceFeatureMasterBlock().createContent(managedForm);
    }
}
