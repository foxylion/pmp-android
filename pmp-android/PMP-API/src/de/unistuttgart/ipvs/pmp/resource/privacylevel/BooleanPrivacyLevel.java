package de.unistuttgart.ipvs.pmp.resource.privacylevel;

import java.util.HashMap;
import java.util.Map;

public class BooleanPrivacyLevel extends PrivacyLevel {
    
    private static final String DEFAULT_LOCALE = "en";
    
    private Map<String, String> names;
    private Map<String, String> descriptions;
    
    public BooleanPrivacyLevel(Map<String, String> names, Map<String, String> descriptions) {
	if (!names.containsKey(DEFAULT_LOCALE)) {
	    throw new IllegalArgumentException();
	}
	this.names = names;
	if (!descriptions.containsKey(DEFAULT_LOCALE)) {
	    throw new IllegalArgumentException();
	}
	this.descriptions = descriptions;
    }
    
    public BooleanPrivacyLevel(String defaultName, String defaultDescription) {
	this.names = new HashMap<String, String>();
	this.names.put(DEFAULT_LOCALE, defaultName);
	this.descriptions = new HashMap<String, String>();
	this.descriptions.put(DEFAULT_LOCALE, defaultDescription);
    }

    @Override
    public String getName(String locale) {
	if (names.get(locale) == null) {
	    return names.get(DEFAULT_LOCALE);
	} else {
	    return names.get(locale);
	}
    }

    @Override
    public String getDescription(String locale) {
	if (descriptions.get(locale) == null) {
	    return descriptions.get(DEFAULT_LOCALE);
	} else {
	    return descriptions.get(locale);
	}
    }

    @Override
    public String getHumanReadableValue(String locale, String value) {
	if (locale.equals("de")) {
	    return valueOf(value) ? "wahr" : "falsch";
	} else {
	    return valueOf(value) ? "true" : "false";
	}
    }

    @Override
    public boolean isQualified(String reference, String value) {
	return (valueOf(value) || (!valueOf(reference))); 
    }
    
    /**
     * Parse and return boolean from PrivacyLevel value.
     *
     * @deprecated Please use the static method {@link #valueOf(String)} instead
     *    
     */
    @Deprecated
    public boolean parseValue(String value) {
	return Boolean.valueOf(value);
    }

    /**
     * Parse and return boolean value from a Privacy Level string.
     * 
     * @param value
     * @return Boolean.TRUE if value is equal to "true" using case insensitive
     *         comparison, Boolean.FALSE otherwise.
     */
    public static boolean valueOf(String value) {
	return Boolean.valueOf(value);
    }
}
