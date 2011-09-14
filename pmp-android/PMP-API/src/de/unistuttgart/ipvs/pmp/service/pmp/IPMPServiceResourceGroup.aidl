package de.unistuttgart.ipvs.pmp.service.pmp;

/**
 * The Service of PMP provided for an authentificated ResourceGroup.
 *
 * @author Jakob Jarosch
 */
interface IPMPServiceResourceGroup {

	/**
	 * Save a changed PrivacyLevel in the PMPService.
	 *
	 * @param preset Identifier of the Preset
	 * @param privacyLevel Identifier of the PrivacyLevel
	 * @param vale new value of the PrivacyLevel which should be set
	 */
	void savePrivacyLevel(String preset, String privacyLevel, String value);
}