package de.unistuttgart.ipvs.pmp.resource;

import java.util.Locale;
import java.util.Map;

/**
 * A resource group that bundles {@link Resource}s.
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
     * 
     * @param locale
     * @return the name of this resource group for the given locale
     */
    public abstract String getName(Locale locale);

    /**
     * 
     * @param locale
     * @return the description of this resource group for the given locale
     */
    public abstract String getDescription(Locale locale);

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
     * Registers resource as identifier in this resource group.
     * 
     * @param identifier
     * @param resource
     */
    public void registerResource(String identifier, Resource resource) {
	resources.put(identifier, resource);
    }
}
