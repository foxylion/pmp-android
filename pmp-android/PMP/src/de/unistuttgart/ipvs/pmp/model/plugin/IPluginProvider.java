package de.unistuttgart.ipvs.pmp.model.plugin;

import java.io.InputStream;

import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Provider for managing all {@link ResourceGroup} plugins in PMP.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IPluginProvider {
    
    /**
     * Injects a random apk from an {@link InputStream}. Does not install it.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @param input
     *            the input apk file
     */
    public abstract void injectFile(String rgPackage, InputStream input);
    
    
    /**
     * Installs a specific resource group.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @throws InvalidPluginException
     *             if the supplied plugin is somehow corrupt
     */
    public abstract void install(String rgPackage) throws InvalidPluginException;
    
    
    /**
     * Uninstalls, i.e. removes all data associated with the identified resource group.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     */
    public abstract void uninstall(String rgPackage);
    
    
    /**
     * Looks for the object that is running for this resource group.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @return the one and only instance of the identified resource group in PMP
     * @throws InvalidPluginException
     *             if the supplied plugin is somehow corrupt
     */
    public abstract ResourceGroup getResourceGroupObject(String rgPackage) throws InvalidPluginException;
    
    
    /**
     * Looks for the cached RGIS of the resource group.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @return the XML stream for the specified resource group or null if it wasn't found which should not happen
     * @throws InvalidPluginException
     *             if the supplied plugin is somehow corrupt
     */
    public abstract RGIS getRGIS(String rgPackage) throws InvalidPluginException;
    
    
    /**
     * Looks for the cached icon of terhe resource group.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @return the icon for the specified resource group or null if it wasn't found which should not happen
     * @throws InvalidPluginException
     *             if the supplied plugin is somehow corrupt
     */
    public abstract Drawable getIcon(String rgPackage) throws InvalidPluginException;
    
    
    /**
     * Looks for the cached revision of the resource group.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @return the revision for the specified resource group
     * @throws InvalidPluginException
     *             if the supplied plugin is somehow corrupt
     */
    public abstract long getRevision(String rgPackage) throws InvalidPluginException;
    
}
