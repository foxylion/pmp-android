package de.unistuttgart.ipvs.pmp.resource;

/**
 * An internal PrivacyLevel interface for a standard of accessing privacy
 * levels.
 */
public interface IRGPrivacyLevel {

	/**
	 * @return the identifier of the {@link IRGPrivacyLevel}.
	 */
	public String getIdentifier();

	/**
	 * @see IResourceGroupService.Stub#getPrivacyLevelName(String, String)
	 */
	public String getName(String locale);

	/**
	 * @see IResourceGroupService.Stub#getPrivacyLevelDescription(String,
	 *      String)
	 */
	public String getDescription(String locale);

	/**
	 * @see IResourceGroupService.Stub#getHumanReadablePrivacyLevelValue(String,
	 *      String, String)
	 */
	public String getHumanReadablePrivacyLevelValue(String locale, String value);

	/**
	 * @see IResourceGroupService.Stub#satisfiesPrivacyLevel(String, String,
	 *      String)
	 */
	public String satisfies(String oldValue, String newValue);
}
