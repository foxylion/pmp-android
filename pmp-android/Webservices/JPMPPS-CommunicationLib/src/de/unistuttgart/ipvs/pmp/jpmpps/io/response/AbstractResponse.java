package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * {@link AbstractResponse} gives ability to convert given data into byte arrays
 * to save bandwidth
 * 
 * @author Jakob Jarosch
 */
public abstract class AbstractResponse implements Serializable {

	private static final long serialVersionUID = 2L;

	private byte[] cacheHash = new byte[0];
	
	/**
	 * @return Returns the CacheHash of the response.
	 */
	public byte[] getCacheHash() {
		return cacheHash;
	}
	
	/**
	 * Sets a cacheHash for the response.
	 */
	protected void setCacheHash(byte[] cacheHash) {
		this.cacheHash = cacheHash;
	}
	
	/**
	 * Creates a byte array from an {@link Object}.
	 * 
	 * @param object
	 *            {@link Object} which should be converted into an byte array.
	 * @return Converted {@link Object}.
	 */
	protected byte[] toByteArray(Object object) {
		byte[] result = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(object);
			result = baos.toByteArray();

			out.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Creates a byte array from an {@link InputStream}.
	 * 
	 * @param in
	 *            {@link InputStream} which should be converted into an byte
	 *            array.
	 * @return Converted {@link InputStream}.
	 */
	protected byte[] toByteArray(InputStream in) {
		byte[] result = null;

		try {
			BufferedInputStream inR = new BufferedInputStream(in);
			ByteArrayOutputStream aios = new ByteArrayOutputStream();

			BufferedOutputStream out = new BufferedOutputStream(aios);
			final int BUFFER_SIZE = 32 * 1024;
			byte[] buffer = new byte[BUFFER_SIZE];

			int read = -1;
			do {
				read = inR.read(buffer, 0, BUFFER_SIZE);
				if (read > -1) {
					out.write(buffer, 0, read);
				}
			} while (read > -1);

			result = aios.toByteArray();

			inR.close();
			out.close();
			aios.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Creates a {@link InputStream} from an byte array.
	 * 
	 * @param data
	 *            byte array which should be converted to an {@link InputStream}
	 *            .
	 * @return Converted byte array.
	 */
	protected InputStream fromByteArray(byte[] data) {
		return new ByteArrayInputStream(data);
	}
}
