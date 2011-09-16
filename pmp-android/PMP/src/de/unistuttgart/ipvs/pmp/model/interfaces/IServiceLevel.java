package de.unistuttgart.ipvs.pmp.model.interfaces;

/**
 * {@link IServiceLevel} is returned by an {@link IApp}, containing all required
 * {@link IPrivacyLevel}s for an accordingly service of the {@link IApp}.
 * 
 * <p>
 * It is possible that an {@link IServiceLevel} is not available (can be checked
 * using {@link IServiceLevel#isAvailable()}), that means the
 * {@link IServiceLevel} is using a {@link IPrivacyLevel} of an
 * {@link IResourceGroup} which are not registered at PMP or does not have this
 * {@link IPrivacyLevel} as a choice. They will be made available when
 * {@link IResourceGroup} is registered at PMP and does offer all the required
 * {@link IPrivacyLevel}s.
 * 
 * @author Jakob Jarosch
 */
public interface IServiceLevel {

    /**
     * @return returns the unique level of the {@link IServiceLevel}.
     */
    public int getLevel();

    /**
     * @return Returns the localized name of the {@link IServiceLevel}.
     */
    public String getName();

    /**
     * @return Returns the localized description of the {@link IServiceLevel}.
     */
    public String getDescription();

    /**
     * @return Returns the required {@link IPrivacyLevel}s for the
     *         {@link IServiceLevel}.
     */
    public IPrivacyLevel[] getPrivacyLevels();

    /**
     * @return Returns if a {@link IServiceLevel} is available for setting or
     *         not. (more details can be found in JavaDoc of
     *         {@link IServiceLevel}).
     */
    public boolean isAvailable();
}
