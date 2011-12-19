package de.unistuttgart.ipvs.pmp.model.server;

import java.io.File;

import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

/**
 * Provider for managing all communication to the RG package server of PMP.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IServerProvider {
    
    /**
     * Finds a list of {@link IResourceGroup}s for that search string.
     * 
     * @param searchString
     *            the string for which shall be searched
     * @return an array of RGIS for all resource groups fitting that pattern
     */
    public RgInformationSet[] findResourceGroups(String searchString);
    
    
    /**
     * Downloads the resource group identified by rgPackage and stores it in a temporary file.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @return the file where the resource group is contained.
     */
    public File downloadResourceGroup(String rgPackage);
    
    
    /**
     * Allows to set a callback object which can receive updates on the progress during the downloads.
     * 
     * @param callback
     *            the new callback to call or null for no callback
     */
    public void setCallback(IServerDownloadCallback callback);
}
