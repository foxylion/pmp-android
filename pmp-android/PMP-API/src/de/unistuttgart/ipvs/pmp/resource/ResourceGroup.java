package de.unistuttgart.ipvs.pmp.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A resource group that bundles {@link Resource}s and {@link PrivacyLevel}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ResourceGroup {

    /**
     * The resources present in that resource group.
     */
    private Map<String, Resource> resources;
    
    /**
     * The privacy levels present in that resource group.
     */
    private Map<String, PrivacyLevel> privacyLevels;
    
    public ResourceGroup() {
	resources = new HashMap<String, Resource>();
	privacyLevels = new HashMap<String, PrivacyLevel>();
    }

    /**
     * 
     * @param locale the ISO-639 locale string available from {@link Locale#getLanguage()}
     * @return the name of this resource group for the given locale
     */
    public abstract String getName(String locale);

    /**
     * 
     * @param locale the ISO-639 locale string available from {@link Locale#getLanguage()}
     * @return the description of this resource group for the given locale
     */
    public abstract String getDescription(String locale);
    
    /**
     * Registers resource as resource "identifier" in this resource group.
     * 
     * @param identifier
     * @param resource
     */
    public void registerResource(String identifier, Resource resource) {
	resources.put(identifier, resource);
    }
    
    /**
     * 
     * @param identifier
     * @return the resource identified by "identifier", if present, null
     *         otherwise
     */
    public Resource getResource(String identifier) {
	return resources.get(identifier);
    }
    
    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getResources() {
	return new ArrayList<String>(resources.keySet());
    }
    
    /**
     * Registers privacyLevel as privacy level "identifier" in this resource group.
     * 
     * @param identifier
     * @param privacyLevel
     */
    public void registerPrivacyLevel(String identifier, PrivacyLevel privacyLevel) {
	privacyLevels.put(identifier, privacyLevel);
    }
    
    /**
     * 
     * @param identifier
     * @return the privacy level identified by "identifier", if present, null
     *         otherwise
     */
    public PrivacyLevel getPrivacyLevel(String identifier) {
	return privacyLevels.get(identifier);
    }
    
    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getPrivacyLevels() {
	return new ArrayList<String>(privacyLevels.keySet());
    }
}
