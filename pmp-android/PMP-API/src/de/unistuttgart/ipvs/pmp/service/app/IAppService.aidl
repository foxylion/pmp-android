package de.unistuttgart.ipvs.pmp.service.app;

import de.unistuttgart.ipvs.pmp.service.app.RegistrationResult;

/**
 * The IAppService is provided by an application, over the interface
 * it's possible to get name, description and ServiceLevels from the App.
 *
 * @author Jakob Jarosch
 */
interface IAppService {
	
	 /**
     * Informs the app about its currently granted i.e. active service features. 
     * 
     * @param features
     *            the Bundle that contains the mappings of strings (the identifiers of the service features in the app
     *            description XML) to booleans (true for granted i.e. active, false for not granted)
     */
    void updateServiceFeatures(in Bundle features);
	
	/**
	 * Informs the about the success (or failure) of the registration with PMP
	 *
	 * @param result result of the registration
	 */
	void replyRegistrationResult(in RegistrationResult result);
	

}