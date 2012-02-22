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
