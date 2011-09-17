package de.unistuttgart.ipvs.pmp.resource;

import java.io.Serializable;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;

/**
 * A App representation for the {@link IResourceGroupServicePMP}.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupAccess implements Serializable {

    private static final long serialVersionUID = 6045269413834594529L;
    
    private ResourceGroupAccessHeader header;
    private Map<String, String> privacyLevelValues;

    /**
     * Creates a new {@link ResourceGroupAccess}.
     * 
     * @param header
     *            header of this access set.
     * @param privacyLevelValues
     *            Bundle of privacy levels and their set values
     */
    public ResourceGroupAccess(ResourceGroupAccessHeader header,
	    Map<String, String> privacyLevelValues) {
	this.header = header;
	this.privacyLevelValues = privacyLevelValues;
    }

    public ResourceGroupAccessHeader getHeader() {
	return this.header;
    }

    /**
     * Returns the corresponding value to a privacy level.
     * 
     * @param privacyLevel
     *            name of the privacy level
     * @return value of the privacy level or NULL if it is not set
     */
    public String getPrivacyLevelValue(String privacyLevel) {
	return privacyLevelValues.get(privacyLevel);
    }

    /**
     * @return the bundle containing the privacy level values.
     */
    public Map<String, String> getPrivacyLevelValues() {
	return privacyLevelValues;
    }
}
