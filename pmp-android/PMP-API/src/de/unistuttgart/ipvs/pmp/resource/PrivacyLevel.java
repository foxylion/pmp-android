package de.unistuttgart.ipvs.pmp.resource;

import java.util.Locale;

import android.app.Activity;

/**
 * An internal PrivacyLevel interface for a standard of accessing privacy
 * levels.
 */
public abstract class PrivacyLevel {

    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from
     *            {@link Locale#getLanguage()}
     * @return the name of this privacy level for the given locale
     */
    public abstract String getName(String locale);

    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from
     *            {@link Locale#getLanguage()}
     * @return the description of this privacy level for the given locale
     */
    public abstract String getDescription(String locale);

    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from
     *            {@link Locale#getLanguage()}
     * @return the human readable representation of the value for this privacy
     *         level for the given locale
     */
    public abstract String getHumanReadablePrivacyLevelValue(String locale,
	    String value);

    /**
     * Should return true, iff value >= reference for this privacy level. E.g.
     * value.compareTo(reference) >= 0
     */
    public abstract boolean satisfies(String reference, String value);

    /**
     * Should internally call an {@link Activity} which enables the user to
     * change the oldValue to a new value.
     * 
     * (tk: I suppose the upper comment is wrong and the Activity should be
     * called in ResourceGroup)
     * 
     * @param oldValue
     *            old value which should be initially displayed
     * @return Returns the new value which has been set.
     */
    public abstract String changeValue(String oldValue);
}
