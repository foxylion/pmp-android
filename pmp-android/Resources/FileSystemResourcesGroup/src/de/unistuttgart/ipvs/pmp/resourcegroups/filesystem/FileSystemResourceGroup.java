package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import android.content.Context;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;

public class FileSystemResourceGroup extends ResourceGroup {
	
	private final Context serviceContext;

	public FileSystemResourceGroup(Context serviceContext,
			Class<? extends PMPSignedService> service) {
		super(serviceContext, service);
		this.serviceContext = serviceContext;
	}

	@Override
	public String getName(String locale) {
		if (locale.equalsIgnoreCase("de")) {
			return "Dateisystem";
		} else {
			return "Filesystem";
		}
	}

	@Override
	public String getDescription(String locale) {
		if (locale.equalsIgnoreCase("de")) {
			return "Bietet Anwendungen Zugriff auf auf dem Gerät gespeicherte Dateien.";
		} else {
			return "Gives applications access to files saved on the device.";
		}
	}

	@Override
	protected String getServiceAndroidName() {
		return "de.unistuttgart.ipvs.pmp.resources.FileSystemService";
	}

	@Override
	public void onRegistrationSuccess() {
		Toast toast = Toast.makeText(serviceContext, 
						"Registration successfull", Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onRegistrationFailed(String message) {
		Toast toast = Toast.makeText(serviceContext, 
				"Registration failed. PMP says: " + message, Toast.LENGTH_SHORT);
		toast.show();
	}

}
