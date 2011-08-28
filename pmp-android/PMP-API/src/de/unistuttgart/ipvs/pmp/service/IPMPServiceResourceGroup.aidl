package de.unistuttgart.ipvs.pmp.service;

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