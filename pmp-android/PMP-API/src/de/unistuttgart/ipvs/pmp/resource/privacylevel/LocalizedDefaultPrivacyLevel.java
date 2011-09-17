package de.unistuttgart.ipvs.pmp.resource.privacylevel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.Constants;

/**
 * {@link LocalizedDefaultPrivacyLevel} that supplies some basic localization.
 * 
 * @author Tobias Kuhn
 * 
 * @param <T>
 *            the type that is stored in this
 *            {@link LocalizedDefaultPrivacyLevel}.
 */
public abstract class LocalizedDefaultPrivacyLevel<T extends Comparable<T>>
	extends DefaultPrivacyLevel<T> {
    /**
     * The names
     */
    private Map<String, String> names;

    /**
     * The descriptions
     */
    private Map<String, String> descriptions;

    /**
     * Creates a new {@link LocalizedDefaultPrivacyLevel} using the
     * {@link Comparable} implementation of T.
     * 
     * @param names
     *            map of names mapping ISO 639 locale strings to the names for
     *            that locale
     * @param descriptions
     *            map of descriptions mapping ISO 639 locale strings to the
     *            descriptions for that locale
     * @throws IllegalArgumentException
     *             if either names or descriptions do not map
     *             {@link Constants#DEFAULT_LOCALE} to a string.
     */
    public LocalizedDefaultPrivacyLevel(Map<String, String> names,
	    Map<String, String> descriptions) {
	super();

	if (!names.containsKey(Constants.DEFAULT_LOCALE)) {
	    throw new IllegalArgumentException();
	}
	this.names = names;

	if (!descriptions.containsKey(Constants.DEFAULT_LOCALE)) {
	    throw new IllegalArgumentException();
	}
	this.descriptions = descriptions;
    }

    /**
     * Creates a new {@link LocalizedDefaultPrivacyLevel} using the
     * {@link Comparable} implementation of T.
     * 
     * @param defaultName
     *            name for {@link Constants#DEFAULT_LOCALE}.
     * @param defaultDescription
     *            description for {@link Constants#DEFAULT_LOCALE}.
     * @param comparator
     *            Comparator to represent the "permit more or equal" partial
     *            order.
     */
    public LocalizedDefaultPrivacyLevel(String defaultName,
	    String defaultDescription) {
	super();

	this.names = new HashMap<String, String>();
	this.names.put(Constants.DEFAULT_LOCALE, defaultName);
	this.descriptions = new HashMap<String, String>();
	this.descriptions.put(Constants.DEFAULT_LOCALE, defaultDescription);
    }

    /**
     * Creates a new {@link LocalizedDefaultPrivacyLevel} using the
     * {@link Comparator} implementation for T.
     * 
     * @param names
     *            map of names mapping ISO 639 locale strings to the names for
     *            that locale
     * @param descriptions
     *            map of descriptions mapping ISO 639 locale strings to the
     *            descriptions for that locale
     * @param comparator
     *            Comparator to represent the "permit more or equal" partial
     *            order.
     * @throws IllegalArgumentException
     *             if either names or descriptions do not map
     *             {@link Constants#DEFAULT_LOCALE} to a string.
     */
    public LocalizedDefaultPrivacyLevel(Map<String, String> names,
	    Map<String, String> descriptions, Comparator<T> comparator) {
	super(comparator);

	setNames(names);
	setDescriptions(descriptions);
    }

    /**
     * Creates a new {@link LocalizedDefaultPrivacyLevel} using the
     * {@link Comparator} implementation for T.
     * 
     * @param defaultName
     *            name for {@link Constants#DEFAULT_LOCALE}.
     * @param defaultDescription
     *            description for {@link Constants#DEFAULT_LOCALE}.
     */
    public LocalizedDefaultPrivacyLevel(String defaultName,
	    String defaultDescription, Comparator<T> comparator) {
	super(comparator);

	this.names = new HashMap<String, String>();
	this.names.put(Constants.DEFAULT_LOCALE, defaultName);
	this.descriptions = new HashMap<String, String>();
	this.descriptions.put(Constants.DEFAULT_LOCALE, defaultDescription);
    }

    /**
     * Sets the localized names.
     * 
     * @param names
     *            map of names mapping ISO 639 locale strings to the names for
     *            that locale
     * @throws IllegalArgumentException
     *             if either names does not map {@link Constants#DEFAULT_LOCALE}
     *             to a string.
     */
    public void setNames(Map<String, String> names) {
	if (!names.containsKey(Constants.DEFAULT_LOCALE)) {
	    throw new IllegalArgumentException();
	}
	this.names = names;
    }

    /**
     * Sets the localized descriptions.
     * 
     * @param descriptions
     *            map of descriptions mapping ISO 639 locale strings to the
     *            descriptions for that locale
     * @throws IllegalArgumentException
     *             if descriptions does not map {@link Constants#DEFAULT_LOCALE}
     *             to a string.
     */
    public void setDescriptions(Map<String, String> descriptions) {
	if (!descriptions.containsKey(Constants.DEFAULT_LOCALE)) {
	    throw new IllegalArgumentException();
	}
	this.descriptions = descriptions;
    }

    @Override
    public String getName(String locale) {
	if (names.get(locale) == null) {
	    return names.get(Constants.DEFAULT_LOCALE);
	} else {
	    return names.get(locale);
	}
    }

    @Override
    public String getDescription(String locale) {
	if (descriptions.get(locale) == null) {
	    return descriptions.get(Constants.DEFAULT_LOCALE);
	} else {
	    return descriptions.get(locale);
	}
    }
}
