package de.unistuttgart.ipvs.pmp.util.xml;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;

/**
 * This class abstracts common used fields and methods for the information sets of apps and resourcegroups
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractInformationSet {

	/**
	 * This variable contains the identifier
	 */
	protected String identifier;

	/**
	 * This map contains all names. key = locale
	 */
	protected Map<Locale, String> names;

	/**
	 * This map contains all descriptions. key = locale
	 */
	protected Map<Locale, String> descriptions;

	/**
	 * Constructor is used to instantiate the data structures.
	 */
	protected AbstractInformationSet() {
		this.names = new HashMap<Locale, String>();
		this.descriptions = new HashMap<Locale, String>();
	}

	/**
	 * Get the identifier.
	 * 
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Set the identifier of the app
	 * 
	 * @param identifier
	 *            identifier of the app
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Get all names.
	 * 
	 * @return map with names, key = locale
	 */
	public Map<Locale, String> getNames() {
		return this.names;
	}

	/**
	 * Add a name.
	 * 
	 * @param locale
	 *            of the name
	 * @param name
	 *            name
	 */
	public void addName(Locale locale, String name) {
		if (this.names.containsKey(locale)) {
			throw new XMLParserException(
					Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,
					"The name with the locale " + locale.getLanguage()
							+ " already exists.");
		}
		this.names.put(locale, name);
	}

	/**
	 * Get all descriptions.
	 * 
	 * @return map with descriptions, key = locale
	 */
	public Map<Locale, String> getDescriptions() {
		return this.descriptions;
	}

	/**
	 * Add a description
	 * 
	 * @param locale
	 *            of the description
	 * @param description
	 *            description
	 */
	public void addDescription(Locale locale, String description) {
		if (this.descriptions.containsKey(locale)) {
			throw new XMLParserException(
					Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
					"The description with the locale " + locale.getLanguage()
							+ " already exists.");
		}
		this.descriptions.put(locale, description);
	}

}
