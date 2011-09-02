package de.unistuttgart.ipvs.pmp.service.app;

import de.unistuttgart.ipvs.pmp.service.RegistrationState;

/**
 * The IAppService is provided by an application, over the interface
 * it's possible to get name, description and ServiceLevels from the App.
 *
 * @author Jakob Jarosch
 */
interface IAppService {
	
	String getName(String locale);
	
	String getDescription(String locale);
	
	int getServiceLevelCount();
	
	String getServiceLevelName(String locale, int serviceLevelOrdering);
	
	String getServiceLevelDescription(String locale, int serviceLevelOrdering);
	
	/**
	 * @return Returns a List of {@link AppPrivacyLevel}s
	 */
	List getServiceLevelPrivacyLevels(int serviceLevelOrdering);
	
	void setActiveServiceLevel(int serviceLevelOrdering);
	
	/**
	 * Is called when the PMP will inform the app about a successful (or not) registration.
	 *
	 * @param state State of the registration
	 */
	void setRegistrationSuccessful(in RegistrationState state);
}