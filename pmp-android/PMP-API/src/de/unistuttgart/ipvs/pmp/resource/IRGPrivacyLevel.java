package de.unistuttgart.ipvs.pmp.resource;

import android.app.Activity;

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

    /**
     * Should internally call an {@link Activity} which enables the user to
     * change the oldValue to a new value.
     * 
     * @param oldValue
     *            old value which should be initially displayed
     * @return Returns the new value which has been set.
     */
    public String changeValue(String oldValue);
}
