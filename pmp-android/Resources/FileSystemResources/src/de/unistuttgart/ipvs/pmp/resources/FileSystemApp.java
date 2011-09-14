package de.unistuttgart.ipvs.pmp.resources;

import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;

public class FileSystemApp extends ResourceGroupSingleApp<FileSystemResourceGroup> {

	@Override
	protected FileSystemResourceGroup createResourceGroup() {
		
		FileSystemService service = new FileSystemService();
		
		return new FileSystemResourceGroup(service.getApplicationContext(),
				FileSystemService.class);
	}



}
