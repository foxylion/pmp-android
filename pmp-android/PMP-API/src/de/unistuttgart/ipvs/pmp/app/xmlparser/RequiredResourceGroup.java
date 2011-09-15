package de.unistuttgart.ipvs.pmp.app.xmlparser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParserException.Type;

/**
 * This is a representation of a resource group, which is required for a
 * specific service level. It contains the identifier of a assign privacy level
 * and its required value.
 * 
 * @author Marcus Vetter
 * 
 */
public class RequiredResourceGroup implements Serializable {

    private static final long serialVersionUID = 5951904689151789055L;

    /**
     * Identifier of the required resource group
     */
    private String rgIdentifier;

    /**
     * This map contains all required privacy levels of the required resource
     * group. key = identifier of the privacy level
     */
    private Map<String, String> privacyLevels;

    /**
     * Constructor is used to instantiate the data structures and set the
     * required resource group identifier.
     * 
     * @param rgIdentifier
     */
    protected RequiredResourceGroup(String rgIdentifier) {
	privacyLevels = new HashMap<String, String>();
	this.rgIdentifier = rgIdentifier;
    }

    /**
     * Get the identifier of the required resource group
     * 
     * @return identifier of the required resource group
     */
    public String getRgIdentifier() {
	return rgIdentifier;
    }

    /**
     * Set the identifier of the required resource group
     * 
     * @param rgIdentifier
     *            of the required resource group
     */
    protected void setRgIdentifier(String rgIdentifier) {
	this.rgIdentifier = rgIdentifier;
    }

    /**
     * Get all privacy levels of the required resource group
     * 
     * @return map of privacy levels
     */
    public Map<String, String> getPrivacyLevelMap() {
	return privacyLevels;
    }

    /**
     * Add a privacy level to the required resource group
     * 
     * @param identifier
     *            of the privacy level
     * @param requiredValue
     *            of the privacy level
     */
    protected void addPrivacyLevel(String identifier, String requiredValue) {
	if (privacyLevels.containsKey(identifier)) {
	    throw new XMLParserException(
		    Type.PRIVACY_LEVEL_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
		    "The privacy level of a service level with the identifier "
			    + identifier + " already exists.");
	}
	privacyLevels.put(identifier, requiredValue);
    }

}
