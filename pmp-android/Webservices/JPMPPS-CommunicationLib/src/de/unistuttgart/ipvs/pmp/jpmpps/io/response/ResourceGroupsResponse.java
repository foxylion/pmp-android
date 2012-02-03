package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

import java.io.ObjectInputStream;
import java.io.Serializable;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;

/**
 * Response when {@link RequestResourceGroups} was sent as an request.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupsResponse extends AbstractResponse implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] resourceGroups;
	
	private byte[] hash;

	/**
	 * Creates a new {@link ResourceGroupsResponse}.
	 * 
	 * @param rgs {@link LocalizedResourceGroup} which should be attached.
	 */
	public ResourceGroupsResponse(LocalizedResourceGroup[] rgs, byte[] hash) {
		this.resourceGroups = toByteArray(rgs, false);
		this.hash = hash;
	}

	/**
	 * @return Returns all attached {@link LocalizedResourceGroup}s.
	 */
	public LocalizedResourceGroup[] getResourceGroups() {
		LocalizedResourceGroup[] rgs = null;

		try {
			ObjectInputStream ois = new ObjectInputStream(fromByteArray(
					resourceGroups, false));
			rgs = (LocalizedResourceGroup[]) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rgs;
	}

	/**
	 * @return Returns the hash of the response.
	 */
	public byte[] getHash() {
		return hash;
	}
}
