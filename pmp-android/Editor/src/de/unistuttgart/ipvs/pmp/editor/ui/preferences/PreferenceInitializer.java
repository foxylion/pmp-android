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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.unistuttgart.ipvs.pmp.editor.Activator;
import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPSConstants;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	store.setDefault(PreferenceConstants.JPMPPS_HOSTNAME,
		JPMPPSConstants.HOSTNAME);
	store.setDefault(PreferenceConstants.JPMPPS_PORT, JPMPPSConstants.PORT);
	store.setDefault(PreferenceConstants.JPMPPS_TIMEOUT, 10);
    }

    public static String getJpmppsHostname() {
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	return store.getString(PreferenceConstants.JPMPPS_HOSTNAME);
    }

    public static int getJpmppsPort() {
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	return store.getInt(PreferenceConstants.JPMPPS_PORT);
    }

    public static int getJpmppsTimeout() {
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	return store.getInt(PreferenceConstants.JPMPPS_TIMEOUT);
    }

}
