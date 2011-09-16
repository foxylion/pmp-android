package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.PrivacyLevels;

import android.os.Environment;
import android.os.RemoteException;

/**
 * Handles the access to a external resource
 * @author Patrick Strobel
 * @version 0.1.0
 */
public class ExternalFileAccess extends IFileAccess.Stub {

	private String app;
	private ExternalFileAccessResource resource;

	public enum Directories {
		MUSIC, PODCASTS, RINGTONES, ALARMS, NOTIFICATIONS, PICTURES, MOVIES, DOWNLOAD
	};

	private enum Functions {
		READ, WRITE, LIST, DELETE
	};

	private final Directories directory;

	/**
	 * Creates a new instance
	 * @param app		Identifier of the app that is using this access handler
	 * @param resource	Resource this access handler object belongs to
	 * @param dir		External directory this gives access to
	 */
	public ExternalFileAccess(String app, ExternalFileAccessResource resource,
			Directories dir) {
		this.app = app;
		this.resource = resource;
		this.directory = dir;

	}

	@Override
	public String read(String path) throws RemoteException {
		// Check if application is allowed to use this function
		if (!privacyLevelSet(Functions.READ)) {
			throw new IllegalAccessError("Generic file reading not allowed");
		}

		try {
			return Utils.readFileToString(getExternalDirectory(path));
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
		if (!privacyLevelSet(Functions.WRITE)) {
			throw new IllegalAccessError("Generic file reading not allowed");
		}
		// TODO Auto-generated method stub
		try {
			Utils.writeStringToFile(getExternalDirectory(path), data, append);
			return true;
		} catch (IOException e) {
			Log.d("Cannot write data to " + path);
			return false;
		}
	}

	@Override
	public boolean delete(String path) throws RemoteException {
		// Check if application is allowed to use this function
		if (!privacyLevelSet(Functions.DELETE)) {
			throw new IllegalAccessError("Generic file reading not allowed");
		}

		return getExternalDirectory(path).delete();
	}

	@Override
	public List<FileDetails> list(String directory) throws RemoteException {
		// Check if application is allowed to use this function
		if (!privacyLevelSet(Functions.LIST)) {
			throw new IllegalAccessError("Generic file reading not allowed");
		}

		return Utils.getFileDetailsList(getExternalDirectory(directory));
	}

	/**
	 * Returns the path to the external storage
	 * (&lt;Directory&gt;/&lt;subpath&gt;)
	 * 
	 * @param subpath
	 *            Subpath &lt;subpath&gt; in the external directory
	 * @return Computet path to the external storage
	 */
	private File getExternalDirectory(String subpath) {
		File root = Environment.getExternalStorageDirectory();
		File baseDir = null;
		switch (directory) {
		case MUSIC:
			baseDir = new File(root, "Music");
			break;
		case PODCASTS:
			baseDir = new File(root, "Podcasts");
			break;
		case RINGTONES:
			baseDir = new File(root, "Ringtones");
			break;
		case ALARMS:
			baseDir = new File(root, "Alarms");
			break;
		case NOTIFICATIONS:
			baseDir = new File(root, "Notifications");
			break;
		case PICTURES:
			baseDir = new File(root, "Pictures");
			break;
		case MOVIES:
			baseDir = new File(root, "Movies");
			break;
		case DOWNLOAD:
			baseDir = new File(root, "Download");
			break;
		}

		return new File(baseDir, subpath);
	}

	/**
	 * Checks if a specific privacy-level ist set for an application
	 * 
	 * @param privacyLevelName
	 *            The privacy-level to check
	 * @return True, if privacy-level is set for this application
	 */
	private boolean privacyLevelSet(Functions function) {
		String privacyLevelName = null;

		switch (function) {
		// Read function
		case READ:
			switch (directory) {
			case MUSIC:
				privacyLevelName = PrivacyLevels.EXTERNAL_MUSIC_READ;
				break;
			case PODCASTS:
				privacyLevelName = PrivacyLevels.EXTERNAL_PODCASTS_READ;
				break;
			case RINGTONES:
				privacyLevelName = PrivacyLevels.EXTERNAL_RINGTONES_READ;
				break;
			case ALARMS:
				privacyLevelName = PrivacyLevels.EXTERNAL_ALARMS_READ;
				break;
			case NOTIFICATIONS:
				privacyLevelName = PrivacyLevels.EXTERNAL_NOTIFICATIONS_READ;
				break;
			case PICTURES:
				privacyLevelName = PrivacyLevels.EXTERNAL_PICTURES_READ;
				break;
			case MOVIES:
				privacyLevelName = PrivacyLevels.EXTERNAL_MOVIES_READ;
				break;
			case DOWNLOAD:
				privacyLevelName = PrivacyLevels.EXTERNAL_DOWNLOAD_READ;
				break;
			}
			break;
		case WRITE:
			// Write function
			switch (directory) {
			case MUSIC:
				privacyLevelName = PrivacyLevels.EXTERNAL_MUSIC_WRITE;
				break;
			case PODCASTS:
				privacyLevelName = PrivacyLevels.EXTERNAL_PODCASTS_WRITE;
				break;
			case RINGTONES:
				privacyLevelName = PrivacyLevels.EXTERNAL_RINGTONES_WRITE;
				break;
			case ALARMS:
				privacyLevelName = PrivacyLevels.EXTERNAL_ALARMS_WRITE;
				break;
			case NOTIFICATIONS:
				privacyLevelName = PrivacyLevels.EXTERNAL_NOTIFICATIONS_WRITE;
				break;
			case PICTURES:
				privacyLevelName = PrivacyLevels.EXTERNAL_PICTURES_WRITE;
				break;
			case MOVIES:
				privacyLevelName = PrivacyLevels.EXTERNAL_MOVIES_WRITE;
				break;
			case DOWNLOAD:
				privacyLevelName = PrivacyLevels.EXTERNAL_DOWNLOAD_WRITE;
				break;
			}
			break;
		case LIST:
			// Write function
			switch (directory) {
			case MUSIC:
				privacyLevelName = PrivacyLevels.EXTERNAL_MUSIC_LIST;
				break;
			case PODCASTS:
				privacyLevelName = PrivacyLevels.EXTERNAL_PODCASTS_LIST;
				break;
			case RINGTONES:
				privacyLevelName = PrivacyLevels.EXTERNAL_RINGTONES_LIST;
				break;
			case ALARMS:
				privacyLevelName = PrivacyLevels.EXTERNAL_ALARMS_LIST;
				break;
			case NOTIFICATIONS:
				privacyLevelName = PrivacyLevels.EXTERNAL_NOTIFICATIONS_LIST;
				break;
			case PICTURES:
				privacyLevelName = PrivacyLevels.EXTERNAL_PICTURES_LIST;
				break;
			case MOVIES:
				privacyLevelName = PrivacyLevels.EXTERNAL_MOVIES_LIST;
				break;
			case DOWNLOAD:
				privacyLevelName = PrivacyLevels.EXTERNAL_DOWNLOAD_LIST;
				break;
			}
			break;
		case DELETE:
			// Write function
			switch (directory) {
			case MUSIC:
				privacyLevelName = PrivacyLevels.EXTERNAL_MUSIC_DELETE;
				break;
			case PODCASTS:
				privacyLevelName = PrivacyLevels.EXTERNAL_PODCASTS_DELETE;
				break;
			case RINGTONES:
				privacyLevelName = PrivacyLevels.EXTERNAL_RINGTONES_DELETE;
				break;
			case ALARMS:
				privacyLevelName = PrivacyLevels.EXTERNAL_ALARMS_DELETE;
				break;
			case NOTIFICATIONS:
				privacyLevelName = PrivacyLevels.EXTERNAL_NOTIFICATIONS_DELETE;
				break;
			case PICTURES:
				privacyLevelName = PrivacyLevels.EXTERNAL_PICTURES_DELETE;
				break;
			case MOVIES:
				privacyLevelName = PrivacyLevels.EXTERNAL_MOVIES_DELETE;
				break;
			case DOWNLOAD:
				privacyLevelName = PrivacyLevels.EXTERNAL_DOWNLOAD_DELETE;
				break;
			}
			break;
		}

		return PrivacyLevels.privacyLevelSet(privacyLevelName, app, resource);
	}

}
