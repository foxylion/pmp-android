package de.unistuttgart.ipvs.pmp.model.interfaces;

/**
 * {@link IServiceLevel} is served by a {@link IApp}, containing all required
 * {@link IPrivacyLevel}s for an accordingly service of the .
 * 
 * @author Jakob Jarosch
 */
public interface IServiceLevel {

    /**
     * @return returns the level position of the service level.
     */
    public int getLevel();

    /**
     * @return Returns the localized name of the service level.
     */
    public String getName();

    /**
     * @return Returns the localized description of the service level.
     */
    public String getDescription();

    /**
     * @return Returns the required privacy levels for the service level.
     */
    public IPrivacyLevel[] getPrivacyLevels();
}
