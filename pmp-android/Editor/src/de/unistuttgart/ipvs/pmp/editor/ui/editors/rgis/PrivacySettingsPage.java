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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import de.unistuttgart.ipvs.pmp.editor.model.Model;

/**
 * Privacy Settings page of the RGIS-Editor
 * 
 * @author Patrick Strobel
 */
public class PrivacySettingsPage extends FormPage {

    public static final String ID = "rgis_ps";
    private final PrivacySettingsBlock privacySettingsBlock;

    public PrivacySettingsPage(FormEditor parent, Model model) {
	super(parent, ID, "Privacy Settings");
	privacySettingsBlock = new PrivacySettingsBlock(this, model);

    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
	ScrolledForm form = managedForm.getForm();
	form.setText("Defines the Privacy Setting");

	privacySettingsBlock.createContent(managedForm);
    }

}
