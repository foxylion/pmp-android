package de.unistuttgart.ipvs.pmp.util.xml.rg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.util.xml.AbstractInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;

public class RgInformationSet extends AbstractInformationSet implements
		Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -5127998017294446211L;

	/**
	 * This map contains all privacy settings of the resourcegroup. key =
	 * identifier
	 */
	private Map<String, PrivacySetting> privacySettingsMap;

	/**
	 * Constructor is used to instantiate the data structures.
	 */
	protected RgInformationSet() {
		super();
		this.privacySettingsMap = new HashMap<String, PrivacySetting>();
	}

	/**
	 * Add a privacy setting to the resourcegroup
	 * 
	 * @param identifier
	 *            of the privacy setting
	 * @param ps
	 *            privacy setting
	 */
	protected void addPrivacySetting(String identifier, PrivacySetting ps) {
		if (this.privacySettingsMap.containsKey(identifier)) {
			throw new XMLParserException(
					Type.PRIVACY_SETTING_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
					"A Privacy Setting with the identifier " + identifier
							+ " already exists.");
		}
		this.privacySettingsMap.put(identifier, ps);
	}

	/**
	 * Get the map which contains all privacy settings
	 * 
	 * @return map with privacy settings
	 */
	public Map<String, PrivacySetting> getPrivacySettingsMap() {
		return privacySettingsMap;
	}
}
