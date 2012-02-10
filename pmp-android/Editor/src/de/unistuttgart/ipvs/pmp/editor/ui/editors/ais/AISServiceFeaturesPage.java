package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

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

    /**
     * Constructor
     * 
     * @param editor
     */
    public AISServiceFeaturesPage(FormEditor editor) {
	super(editor, ID, "Service Features");
    }
    
    @Override
    protected void createFormContent(IManagedForm managedForm) {
	
    }

}
