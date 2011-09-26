package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.PrivacyLevels;

/**
 * This class handled the access to all file stored on the device
 * 
 * @author Patrick Strobel
 * @version 0.2.0
 * 
 */
public class GenericFileAccess extends IFileAccess.Stub {
    
    private String app;
    private GenericFileAccessResource resource;
    
    
    /**
     * Creates a new instance
     * 
     * @param app
     *            Identifier of the app that is using this access handler
     * @param resource
     *            Resource this access handler object belongs to
     */
    protected GenericFileAccess(String app, GenericFileAccessResource resource) {
        this.app = app;
        this.resource = resource;
    }
    
    
    /**
     * Reads a file into a string
     * 
     * @param path
     *            File to read
     * @return Read data of the selected file
     * @throws IllegalAccessError
     *             Thrown, if app's privacy-level is not set
     * @throws RemoteException
     *             Thrown, if file is not readable
     */
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
    
    
    /**
     * Writes data into a file
     * 
     * @param path
     *            File to which this data should be written
     * @param data
     *            Data to write
     * @param append
     *            True, if data should be appended to the current file's data
     * @return True, if data was written
     * @throws IllegalAccessError
     *             Thrown, if app's privacy levels is not set
     */
    @Override
    public boolean write(String path, String data, boolean append) throws RemoteException {
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
    
    
    /**
     * Deletes a file or directory. Directories have to be empty before they can be deleted.
     * 
     * @param path
     *            File or directory to delete
     * @return True, if deleting was successful
     * @throws IllegalAccessError
     *             Throw, if the app's privacy level is not set
     */
    @Override
    public boolean delete(String path) throws RemoteException {
        // Check if application is allowed to use this function
        if (!privacyLevelSet(PrivacyLevels.GENERIC_DELETE)) {
            throw new IllegalAccessError("Generic file deleting not allowed");
        }
        
        return new File(path).delete();
    }
    
    
    /**
     * Returns a list of all files and directories in a given directory
     * 
     * @param directory
     *            Path of the parent directory
     * @return List of detailed file information data or null, if path points to a non existing directory or a file
     * @throws IllegalAccessError
     *             Thrown, if the app's privacy level is not set
     */
    @Override
    public List<FileDetails> list(String directory) throws RemoteException {
        // Check if application is allowed to use this function
        if (!privacyLevelSet(PrivacyLevels.GENERIC_LIST)) {
            throw new IllegalAccessError("Generic file listing not allowed");
        }
        
        return Utils.getFileDetailsList(new File(directory));
    }
    
    
    /**
     * Creates all directories that are not existing
     * 
     * @see File#mkdirs()
     * @param path
     *            Directory path
     * @return True, if directories where created successfully
     * @throws IllegalAccessError
     *             Thrown, if the app's privacy level is not set
     */
    @Override
    public boolean makeDirs(String path) throws RemoteException {
        // Check if application is allowed to use this function
        if (!privacyLevelSet(PrivacyLevels.GENERIC_MAKE_DIRS)) {
            throw new IllegalAccessError("Generic file listing not allowed");
        }
        
        return new File(path).mkdirs();
    }
    
    
    /**
     * Checks if a specific privacy-level is set for an application
     * 
     * @param privacyLevelName
     *            The privacy-level to check
     * @return True, if privacy-level is set for this application
     */
    private boolean privacyLevelSet(String privacyLevelName) {
        return PrivacyLevels.privacyLevelSet(privacyLevelName, this.app, this.resource);
    }
    
}
