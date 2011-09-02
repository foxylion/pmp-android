package de.unistuttgart.ipvs.pmp.service.resource;

/**
 * The registration interface for secure registering at a ResourceGroup.
 *
 * @author Tobias Kuhn
 */
interface IResourceGroupServiceRegister {

	/**
	 * Registers the remote sender with its public key.
	 * 
	 * @param publicKey the public key with which the remote shall be registered
	 */
	void register(in byte[] publicKey);

}