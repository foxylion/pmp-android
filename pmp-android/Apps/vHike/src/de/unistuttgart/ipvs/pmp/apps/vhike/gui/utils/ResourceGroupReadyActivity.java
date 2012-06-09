package de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils;

import android.app.Activity;
import android.os.IInterface;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.aidl.INotification;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * This class provides easy access to all the needed resource groups
 * 
 * @author Dang Huynh
 * 
 */
public class ResourceGroupReadyActivity extends Activity implements IResourceGroupReady {
    
    protected static IAbsoluteLocation rgLocation;
    protected static IvHikeWebservice rgvHike;
    protected static INotification rgNotification;
    protected static IContact rgContact;
    protected static IBluetooth rgBluetooth;
    
    
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
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) {
        switch (resourceGroupId) {
            case Constants.RG_LOCATION:
                rgLocation = (IAbsoluteLocation) resourceGroup;
                break;
            case Constants.RG_NOTIFICATION:
                rgNotification = (INotification) resourceGroup;
                break;
            case Constants.RG_VHIKE_WEBSERVICE:
                rgvHike = (IvHikeWebservice) resourceGroup;
                break;
            case Constants.RG_CONTACT:
                rgContact = (IContact) resourceGroup;
                break;
            case Constants.RG_BLUETOOTH:
                rgBluetooth = (IBluetooth) resourceGroup;
                break;
        }
    }
    
    
    /**
     * Returns a resource group if it's available or null if the resource group is being cached. If you get a
     * null variable from this method, your activity will be informed by calling the function onResourceGroupReady.
     * Override that method and implement your own logic.
     * 
     * Remember to check permission and catch Exceptions.
     * 
     * @param activity
     *            Your ResourceGroupReadyActivity
     * @param resourceGroupId
     *            The resource group's ID. See {@link Constants}.RG_***
     * @return null or a IInterface
     */
    protected IInterface requestResourceGroup(ResourceGroupReadyActivity activity, int resourceGroupId) {
        switch (resourceGroupId) {
            case Constants.RG_LOCATION:
                if (rgLocation == null) {
                    rgLocation = (IAbsoluteLocation) vHikeService.getInstance().requestResourceGroup(
                            activity,
                            resourceGroupId);
                }
                return rgLocation;
            case Constants.RG_NOTIFICATION:
                if (rgNotification == null) {
                    rgNotification = (INotification) vHikeService.getInstance().requestResourceGroup(
                            activity,
                            resourceGroupId);
                }
                return rgNotification;
            case Constants.RG_VHIKE_WEBSERVICE:
                if (rgvHike == null) {
                    rgvHike = (IvHikeWebservice) vHikeService.getInstance().requestResourceGroup(activity,
                            resourceGroupId);
                }
                return rgvHike;
            case Constants.RG_CONTACT:
                if (rgContact == null) {
                    rgContact = (IContact) vHikeService.getInstance().requestResourceGroup(activity,
                            resourceGroupId);
                }
                return rgContact;
            case Constants.RG_BLUETOOTH:
                if (rgBluetooth == null) {
                    rgBluetooth = (IBluetooth) vHikeService.getInstance().requestResourceGroup(activity,
                            resourceGroupId);
                }
                return rgBluetooth;
        }
        return null;
    }
    
    
    /**
     * Returns an Absolute Location resource group if it's available or null if the resource group is being cached. If
     * you get a null variable from this method, your activity will be informed by calling the function
     * onResourceGroupReady. Override that method and implement your own logic.
     * 
     * Remember to check permission and catch Exceptions.
     * 
     * @param activity
     *            Your ResourceGroupReadyActivity
     * @return Null or an {@link IAbsoluteLocation} object
     */
    protected IAbsoluteLocation getLocationRG(ResourceGroupReadyActivity activity) {
        if (rgLocation == null) {
            rgLocation = (IAbsoluteLocation) vHikeService.getInstance().requestResourceGroup(activity,
                    Constants.RG_LOCATION);
        }
        return rgLocation;
    }
    
    
    /**
     * Returns a Notification resource group if it's available or null if the resource group is being cached. If
     * you get a null variable from this method, your activity will be informed by calling the function
     * onResourceGroupReady. Override that method and implement your own logic.
     * 
     * Remember to check permission and catch Exceptions.
     * 
     * @param activity
     *            Your ResourceGroupReadyActivity
     * @return Null or an {@link INotification} object
     */
    protected INotification getNotificationRG(ResourceGroupReadyActivity activity) {
        if (rgNotification == null) {
            rgNotification = (INotification) vHikeService.getInstance().requestResourceGroup(activity,
                    Constants.RG_NOTIFICATION);
        }
        return rgNotification;
    }
    
    
    /**
     * Returns a vHike Web Service resource group if it's available or null if the resource group is being cached. If
     * you get a null variable from this method, your activity will be informed by calling the function
     * onResourceGroupReady. Override that method and implement your own logic.
     * 
     * Remember to check permission and catch Exceptions.
     * 
     * @param activity
     *            Your ResourceGroupReadyActivity
     * @return Null or an {@link IvHikeWebservice} object
     */
    protected IvHikeWebservice getvHikeRG(ResourceGroupReadyActivity activity) {
        if (rgvHike == null) {
            rgvHike = (IvHikeWebservice) vHikeService.getInstance().requestResourceGroup(activity,
                    Constants.RG_VHIKE_WEBSERVICE);
        }
        return rgvHike;
    }
    
    
    protected IContact getContactRG(ResourceGroupReadyActivity activity) {
        if (rgContact == null) {
            rgContact = (IContact) vHikeService.getInstance().requestResourceGroup(activity,
                    Constants.RG_CONTACT);
        }
        return rgContact;
    }
    
    
    protected IBluetooth getBluetoothRG(ResourceGroupReadyActivity activity) {
        if (rgBluetooth == null) {
            rgBluetooth = (IBluetooth) vHikeService.getInstance()
                    .requestResourceGroup(activity, Constants.RG_BLUETOOTH);
        }
        return rgBluetooth;
    }
    
    
    /**
     * Reload a resource group even when it's cached. This method should be called when your resource group returns a
     * strange error.
     * 
     * Remember to check permission and catch Exceptions.
     * 
     * @param activity
     *            Your ResourceGroupReadyActivity
     */
    protected void reloadResourceGroup(ResourceGroupReadyActivity activity, int resourceGroupId) {
        vHikeService.getInstance().loadResourceGroup(activity, resourceGroupId);
    }
}
