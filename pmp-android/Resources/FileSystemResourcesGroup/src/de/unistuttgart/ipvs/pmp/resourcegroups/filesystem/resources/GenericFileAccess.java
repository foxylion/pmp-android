package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
		
		File file = new File(path);
		
		try {
			// Read file to string
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			StringBuilder result = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();
			return result.toString();
			
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
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
			writer.write(data);
			writer.close();
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
		
		File file = new File(path);
		return file.delete();
	}

	@Override
	public List<FileDetails> list(String directory) throws RemoteException {
		// Check if application is allowed to use this function
		if (!privacyLevelSet(PrivacyLevels.GENERIC_LIST)) {
			throw new IllegalAccessError("Generic file listing not allowed");
		}

		File file = new File(directory);
		File[] fileArray = file.listFiles();

		// The user has selected a file instead of a directory
		if (fileArray == null) {
			return null;
		}

		// Generate our FileDetails-List
		List<FileDetails> detailsList = new ArrayList<FileDetails>();

		for (File f : fileArray) {
			detailsList.add(new FileDetails(f));
		}

		return detailsList;
	}

	/**
	 * Checks if a specific privacy-level ist set for an application
	 * @param privacyLevelName The privacy-level to check
	 * @return True, if privacy-level is set for this application
	 */
	private boolean privacyLevelSet(String privacyLevelName) {
		return PrivacyLevels.privacyLevelSet(privacyLevelName, app, resource);
	}

}
