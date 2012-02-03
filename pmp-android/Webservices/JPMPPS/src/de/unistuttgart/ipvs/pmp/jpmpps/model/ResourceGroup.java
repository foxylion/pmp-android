package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Class which holds the information about a resource group (identifier, name,
 * description, revision) in all languages.
 * 
 * @author Jakob Jarosch
 * 
 */
public class ResourceGroup {

	private File path;
	
	/**
	 * Creates a new ResourceGroup.
	 */
	public ResourceGroup(String filename) throws FileNotFoundException {
		this.path = new File(filename);
		
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
}
