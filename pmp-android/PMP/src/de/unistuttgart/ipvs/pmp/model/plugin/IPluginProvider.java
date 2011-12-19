package de.unistuttgart.ipvs.pmp.model.plugin;

import java.io.InputStream;

import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

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
     * @return true, if and only if the operation succeeded
     */
    public abstract boolean install(String rgPackage);
    
    
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
     */
    public abstract ResourceGroup getResourceGroupObject(String rgPackage);
    
    
    /**
     * Looks for the cached RGIS of the resource group.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @return the XML stream for the specified resource group or null if it wasn't found which should not happen
     */
    public abstract RgInformationSet getRGIS(String rgPackage);
    
    
    /**
     * Looks for the cached icon of the resource group.
     * 
     * @param rgPackage
     *            the main package of the resource group's apk
     * @return the icon for the specified resource group or null if it wasn't found which should not happen
     */
    public abstract Drawable getIcon(String rgPackage);
    
}