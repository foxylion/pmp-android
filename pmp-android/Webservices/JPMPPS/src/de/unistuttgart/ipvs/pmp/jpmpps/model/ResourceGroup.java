package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.unistuttgart.ipvs.pmp.xmlutil.parser.RGISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Class which holds the information about a resource group (identifier, name,
 * description, revision) in all languages.
 * 
 * @author Jakob Jarosch
 * 
 */
public class ResourceGroup {

	private File path;
	
	private RGIS parsedRGIS = null;

	/**
	 * Creates a new ResourceGroup.
	 */
	public ResourceGroup(File filename) throws FileNotFoundException {
		this.path = filename;

		if (!this.path.isFile()) {
			throw new FileNotFoundException();
		}
	}

	/**
	 * @return Returns the Path to the file.
	 */
	public File getPath() {
		return this.path;
	}

	/**
	 * @return Returns a {@link InputStream} for the {@link ResourceGroup}.
	 */
	public InputStream getInputStream() {
		try {
			return new FileInputStream(this.path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public RGIS getRGIS() {
		if(parsedRGIS != null) {
			return parsedRGIS;
		}
		
		/* Start new parsing proccess. */
		RGISParser parser = new RGISParser();

		try {
			ZipFile zip = new ZipFile(getPath());
			ZipEntry entry = zip.getEntry("res/rgis.xml");
			if (entry == null) {
				System.out.println("[E] rgis.xml does not exist in package " + getPath().getName()+ ".");
				return null;
			}
			InputStream stream = zip.getInputStream(entry);
			parsedRGIS = parser.parse(stream);
			
		} catch (IOException e) {
			System.out.println("[E] Failed to load rgis.xml from package " + getPath().toString() + ", skipping. (Error: " + e.getMessage() + ")");
		}
		
		return null;
	}
}
