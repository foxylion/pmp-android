package de.unistuttgart.ipvs.pmp.service;

/**
 * The Service of PMP provided for an authentificated ResourceGroup.
 *
 * @author Jakob Jarosch
 */
interface IPMPServiceResourceGroup {

	/**
	 * Save a changed PrivacyLevel in the PMPService.
	 *
	 * @param app Identifier of the App
	 * @param privacyLevel Identifier of the PrivacyLevel
	 * @param vale new value of the PrivacyLevel which should be set
	 */
	void savePrivacyLevel(String app, String privacyLevel, String value);
}