package de.unistuttgart.ipvs.pmp.resource;

import java.util.HashMap;

public class SimplePrivacyLevel extends PrivacyLevel {

    private Class myClass;
    private HashMap<String, String> names, descriptions;

    /**
     * 
     * @param type Class name of the privacy level's value. Currently this class only supports Boolean and Float.
     * @param names Contains names of the privacy level in different languages together with their locale. Leave this null if you want to use the localization in the application's resource.
     * @param names Contains description of the privacy level in different languages together with their locale. Leave this null if you want to use the localization in the application's resource.
     */
    public SimplePrivacyLevel(Class type, HashMap<String, String> names,
	    HashMap<String, String> descriptions) throws Exception {
	if (!(type == Boolean.class || type == Float.class)) {
	    // TODO: Localization?
	    throw new Exception(
		    "This class was not implemented for your data type. Please implement your own class extending the PrivacyLevel class. Thank you!");
	} else {
	    myClass = type;
	    this.names = names;
	    this.descriptions = descriptions;
	}
    }

    @Override
    public String getName(String locale) {
	// TODO: do it for real!
	if (names == null) {
	    return "";
	} else {
	    return names.get(locale);
	}
    }

    @Override
    public String getDescription(String locale) {
	// TODO: do it for real!
	if (descriptions == null) {
	    return "";
	} else {
	    return descriptions.get(locale);
	}    }

    @Override
    public String getHumanReadableValue(String locale, String value) {
	return null;
    }

    @Override
    public boolean isQualified(String reference, String value) {
	if (myClass == Boolean.class) {
	    return Boolean.parseBoolean(value) == Boolean
		    .parseBoolean(reference);
	} else if (myClass == Float.class) {
	    // For MaxFileSize only.
	    // TODO: implementation for other cases
	    return Float.parseFloat(value) <= Float.parseFloat(reference);
	}
	return false;
    }

    @Override
    public String changeValue(String oldValue) {
	// TODO Auto-generated method stub
	return null;
    }
}
