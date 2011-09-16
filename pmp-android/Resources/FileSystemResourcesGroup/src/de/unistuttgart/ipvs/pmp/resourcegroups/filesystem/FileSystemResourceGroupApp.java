package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;

/**
 * Application class encapsulating all resource-groups and its services.
 * Currently we have only one resource-group, so we are using
 * "ResourceGroupSingleApp" as our base-class instead of "ResourceGroupApp".
 * This keeps our application class simple.
 *   
 * @author Patrick Strobel
 * @version 0.1.0
 *
 */
public class FileSystemResourceGroupApp extends ResourceGroupSingleApp<FileSystemResourceGroup> {

	static {
		Log.setTagSufix("FilesystemRG");
	}
	
	
	@Override
	protected FileSystemResourceGroup createResourceGroup() {
		
		FileSystemService service = new FileSystemService();
		
		try {
			return new FileSystemResourceGroup(service.getApplicationContext(),
					FileSystemService.class);
		} catch (Exception e) {
			Log.e("Cannot instantiate  resource-group.", e);
			return null;
		}
	}



}
