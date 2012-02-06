package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Answer of the server for a specific rgis request.
 * 
 * @author Jakob Jarosch
 */
public class RGISResponse extends AbstractResponse {

	private static final long serialVersionUID = 2L;

	private byte[] rgis = null;

	/**
	 * Creates a new {@link RGISResponse}.
	 * 
	 * @param resourceGroup {@link InputStream} of the package which should be attached.
	 */
	public RGISResponse(Serializable rgis, byte[] cacheHash) {
		if (!rgis.getClass().getSimpleName().equals("RGIS")) {
			throw new IllegalArgumentException();
		}
		
		this.rgis = toByteArray(rgis);
		setCacheHash(cacheHash);
	}

	/**
	 * @return Returns the package as an {@link Serializable}. Must be casted to RGIS.
	 */
	public Object getRGIS() {
		try {
			ObjectInputStream ois = new ObjectInputStream(fromByteArray(this.rgis));
			return ois.readObject();
		} catch(ClassNotFoundException e) {
			throw new IllegalAccessError("When you use this method you must also add the PMP-XML-UTILITIES to your library");
		} catch (IOException e) {
			System.out.println("[E] While reading the object from bytearray (Error: " + e.getMessage() + ")");
			e.printStackTrace();
		}
		return null;
	}
}
