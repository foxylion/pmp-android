package de.unistuttgart.ipvs.pmp.model.server;

import java.io.File;

import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;

/**
 * Provider for managing all communication to the RG package server of PMP.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IServerProvider {
    
    /**
     * Finds a list of {@link LocalizedResourceGroup}s for that search string.
     * 
     * @param searchPattern
     *            the string for which shall be searched
     * @return an array of {@link LocalizedResourceGroup} for all resource groups fitting that pattern
     */
    public LocalizedResourceGroup[] findResourceGroups(String searchPattern);
    
    
    /**
     * Downloads the resource group identified by rgPackage and stores it in a temporary file.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @return the file where the resource group is contained or null, if an error occurred
     */
    public File downloadResourceGroup(String rgPackage);
    
    
    /**
     * Allows to set a callback object which can receive updates on the progress during the downloads.
     * 
     * @param callback
     *            the new callback to call or null for no callback
     */
    public void setCallback(IServerDownloadCallback callback);
    
    
    /**
     * Cleans the entire download cache.
     */
    public void cleanCache();
}
