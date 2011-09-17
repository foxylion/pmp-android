package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * This resource gives access to external files stored on the device.
 * The used external directory (e.g. "/Music" or "/Ringtones") is selected in the constructor.
 * @author Patrick Strobel
 * @version 0.1.0
 */
public class ExternalFileAccessResource extends Resource {
	
	private ExternalFileAccess.Directories directory;
	
	/**
	 * Creates a new resource.
	 * @param directory	Directory to which this resource shoud grant access
	 */
	public ExternalFileAccessResource(ExternalFileAccess.Directories directory) {
		this.directory = directory;
	}

	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		return new ExternalFileAccess(appIdentifier, this, directory);
	}

}
