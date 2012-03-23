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
package de.unistuttgart.ipvs.pmp.editor.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.unistuttgart.ipvs.pmp.editor.Activator;
import de.unistuttgart.ipvs.pmp.editor.util.I18N;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    
    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(I18N.preferences_jpmpps_description);
    }
    
    
    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    @Override
    public void createFieldEditors() {
        addField(new StringFieldEditor(PreferenceConstants.JPMPPS_HOSTNAME, I18N.preferences_jpmpps_url + ":", //$NON-NLS-1$
                getFieldEditorParent()));
        addField(new IntegerFieldEditor(PreferenceConstants.JPMPPS_PORT, I18N.preferences_jpmpps_port + ":", //$NON-NLS-1$
                getFieldEditorParent()));
        addField(new IntegerFieldEditor(PreferenceConstants.JPMPPS_TIMEOUT, I18N.preferences_jpmpps_timeout + ":", //$NON-NLS-1$
                getFieldEditorParent(), 3));
        addField(new TestButton(I18N.preferences_jpmpps_test + ":", getFieldEditorParent())); //$NON-NLS-1$
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
    }
    
}
