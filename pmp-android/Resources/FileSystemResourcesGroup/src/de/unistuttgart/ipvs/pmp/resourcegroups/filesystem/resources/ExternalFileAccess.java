package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.os.Environment;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.PrivacyLevels;

/**
 * Handles the access to external directories.
 * 
 * @author Patrick Strobel
 * @version 0.2.1
 */
public class ExternalFileAccess extends IFileAccess.Stub {
    
    private String app;
    private ExternalFileAccessResource resource;
    
    public enum Directories {
        BASE_DIR,
        MUSIC,
        PODCASTS,
        RINGTONES,
        ALARMS,
        NOTIFICATIONS,
        PICTURES,
        MOVIES,
        DOWNLOAD
    };
    
    private enum Functions {
        READ,
        WRITE,
        LIST,
        DELETE,
        MAKE_DIRS
    };
    
    private final Directories directory;
    
    private static final String SWITCHING_EXCEPTION = "Switching in upper directories is not allowed";
    
    
    /**
     * Creates a new instance
     * 
     * @param app
     *            Identifier of the app that is using this access handler
     * @param resource
     *            Resource this access handler object belongs to
     * @param dir
     *            External directory this gives access to
     */
    protected ExternalFileAccess(String app, ExternalFileAccessResource resource, Directories dir) {
        this.app = app;
        this.resource = resource;
        this.directory = dir;
        
    }
    
    
    /**
     * Reads a file stored in the external directory into a string.
     * 
     * @param path
     *            Path to the file in the external directory as sub-directory of the currently selected external directory.
     *            For example, if this resource gives access to the Music-Directory and <code>path</code> is set to 
     *            <code>example/testFile.txt</code>, then <code>Music/example/testFile.txt</code> will be read).
     * @return Data of the selected file.
     * @throws IllegalAccessError
     *             Thrown, if app's privacy level is not set or the <code>path</code> parameters contains character for
     *             switching into a upper directory (typically <code>../</code>).
     * @throws RemoteException
     *             Thrown, if file is not readable (e.g. does not exist).
     */
    @Override
    public String read(String path) throws RemoteException {
        // Check if application is allowed to use this function
        if (!privacyLevelSet(Functions.READ)) {
            throw new IllegalAccessError("External file reading not allowed");
        }
        
        try {
            return Utils.readFileToString(getExternalDirectory(path));
        } catch (FileNotFoundException e) {
            Log.d("Cannot open file: " + path, e);
            throw new RemoteException();
        } catch (IOException e) {
            Log.d("Cannot read file", e);
            throw new RemoteException();
        } catch (IllegalArgumentException e) {
            Log.d(SWITCHING_EXCEPTION, e);
            throw new IllegalAccessError(SWITCHING_EXCEPTION);
        }
        
    }
    
    
    /**
     * Writes a given string into a file in the external directory
     * 
     * @param path
     *            Path to the file in the external directory the string should be written to.
     *            For example, if this resource gives access to the Music-Directory and <code>path</code> is set to 
     *            <code>example/testFile.txt</code>, then the string will be written to
     *            <code>Music/example/testFile.txt</code>).
     * @param data
     *            Date to write into the selected file.
     * @param append
     *            True, if data should be appended to the existing file data. Otherwise it's data will be overwritten.
     * @return True, if data was successfully written.
     * @throws IllegalAccessError
     *             Thrown, if app's privacy level is not set or the <code>path</code> parameters contains character for
     *             switching into a upper directory (typically <code>../</code>).
     */
    @Override
    public boolean write(String path, String data, boolean append) throws RemoteException {
        // Check if application is allowed to use this function
        if (!privacyLevelSet(Functions.WRITE)) {
            throw new IllegalAccessError("External file reading not allowed");
        }
        
        try {
            Utils.writeStringToFile(getExternalDirectory(path), data, append);
            return true;
        } catch (IOException e) {
            Log.d("Cannot write data to " + path);
            return false;
        } catch (IllegalArgumentException e) {
            Log.d(SWITCHING_EXCEPTION, e);
            throw new IllegalAccessError(SWITCHING_EXCEPTION);
        }
    }
    
    
    /**
     * Deletes a file or directory in the external directory. Directories can only be deleted if they do not have any
     * files.
     * 
     * @param path
     *            Path of the file or directory which should be deleted.
     *            For example, if this resource gives access to the Music-Directory and <code>path</code> is set to 
     *            <code>example/testDir</code>, then <code>Music/example/testDir</code> will be deleted).
     * @return True, if file or directory was deleted successfully.
     * @throws IllegalAccessError
     *             Thrown, if app's privacy level is not set or the <code>path</code> parameters contains character for
     *             switching into a upper directory (typically <code>../</code>).
     */
    @Override
    public boolean delete(String path) throws RemoteException {
        // Check if application is allowed to use this function
        if (!privacyLevelSet(Functions.DELETE)) {
            throw new IllegalAccessError("External file reading not allowed");
        }
        try {
            return getExternalDirectory(path).delete();
        } catch (IllegalArgumentException e) {
            Log.d(SWITCHING_EXCEPTION, e);
            throw new IllegalAccessError(SWITCHING_EXCEPTION);
        }
    }
    
    
    /**
     * Returns a list of all files and directories in a given external directory.
     * 
     * @param directory
     *            Path of the parent directory.
     *            For example, if this resource gives access to the Music-Directory and <code>path</code> is set to 
     *            <code>example/testDir</code>, then a list of all files and sub-directories in
     *            <code>Music/example/testDir</code> will be generated).
     * @return List of detailed file information data or null, if path points to a non existing directory or a file.
     * @throws IllegalAccessError
     *             Thrown, if the app's privacy level is not set or the <code>path</code> parameters contains character
     *             for switching into a upper directory (typically <code>../</code>).
     */
    @Override
    public List<FileDetails> list(String directory) throws RemoteException {
        // Check if application is allowed to use this function
        if (!privacyLevelSet(Functions.LIST)) {
            throw new IllegalAccessError("External file reading not allowed");
        }
        
        try {
            return Utils.getFileDetailsList(getExternalDirectory(directory));
        } catch (IllegalArgumentException e) {
            Log.d(SWITCHING_EXCEPTION, e);
            throw new IllegalAccessError(SWITCHING_EXCEPTION);
        }
    }
    
    
    /**
     * Creates all directories that are not existing
     * 
     * @see File#mkdirs()
     * @param path
     *            Directory path.
     *            For example, if this resource gives access to the Music-Directory and <code>path</code> is set to 
     *            <code>example/testDir</code>, then <code>Music/example/testFDir</code> will be created).
     * @return True, if directories where created successfully.
     * @throws IllegalAccessError
     *             Thrown, if the app's privacy level is not set or the <code>path</code> parameters contains character
     *             for switching into a upper directory (typically <code>../</code>).
     */
    @Override
    public boolean makeDirs(String path) throws RemoteException {
        // Check if application is allowed to use this function
        if (!privacyLevelSet(Functions.MAKE_DIRS)) {
            throw new IllegalAccessError("External file reading not allowed");
        }
        
        try {
            return getExternalDirectory(path).mkdirs();
        } catch (IllegalArgumentException e) {
            Log.d(SWITCHING_EXCEPTION, e);
            throw new IllegalAccessError(SWITCHING_EXCEPTION);
        }
    }
    
    
    /**
     * Returns the path to the external storage (&lt;Directory&gt;/&lt;subpath&gt;)
     * 
     * @param subpath
     *            Sub-path &lt;subpath&gt; in the external directory.
     * @return Computed path to the external storage.
     * @throws IllegalArgumentException
     *             Thrown, if the sub-path string contains characters for switching into an upper directory (typically
     *             "../").
     */
    private File getExternalDirectory(String subpath) throws IllegalArgumentException {
        // Prevent switching into an upper directory using "../"
        if (subpath.contains("..")) {
            throw new IllegalArgumentException(SWITCHING_EXCEPTION);
        }
        
        File root = Environment.getExternalStorageDirectory();
        File baseDir = null;
        
        switch (this.directory) {
            case BASE_DIR:
                baseDir = root;
                break;
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
     * Checks if a specific privacy-level is set for an application.
     * 
     * @param privacyLevelName
     *            The privacy-level to check.
     * @return True, if privacy-level is set for this application.
     */
    private boolean privacyLevelSet(Functions function) {
        String privacyLevelName = null;
        
        switch (function) {
        // Read function
            case READ:
                switch (this.directory) {
                    case BASE_DIR:
                        privacyLevelName = PrivacyLevels.EXTERNAL_BASE_DIR_READ;
                        break;
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
                switch (this.directory) {
                    case BASE_DIR:
                        privacyLevelName = PrivacyLevels.EXTERNAL_BASE_DIR_WRITE;
                        break;
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
                // List function
                switch (this.directory) {
                    case BASE_DIR:
                        privacyLevelName = PrivacyLevels.EXTERNAL_BASE_DIR_LIST;
                        break;
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
                // Delete function
                switch (this.directory) {
                    case BASE_DIR:
                        privacyLevelName = PrivacyLevels.EXTERNAL_BASE_DIR_DELETE;
                        break;
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
            
            case MAKE_DIRS:
                // Delete function
                switch (this.directory) {
                    case BASE_DIR:
                        privacyLevelName = PrivacyLevels.EXTERNAL_BASE_DIR_MAKE_DIRS;
                        break;
                    case MUSIC:
                        privacyLevelName = PrivacyLevels.EXTERNAL_MUSIC_MAKE_DIRS;
                        break;
                    case PODCASTS:
                        privacyLevelName = PrivacyLevels.EXTERNAL_PODCASTS_MAKE_DIRS;
                        break;
                    case RINGTONES:
                        privacyLevelName = PrivacyLevels.EXTERNAL_RINGTONES_MAKE_DIRS;
                        break;
                    case ALARMS:
                        privacyLevelName = PrivacyLevels.EXTERNAL_ALARMS_MAKE_DIRS;
                        break;
                    case NOTIFICATIONS:
                        privacyLevelName = PrivacyLevels.EXTERNAL_NOTIFICATIONS_MAKE_DIRS;
                        break;
                    case PICTURES:
                        privacyLevelName = PrivacyLevels.EXTERNAL_PICTURES_MAKE_DIRS;
                        break;
                    case MOVIES:
                        privacyLevelName = PrivacyLevels.EXTERNAL_MOVIES_MAKE_DIRS;
                        break;
                    case DOWNLOAD:
                        privacyLevelName = PrivacyLevels.EXTERNAL_DOWNLOAD_MAKE_DIRS;
                        break;
                }
                break;
        }
        
        return PrivacyLevels.privacyLevelSet(privacyLevelName, this.app, this.resource);
    }
}
