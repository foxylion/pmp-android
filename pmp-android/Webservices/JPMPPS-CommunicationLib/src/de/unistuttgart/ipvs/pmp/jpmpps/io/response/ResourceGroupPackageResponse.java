package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

import java.io.InputStream;

/**
 * Answer of the server for a specific package request.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupPackageResponse extends AbstractResponse {

	private static final long serialVersionUID = 2L;

	private byte[] resourceGroup = null;

	/**
	 * Creates a new {@link ResourceGroupPackageResponse}.
	 * 
	 * @param resourceGroup {@link InputStream} of the package which should be attached.
	 */
	public ResourceGroupPackageResponse(InputStream resourceGroup, byte[] cacheHash) {
		this.resourceGroup = toByteArray(resourceGroup);
		setCacheHash(cacheHash);
	}

	/**
	 * @return Returns the package as an {@link InputStream}.
	 */
	public InputStream getResourceGroupInputStream() {
		return fromByteArray(this.resourceGroup);
	}
}