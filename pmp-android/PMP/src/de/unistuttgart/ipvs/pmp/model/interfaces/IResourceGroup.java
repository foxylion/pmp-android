package de.unistuttgart.ipvs.pmp.model.interfaces;

/**
 * {@link IResourceGroup} represents a resource group known by PMP.
 * 
 * @author Jakob Jarosch
 */
public interface IResourceGroup {

    /**
     * @return Returns the unique identifier of the {@link IResourceGroup}.
     */
    public String getIdentifier();

    /**
     * @return Returns the localized name of the {@link IResourceGroup}.
     */
    public String getName();

    /**
     * @return Returns the localized description of the {@link IResourceGroup}.
     */
    public String getDescription();

    /**
     * @return Returns all usable {@link IPrivacyLevel}s of the
     *         {@link IResourceGroup}.
     */
    public IPrivacyLevel[] getPrivacyLevels();

    /**
     * This method return all {@link IApp}s which are currently using the
     * {@link IResourceGroup}.<br>
     * Currently means that the {@link IApp} has an {@link IServiceLevel} set as
     * active where one of the {@link IResourceGroup}s {@link IPrivacyLevel} is
     * referenced.
     * 
     * @return Returns all {@link IApp} which are <b>currently</b> using this
     *         {@link IResourceGroup}.
     */
    IApp[] getAllAppsUsingThisResourceGroup();
}
