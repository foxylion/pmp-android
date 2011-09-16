package de.unistuttgart.ipvs.pmp.model.interfaces;

/**
 * This represents a App registred at PMP.
 * 
 * @author Jakob Jarosch
 */
public interface IApp {

    /**
     * @return Returns the unique identifier of the App.
     */
    public String getIdentifier();

    /**
     * @return Returns the localized name of the App.
     */
    public String getName();

    /**
     * @return Returns the localized description of the App.
     */
    public String getDescription();

    /**
     * @return Returns the service levels provided by the App.
     */
    public IServiceLevel[] getServiceLevels();

    /**
     * Returns a service level with exactly this service level.
     * 
     * @param level
     *            ID of the service level
     * @return the to the ID corresponding service level
     */
    public IServiceLevel getServiceLevel(int level);

    /**
     * @return get the current active service level set for the App.
     */
    public IServiceLevel getActiveServiceLevel();

    /**
     * Set a new service level for the App.<br>
     * 
     * <b>This method removes the App from all Presets, so use that with
     * caution!</b>
     * 
     * @param serviceLevel
     *            New service level which should be set.
     */
    public void setActiveServiceLevelAsPreset(int serviceLevel);

    /**
     * Verifies the service level and publishes the new one asynchronously if it
     * changed.
     */
    public void verifyServiceLevel();

    /**
     * @return Returns all to the App assigned Presets.
     */
    public IPreset[] getAssignedPresets();

    /**
     * @return Returns all {@link IResourceGroup}s which are used in one or more
     *         of the {@link IServiceLevel}s defined by this {@link IApp}.
     */
    public IResourceGroup[] getAllResourceGroupsUsedByServiceLevels();

    /**
     * Returns all {@link IPrivacyLevel}s of this App, which are currently in
     * use, and does match the {@link IResourceGroup}.
     * 
     * @param resourceGroup
     *            the {@link IResourceGroup} which should be matched
     * @return Returns all {@link IPrivacyLevel}s of this App, which are
     *         currently in use, and does match the {@link IResourceGroup}.
     */
    public IPrivacyLevel[] getAllPrivacyLevelsUsedByActiveServiceLevel(
	    IResourceGroup resourceGroup);
}
