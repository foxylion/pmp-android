package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import android.content.Context;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;

/**
 * This resource gives access to files saved on the user's Andorid device. To do
 * so, it defines several privacy-levels, such as privacy-levels for reading or
 * writing files on the device's file system. It also registers all
 * corresponding resources.
 * 
 * @author Patrick Strobel
 * @version 0.2.0
 */
public class FileSystemResourceGroup extends ResourceGroup {

	public static final String SERVICE_NAME = "";

	public static final String PRIVACY_LEVEL_READ = "r";
	public static final String PRIVACY_LEVEL_WRITE = "w";
	public static final String PRIVACY_LEVEL_DELETE = "d";
	public static final String PRIVACY_LEVEL_LIST = "l";

	public static final String RESOURCE_ACCESS_FILE_SYSTEM = "access";

	/**
	 * Context of service in which this resource-group is running.
	 */
	private final Context serviceContext;

	/**
	 * Creates the resource-group including its privacy-levels and resources
	 * 
	 * @param serviceContext
	 *            Context of the service giving access to our resource-group
	 * @param service
	 *            Class of our service.
	 * @throws Exception
	 *             Throws if at least one privacy-level could not be
	 *             instantiated.
	 */
	public FileSystemResourceGroup(Context serviceContext,
			Class<? extends PMPSignedService> service) throws Exception {
		super(serviceContext);

		// Store the service' context, because we will need it later
		this.serviceContext = serviceContext;
		
		// Generate privacy-levels and register them
		PrivacyLevels privacyLevels = new PrivacyLevels();
		privacyLevels.addToResourceGroup(this);
		
		// Generate resources and register them
		Resources resources = new Resources();
		resources.addToResourceGroup(this);

		/*
		// Creates privacy-levels for reading, writing, deleting and listing
		// files.
		// We do not need a specialized implementation of our privacy-levels,
		// so we are using the implementation provided by "SimplePrivacyLevel".
		// As we want to use the privacy-levels' names and descriptions
		// stored in the Andorid resource-file (values/string.xml), we
		// set the name and description parameters to "null".
		SimplePrivacyLevel read = new SimplePrivacyLevel(Boolean.class, null,
				null);
		SimplePrivacyLevel write = new SimplePrivacyLevel(Boolean.class, null,
				null);
		SimplePrivacyLevel delete = new SimplePrivacyLevel(Boolean.class, null,
				null);
		SimplePrivacyLevel list = new SimplePrivacyLevel(Boolean.class, null,
				null);

		// Register our new privacy-levels to make them accessible by PMP
		// and Apps using PMP
		this.registerPrivacyLevel(PRIVACY_LEVEL_READ, read);
		this.registerPrivacyLevel(PRIVACY_LEVEL_WRITE, write);
		this.registerPrivacyLevel(PRIVACY_LEVEL_DELETE, delete);
		this.registerPrivacyLevel(PRIVACY_LEVEL_LIST, list);
		

		// Register all resources used by this resource group
		GenericFileAccessResource resource = new GenericFileAccessResource();
		this.registerResource(RESOURCE_ACCESS_FILE_SYSTEM, resource);
		*/

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
			return "Bietet Anwendungen Zugriff auf auf dem Ger�t gespeicherte Dateien.";
		} else {
			return "Gives applications access to files saved on the device.";
		}
	}

	@Override
	protected String getServiceAndroidName() {
		return SERVICE_NAME;
	}

	@Override
	public void onRegistrationSuccess() {
		// To keep it simple, we just make a log and tell the user that
		// everything went fine
		Log.d("Registration was successfull");

		try {
			Toast toast = Toast.makeText(serviceContext,
					"Registration successfull", Toast.LENGTH_SHORT);
			toast.show();
		} catch (Throwable t) {
			Log.d("Could not show toast-notification.", t);
		}
	}

	@Override
	public void onRegistrationFailed(String message) {
		// As in "onRegistrationSuccess()", we make a log and tell the user what
		// had happened
		Log.d("Registration failed: " + message);

		try {
			Toast toast = Toast.makeText(serviceContext,
					"Registration failed. PMP says: " + message,
					Toast.LENGTH_SHORT);
			toast.show();

		} catch (Throwable t) {
			Log.d("Could not show toast-notification.", t);
		}
	}

}
