package de.unistuttgart.ipvs.pmp.resource;

import de.unistuttgart.ipvs.pmp.service.IResourceGroupService;

public interface IPrivacyLevel {

	/**
	 * @return the identifier of the {@link IPrivacyLevel}.
	 */
	public String getIdentifier();
	
	/**
	 * @see IResourceGroupService.Stub#getPrivacyLevelName(String, String)
	 */
	public String getName(String locale);
	
	/**
	 * @see IResourceGroupService.Stub#getPrivacyLevelDescription(String, String)
	 */
	public String getDescription(String locale);
	
	/**
	 * @see IResourceGroupService.Stub#getHumanReadablePrivacyLevelValue(String, String, String)
	 */
	public String getHumanReadablePrivacyLevelValue(String locale, String value);
	
	/**
	 * @see IResourceGroupService.Stub#satisfiesPrivacyLevel(String, String, String)
	 */
	public String satisfies(String oldValue, String newValue);
}
