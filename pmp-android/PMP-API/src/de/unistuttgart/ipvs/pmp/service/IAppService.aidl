package de.unistuttgart.ipvs.pmp.service;

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
	
	void setRegistrationSuccessful(boolean success);
}