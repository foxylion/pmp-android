package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;

public class FileSystemResourceGroupApp extends ResourceGroupSingleApp<FileSystemResourceGroup> {

	@Override
	protected FileSystemResourceGroup createResourceGroup() {
		
		FileSystemService service = new FileSystemService();
		
		return new FileSystemResourceGroup(service.getApplicationContext(),
				FileSystemService.class);
	}



}
