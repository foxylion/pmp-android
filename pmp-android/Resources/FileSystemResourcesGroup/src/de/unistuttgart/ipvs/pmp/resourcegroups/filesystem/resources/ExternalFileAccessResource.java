package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

public class ExternalFileAccessResource extends Resource {
	
	private ExternalFileAccess.Directories directory;
	
	public ExternalFileAccessResource(ExternalFileAccess.Directories directory) {
		this.directory = directory;
	}

	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		return new ExternalFileAccess(appIdentifier, this, directory);
	}

}
