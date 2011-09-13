package de.unistuttgart.ipvs.pmp.resource;

import android.app.Application;
import de.unistuttgart.ipvs.pmp.service.resource.ResourceGroupService;

/**
 * The default resource group app to implement for creating an App that stores
 * {@link ResourceGroup}s.
 * 
 * You can use {@link ResourceGroupSingleApp}, if you intend to store only one
 * {@link ResourceGroup}.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ResourceGroupApp extends Application {

    /**
     * Fetches the applicable {@link ResourceGroup} for an instance of an
     * {@link ResourceGroupService}.
     * 
     * @param rgs
     *            the service asking for the resourcegroup
     * @return the {@link ResourceGroup} associated with that service
     */
    public abstract ResourceGroup getResourceGroupForService(
	    ResourceGroupService rgs);

}
