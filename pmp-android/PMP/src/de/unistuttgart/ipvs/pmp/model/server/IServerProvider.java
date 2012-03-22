package de.unistuttgart.ipvs.pmp.model.server;

import java.io.File;
import java.util.Date;

import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;

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
     * @return an array of {@link LocalizedResourceGroup} for all resource groups fitting that pattern, or null, if an
     *         error occurred
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
     * Stores a {@link PresetSet} on the server.
     * 
     * @param presetSet
     *            the preset set to store
     * @return the name of the created preset set, or null if an error occurred
     */
    public String storePresetSet(IPresetSet presetSet);
    
    
    /**
     * Loads a {@link PresetSet} from the server.
     * 
     * @param name
     *            the name of the preset set to load
     * @return the loaded preset set, or null if an error occurred
     */
    public IPresetSet loadPresetSet(String name);
    
    
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
    
    
    /**
     * The time the last find RG request was cached on.
     * 
     * @param searchPattern
     *            the string for which shall be searched
     * @return the Date when the RG request for searchPattern was cached
     */
    public Date getFindResourceGroupsCacheDate(String searchPattern);
}
