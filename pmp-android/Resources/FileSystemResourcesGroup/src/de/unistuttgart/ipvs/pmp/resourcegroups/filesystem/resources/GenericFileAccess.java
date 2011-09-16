package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.PrivacyLevels;

import android.os.RemoteException;

/**
 * This class handled the access to all file stored on the device
 * @author Patrick Strobel
 * @version 0.1.0
 * 
 */
public class GenericFileAccess extends IFileAccess.Stub {

	private String app;
	private GenericFileAccessResource resource;

	/**
	 * Creates a new instance
	 * @param app		Identifier of the app that is using this access handler
	 * @param resource	Resource this access handler object belongs to
	 */
	public GenericFileAccess(String app, GenericFileAccessResource resource) {
		this.app = app;
		this.resource = resource;
	}

	@Override
	public String read(String path) throws RemoteException {
		// Check if application is allowed to use this function
		if (!privacyLevelSet(PrivacyLevels.GENERIC_READ)) {
			throw new IllegalAccessError("Generic file reading not allowed");
		}
		
		try {
			return Utils.readFileToString(new File(path));			
		} catch (FileNotFoundException e) {
			Log.d("Cannot open file: " + path, e);
			throw new RemoteException();
		} catch (IOException e) {
			Log.d("Cannot read file", e);
			throw new RemoteException();
		}
	}

	@Override
	public boolean write(String path, String data, boolean append)
			throws RemoteException {
		// Check if application is allowed to use this function
		if (!privacyLevelSet(PrivacyLevels.GENERIC_WRITE)) {
			throw new IllegalAccessError("Generic file writing not allowed");
		}
		
		File file = new File(path);
		try {
			Utils.writeStringToFile(file, data, append);
			return true;
		} catch (IOException e) {
			Log.d("Cannot write data to " + path);
			return false;
		}
	}

	@Override
	public boolean delete(String path) throws RemoteException {
		// Check if application is allowed to use this function
		if (!privacyLevelSet(PrivacyLevels.GENERIC_DELETE)) {
			throw new IllegalAccessError("Generic file deleting not allowed");
		}
		
		return new File(path).delete();
	}

	@Override
	public List<FileDetails> list(String directory) throws RemoteException {
		// Check if application is allowed to use this function
		if (!privacyLevelSet(PrivacyLevels.GENERIC_LIST)) {
			throw new IllegalAccessError("Generic file listing not allowed");
		}

		return Utils.getFileDetailsList(new File(directory));
	}

	/**
	 * Checks if a specific privacy-level is set for an application
	 * @param privacyLevelName The privacy-level to check
	 * @return True, if privacy-level is set for this application
	 */
	private boolean privacyLevelSet(String privacyLevelName) {
		return PrivacyLevels.privacyLevelSet(privacyLevelName, app, resource);
	}

}
