package de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser.XMLParserException.Type;



/**
 * This is an information set of the app.
 * It contains all basic informations (names and descriptions in different locals) and all provided service levels.
 * 
 * @author Marcus Vetter
 */
public class AppInformationSet {
	
	/**
	 * This map contains all names of the app.
	 * key = locale
	 */
	private Map<Locale, String> names;
	
	/**
	 * This map contains all description of the app.
	 * key = locale
	 */
	private Map<Locale, String> descriptions;
	
	/**
	 * This map contains all service levels of the app.
	 * key = level
	 */
	private Map<Integer, ServiceLevel> serviceLevels;
	
	/**
	 * Constructor is used to instantiate the data structures.
	 */
	protected AppInformationSet() {
		names = new HashMap<Locale, String>();
		descriptions = new HashMap<Locale, String>();
		serviceLevels = new HashMap<Integer, ServiceLevel>();
	}

	/**
	 * Get all names of the app
	 * @return map with names, key = locale
	 */
	public Map<Locale, String> getNames() {
		return names;
	}
	
	/**
	 * Get all descriptions of the app	
	 * @return map with descriptions, key = locale
	 */	
	public Map<Locale, String> getDescriptions() {
		return descriptions;
	}
	
	/**
	 * Get all service levels of the app
	 * @return map with service levels, key = level
	 */
	public Map<Integer, ServiceLevel> getServiceLevels() {
		return serviceLevels;
	}
	
	/**
	 * Add a name to the app
	 * @param locale of the name
	 * @param name
	 */
	protected void addName(Locale locale, String name) {
		if (names.containsKey(locale)) {
			throw new XMLParserException(
					Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,
					"The name of the app with the locale "
							+ locale.getLanguage() + " already exists.");
		}
		names.put(locale, name);
	}
	
	/**
	 * Add a description to the app
	 * @param locale of the description
	 * @param description
	 */
	protected void addDescription(Locale locale, String description) {
		if (descriptions.containsKey(locale)) {
			throw new XMLParserException(
					Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
					"The description of the app with the locale "
							+ locale.getLanguage() + " already exists.");
		}
		descriptions.put(locale,  description);
	}
	
	/**
	 * Add a service level to the app
	 * @param level of the service level
	 * @param sl service level
	 */
	protected void addServiceLevel(int level, ServiceLevel sl) {
		if (serviceLevels.containsKey(level)) {
			throw new XMLParserException(
					Type.SERVICE_LEVEL_WITH_SAME_LEVEL_ALREADY_EXISTS,
					"The level " + level + " of a service level already exists.");
		}
		serviceLevels.put(level, sl);
	}

}
