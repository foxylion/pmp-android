package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import android.content.Context;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;

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

	public static final String SERVICE_NAME = "de.unistuttgart.ipvs.pmp.resources.FileSystemService";


	/**
	 * Context of service in which this resource-group is running.
	 */
	private final Context context;

	/**
	 * Creates the resource-group including its privacy-levels and resources
	 * 
	 * @param context
	 *            Context of the service giving access to our resource-group
	 * @param service
	 *            Class of our service.
	 * @throws Exception
	 *             Throws if at least one privacy-level could not be
	 *             instantiated.
	 */
	public FileSystemResourceGroup(Context context) throws Exception {
		super(context);

		// Store the service' context, because we will need it later
		this.context = context;
		
		// Generate privacy-levels and register them
		PrivacyLevels privacyLevels = new PrivacyLevels();
		privacyLevels.addToResourceGroup(this);
		
		// Generate resources and register them
		Resources resources = new Resources();
		resources.addToResourceGroup(this);
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
			Toast toast = Toast.makeText(context,
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
			Toast toast = Toast.makeText(context,
					"Registration failed. PMP says: " + message,
					Toast.LENGTH_SHORT);
			toast.show();

		} catch (Throwable t) {
			Log.d("Could not show toast-notification.", t);
		}
	}

}
