package de.unistuttgart.ipvs.pmp.resource;

import de.unistuttgart.ipvs.pmp.service.resource.ResourceGroupService;

/**
 * Easy-to-use {@link ResourceGroupApp} if you're writing an App that only
 * requires one {@link ResourceGroup}. Set your ResourceGroup with
 * {@link ResourceGroupSingleApp#setResourceGroup(ResourceGroup)}, preferably
 * during initialization.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroupSingleApp extends ResourceGroupApp {

    private ResourceGroup resourceGroup = null;

    public void setResourceGroup(ResourceGroup resourceGroup) {
	this.resourceGroup = resourceGroup;
    }

    @Override
    public ResourceGroup getResourceGroupForService(ResourceGroupService rgs) {
	return resourceGroup;
    }

}
