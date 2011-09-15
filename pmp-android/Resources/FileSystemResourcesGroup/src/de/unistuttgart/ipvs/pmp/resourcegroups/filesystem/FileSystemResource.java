package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * This class is used to return the interface handling the file system access
 * 
 * @author Patrick Strobel
 * @version 0.1.0
 */
public class FileSystemResource extends Resource {

	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		return new FileSystemAccess();
	}

}
