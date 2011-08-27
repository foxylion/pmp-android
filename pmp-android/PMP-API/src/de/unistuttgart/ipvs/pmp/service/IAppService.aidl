package de.unistuttgart.ipvs.pmp.service;

interface IAppService {
	
	String getName(String locale);
	
	String getDescription(String locale);
	
	int getServiceLevelCount();
	
	String getServiceLevelName(String locale, int serviceLevelId);
	
	String getServiceLevelDescription(String locale, int serviceLevelId);
	
	/**
	 * @return a List of Strings, which concatenates
	 * 	       ResourceGroup, PrivacyLevel and Value,
	 *         using a ":" as delemiter.
	 */
	List getServiceLevelPrivacyLevels(int serviceLevelId);
	
	void setServiceLevel(int serviceLevel);
}