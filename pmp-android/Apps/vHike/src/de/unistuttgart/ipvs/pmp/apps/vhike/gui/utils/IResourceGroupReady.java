package de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils;

import android.os.IInterface;

/**
 * This interface provides easy access to all the needed resource groups
 * 
 * @author Dang Huynh
 * 
 */
public interface IResourceGroupReady {
    /**
     * Callback function when your requested resource group is ready. This method set the local resource group variables
     * to real RG-object by default. Override this method and add your own logic.
     * 
     * Remember to catch Exceptions.
     * 
     * @param resourceGroup
     *            The returned resource group
     * @param resourceGroupId
     *            The ID of that resource group
     */
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId);
}
