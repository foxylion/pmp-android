package de.unistuttgart.ipvs.pmp.service.resource;

/**
 * The interface for an app communicating with a ResourceGroup.
 *
 * @author Tobias Kuhn
 */
interface IResourceGroupServiceApp {

	/**
	 * @return Returns a List<String> of all resources available.
	 */
	List getResources();
	
	/**
	 * @param resourceIdentifier the resource to be identified
	 * @return the interface for interacting with that resource
	 */
	IBinder getResource(String resourceIdentifier);

}