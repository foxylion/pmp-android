package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * {@link AbstractResponse} gives ability to convert given data into byte arrays
 * to save bandwidth
 * 
 * @author Jakob Jarosch
 */
public abstract class AbstractResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a byte array from an {@link Object}.
	 * 
	 * @param object
	 *            {@link Object} which should be converted into an byte array.
	 * @param compression
	 *            True when byte array should be compressed, otherwise false.
	 * @return Converted {@link Object}.
	 */
	protected byte[] toByteArray(Object object, boolean compression) {
		byte[] result = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out;
			if (compression) {
				out = new ObjectOutputStream(new GZIPOutputStream(baos));
			} else {
				out = new ObjectOutputStream(baos);
			}
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
	 * @param compression
	 *            True when byte array should be compressed, otherwise false.
	 * @return Converted {@link InputStream}.
	 */
	protected byte[] toByteArray(InputStream in, boolean compression) {
		byte[] result = null;
		try {
			BufferedReader inR = new BufferedReader(new InputStreamReader(in));
			ByteArrayOutputStream aios = new ByteArrayOutputStream();

			BufferedOutputStream out;

			if (compression) {
				out = new BufferedOutputStream(new GZIPOutputStream(aios));
			} else {
				out = new BufferedOutputStream(aios);
			}

			int readByte;

			while ((readByte = inR.read()) != -1) {
				out.write(readByte);
			}

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
	 * @param uncompression
	 *            True when byte array should be uncompressed, otherwise false.
	 * @return Converted byte array.
	 */
	protected InputStream fromByteArray(byte[] data, boolean uncompression) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			if (uncompression) {
				return new GZIPInputStream(bais);
			} else {
				return bais;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
