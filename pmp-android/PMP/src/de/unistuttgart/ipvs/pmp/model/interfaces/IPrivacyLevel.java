package de.unistuttgart.ipvs.pmp.model.interfaces;

/**
 * The {@link IPrivacyLevel} interface represents a PrivacyLevel of a
 * {@link IResourceGroup}. It can be fetched from an {@link IServiceLevel}, {@link IRole} or
 * {@link IResourceGroup}.
 * 
 * @author Jakob Jarosch
 */
public interface IPrivacyLevel {

	/**
	 * @return Returns the resource group wide unique identifier of the privacy
	 *         level.
	 */
	public String getIdentifier();

	/**
	 * @return Returns the father of the PrivacyLevel (a resource group).
	 */
	public IResourceGroup getResourceGroup();

	/**
	 * @return Returns the localized name of the privacy level.
	 */
	public String getName();

	/**
	 * @return Returns the localized description of the privacy level.
	 */
	public String getDescription();

	/**
	 * Returns the set value of the privacy level.
	 * 
	 * @return Returns the set value, NULL if it is fetched from an
	 *         {@link IResourceGroup} instance.
	 */
	public String getValue();

	/**
	 * Returns an human readable representation of the a given value.<br/>
	 * <b>Note: This method uses service communication and is slow, use with
	 * caution.</b>
	 * 
	 * @param value
	 *            Value which should be returned as a human readable
	 *            representation.
	 * @return the human readable representation of the given value.
	 */
	public String getHumanReadableValue(String value);
}
