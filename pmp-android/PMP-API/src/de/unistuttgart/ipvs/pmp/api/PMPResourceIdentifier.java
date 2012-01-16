package de.unistuttgart.ipvs.pmp.api;

import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * A storage object for the identity of a {@link Resource} in PMP.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPResourceIdentifier {
    
    private final String resourceGroup;
    private final String resource;
    
    
    private PMPResourceIdentifier(String resourceGroup, String resource) {
        this.resourceGroup = resourceGroup;
        this.resource = resource;
    }
    
    
    protected String getResourceGroup() {
        return this.resourceGroup;
    }
    
    
    protected String getResource() {
        return this.resource;
    }
    
    
    /**
     * Creates a new {@link PMPResourceIdentifier} identifying one specific resource throughout PMP. This is useful for
     * abbreviating calls to the {@link PMP} API. You can store the
     * returned object for as long as you want, but this is entirely optional.
     * 
     * @param resourceGroupPackage
     *            the package of the resource group you want to access
     * @param resource
     *            the identifier of the resource inside the resource group
     * @return an object that identifies the specific resource throughout PMP
     */
    public static PMPResourceIdentifier make(String resourceGroupPackage, String resource) {
        return new PMPResourceIdentifier(resourceGroupPackage, resource);
    }
}
