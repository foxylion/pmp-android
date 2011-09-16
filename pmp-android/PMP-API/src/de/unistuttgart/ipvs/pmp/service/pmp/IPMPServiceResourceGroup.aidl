package de.unistuttgart.ipvs.pmp.service.pmp;

/**
 * The Service of PMP provided for an authenticated ResourceGroup.
 *
 * @author Jakob Jarosch
 */
interface IPMPServiceResourceGroup {

	/**
	 * Save a changed PrivacyLevel in the PMPService.
	 *
	 * @param preset The identifier of the {@link IPreset}
	 * @param privacyLevel The identifier of the {@link IPrivacyLevel}.
	 * @param value The new value of the {@link IPrivacyLevel} which should be set.
	 */
	void savePrivacyLevel(String preset, String privacyLevel, String value);
}