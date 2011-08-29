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
     * @return Returns the localized name of the resource group.
     */
    public String getName();

    /**
     * @return Returns the localized description of the resource group.
     */
    public String getDescription();

    /**
     * @return Returns all usable {@link IPrivacyLevel}s of the resource group.
     */
    public IPrivacyLevel[] getPrivacyLevels();
}
