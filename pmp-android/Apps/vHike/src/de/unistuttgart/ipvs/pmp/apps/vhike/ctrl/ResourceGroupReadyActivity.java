/**
 * 
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.ctrl;

import android.app.Activity;
import android.os.IInterface;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.aidl.INotification;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * @author Dang Huynh
 * 
 */
public abstract class ResourceGroupReadyActivity extends Activity {
    
    protected static IAbsoluteLocation rgLocation;
    protected static IvHikeWebservice rgvHike;
    protected static INotification rgNotification;
    
    
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
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
        }
    }
    
    
    protected IInterface requestResourceGroup(ResourceGroupReadyActivity activity, int resourceGroupId) {
        switch (resourceGroupId) {
            case Constants.RG_LOCATION:
                rgLocation = (IAbsoluteLocation) vHikeService.getInstance().requestResourceGroup(activity,
                        resourceGroupId);
                return rgLocation;
            case Constants.RG_NOTIFICATION:
                rgNotification = (INotification) vHikeService.getInstance().requestResourceGroup(activity,
                        resourceGroupId);
                return rgNotification;
            case Constants.RG_VHIKE_WEBSERVICE:
                rgvHike = (IvHikeWebservice) vHikeService.getInstance().requestResourceGroup(activity, resourceGroupId);
                return rgvHike;
        }
        return null;
    }
    
    
    protected IAbsoluteLocation getLocationRG(ResourceGroupReadyActivity activity) {
        rgLocation = (IAbsoluteLocation) vHikeService.getInstance().requestResourceGroup(activity,
                Constants.RG_LOCATION);
        return rgLocation;
    }
    
    
    protected INotification getNotificationRG(ResourceGroupReadyActivity activity) {
        rgNotification = (INotification) vHikeService.getInstance().requestResourceGroup(activity,
                Constants.RG_NOTIFICATION);
        return rgNotification;
    }
    
    
    protected IvHikeWebservice getvHikeRG(ResourceGroupReadyActivity activity) {
        rgvHike = (IvHikeWebservice) vHikeService.getInstance().requestResourceGroup(activity, Constants.RG_VHIKE_WEBSERVICE);
        return rgvHike;
    }
    
    
    protected void reloadResourceGroup(ResourceGroupReadyActivity activity, int resourceGroupId) {
        vHikeService.getInstance().loadResourceGroup(activity, resourceGroupId);
    }
}
