package de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser.XMLParserException.Type;

/**
 * This is a service level, which is assigned to an app and contains all
 * required resource groups of the app.
 * 
 * @author Marcus Vetter
 * 
 */
public class ServiceLevel implements Serializable {

    private static final long serialVersionUID = -3279934293726339125L;

    /**
     * This map contains all names of the app. key = locale
     */
    private Map<Locale, String> names;

    /**
     * This map contains all description of the app. key = locale
     */
    private Map<Locale, String> descriptions;

    /**
     * This list contains all required resource groups of the service level.
     */
    private List<RequiredResourceGroup> requiredResourceGroups;

    /**
     * Constructor is used to instantiate the data structures.
     */
    protected ServiceLevel() {
	names = new HashMap<Locale, String>();
	descriptions = new HashMap<Locale, String>();
	requiredResourceGroups = new ArrayList<RequiredResourceGroup>();
    }

    /**
     * Get all names of the service level
     * 
     * @return map with names, key = locale
     */
    public Map<Locale, String> getNames() {
	return names;
    }

    /**
     * Add a name to the service level by using the associated locale
     * 
     * @param locale
     *            of the given name
     * @param name
     *            of the service level
     */
    protected void addName(Locale locale, String name) {
	if (names.containsKey(locale)) {
	    throw new XMLParserException(
		    Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,
		    "The name of a service level with the locale "
			    + locale.getLanguage() + " already exists.");
	}
	names.put(locale, name);
    }

    /**
     * Get all descriptions of the service level
     * 
     * @return map with descriptions, key = locale
     */
    public Map<Locale, String> getDescriptions() {
	return descriptions;
    }

    /**
     * Add a description to the service level by using the associated locale
     * 
     * @param locale
     *            of the given description
     * @param description
     *            of the service level
     */
    protected void addDescription(Locale locale, String description) {
	if (descriptions.containsKey(locale)) {
	    throw new XMLParserException(
		    Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
		    "The description of a service level with the locale "
			    + locale.getLanguage() + " already exists.");
	}
	descriptions.put(locale, description);
    }

    /**
     * Get all required resource groups of the service level
     * 
     * @return list with required resource groups
     */
    public List<RequiredResourceGroup> getRequiredResourceGroups() {
	return requiredResourceGroups;
    }

    /**
     * Add a required resource group to the service level
     * 
     * @param required
     *            resource group
     */
    protected void addRequiredResourceGroup(RequiredResourceGroup rrg) {
	requiredResourceGroups.add(rrg);
    }

}