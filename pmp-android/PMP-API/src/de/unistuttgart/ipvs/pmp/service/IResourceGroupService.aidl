package de.unistuttgart.ipvs.pmp.service;


interface IResourceGroupService {

	/**
	 * @returns the name of the ResourceGroup.
	 */
	String getName(String locale);
	
	/**
	 * @returns the description of the ResourceGroup.
	 */
	String getDescription(String locale);
	
	/**
	 * Returns the privacy levels for the ResourceGroup.
	 */
	List getPrivacyLevelIdentifiers();
	
	String getPrivacyLevelName(String locale, String identifier);
	
	String getPrivacyLevelDescription(String locale, String identifier);
	
	String getHumanReadablePrivacyLevelValue(String locale, String identifier, String value);

	/**
	 * Accepts a Map with key as String and value as String.
	 * The value has to be a List with concatenations (using ":" as delimiter)
	 * of privacy-level and value of the given privacy level inside.<br/>
	 * <br/>
	 * Exact declaration: Map&lt;String, List&lt;String&gt;&gt;
	 */
	void setAccesses(in Map accesses);
	
	/**
	 * Checks if an privacy level value satisfies the currently set value or not.
	 * 
	 * @param reference the original value which should be referenced for testing
	 * @param value the value which should be compared to reference
	 *
	 * @return true if value is equal or better than reference, otherwise false
	 */
	boolean satisfiesPrivacyLevel(String privacyLevel, String reference, String value);
}