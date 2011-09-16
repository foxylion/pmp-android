package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * This resource gives access to every file or directory on the device.
 * 
 * @author Patrick Strobel
 * @version 0.1.0
 */
public class GenericFileAccessResource extends Resource {

	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		return new GenericFileAccess(appIdentifier, this);
	}

}
