/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import de.unistuttgart.ipvs.pmp.editor.model.AisModel;
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
     * The model of this editor instance
     */
    private final AisModel model;
    
    
    /**
     * Constructor to get the model of this editor instance
     * 
     * @param editor
     *            {@link FormEditor}
     * @param model
     *            {@link AisModel} of this {@link AisEditor}
     */
    public AISServiceFeaturesPage(FormEditor editor, AisModel model) {
        super(editor, ID, "Service Features");
        this.model = model;
    }
    
    
    @Override
    protected void createFormContent(IManagedForm managedForm) {
        ScrolledForm form = managedForm.getForm();
        form.setText("Define the Service Features");
        
        form.getBody().setLayout(new GridLayout(2, false));
        new ServiceFeatureMasterBlock(this.model).createContent(managedForm);
    }
}
