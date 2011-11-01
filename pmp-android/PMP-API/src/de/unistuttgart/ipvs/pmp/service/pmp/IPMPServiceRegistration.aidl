package de.unistuttgart.ipvs.pmp.service.pmp;

/**
 * Interface for communicating between PMPService and ResourceGroups/Apps.
 *
 * @author Jakob Jarosch
 */
interface IPMPServiceRegistration {
	
	/**
	 * Method for registering a new App at PMP.
	 *
	 * @param publicKey The public key, for validating the submitted signature in further bindings.
	 * @return Returns the public key of PMP, or null if the identifier was null.
	 */
	byte[] registerApp(in byte[] publicKey);

	/**
	 * Method for registering a new ResourceGroup at PMP.
	 *
	 * @param publicKey The public key, for validating the submitted signature in further bindings.
	 * @return Returns the public key of PMP, or null if the identifier was null.
	 */
	byte[] registerResourceGroup(in byte[] publicKey);
}