/**
 * This service provides methods for checking Service Features and using IPC
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.ctrl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.IResourceGroupReady;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.aidl.INotification;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * @author Dang Huynh
 * 
 */
public class vHikeService extends Service {
    
    private static vHikeService instance;
    private static final String TAG = "vHikeService";
    private static final String[] serviceFeatures = { "useAbsoluteLocation", "hideExactLocation", "anonymousProfile",
            "contactPremium", "notification", "vhikeWebService" };
    private static final PMPResourceIdentifier RGLocationID = PMPResourceIdentifier.make(
            "de.unistuttgart.ipvs.pmp.resourcegroups.location", "absoluteLocationResource");
    private static final PMPResourceIdentifier RGVHikeID = PMPResourceIdentifier.make(
            "de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS", "vHikeWebserviceResource");
    private static final PMPResourceIdentifier RGNotificationID = PMPResourceIdentifier.make(
            "de.unistuttgart.ipvs.pmp.resourcegroups.notification", "NotificationResource");
    private static final PMPResourceIdentifier RGContactID = PMPResourceIdentifier.make(
            "de.unistuttgart.ipvs.pmp.resourcegroups.contact", "contactResource");
    private static final PMPResourceIdentifier RGBluetoothID = PMPResourceIdentifier.make(
            "de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth", "bluetoothResource");
    private static final PMPResourceIdentifier[] resourceGroupIDs = { RGLocationID, RGVHikeID, RGNotificationID,
            RGContactID, RGBluetoothID };
    
    
    /**
     * 
     * @return Returns an instance of vHikeService
     */
    public static vHikeService getInstance() {
        if (instance == null) {
            instance = new vHikeService();
        }
        return instance;
    }
    
    
    /**
     * Private constructor
     */
    public vHikeService() {
    }
    
    private static Application app = de.unistuttgart.ipvs.pmp.apps.vhike.Application.getApp();;
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }
    
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.v(TAG, "onstartcommand");
        return START_STICKY;
    }
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }
    
    
    /**
     * Check whether a service feature is enabled
     * 
     * @param serviceFeatureID
     *            See {@link Constants}.SF_
     * @return True if the service feature is enabled, false otherwise
     */
    public static boolean isServiceFeatureEnabled(int serviceFeatureID) {
        try {
            return PMP.get(app).areServiceFeaturesEnabled(serviceFeatures[serviceFeatureID]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * Check whether a combination of service features is enabled
     * 
     * @param serviceFeatureIDs
     *            See {@link Constants}.SF_
     * @return True if the service features are enabled, false otherwise
     */
    public static boolean areServiceFeatureEnabled(int[] serviceFeatureIDs) {
        try {
            List<String> l = new ArrayList<String>(serviceFeatureIDs.length);
            for (Integer i : serviceFeatureIDs) {
                l.add(serviceFeatures[i]);
            }
            return PMP.get(app).areServiceFeaturesEnabled(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * Show the activity to request a service feature
     * 
     * @param activity
     *            (this)
     * @param serviceFeatureID
     *            See {@link Constants}.SF_
     */
    public static void requestServiceFeature(Activity activity, int serviceFeatureID) {
        PMP.get(app).requestServiceFeatures(activity, serviceFeatures[serviceFeatureID]);
    }
    
    /**
     * Show the activity to request a set of service features
     * 
     * @param activity
     * @param serviceFeatureIDs
     */
    //    public void requestServiceFeatures(Activity activity, int[] serviceFeatureIDs) {
    //        List<String> l = new ArrayList<String>(serviceFeatureIDs.length);
    //        for (int i : serviceFeatureIDs) {
    //            l.add(serviceFeatures[i]);
    //        }
    //        PMP.get(app).requestServiceFeatures(activity, l);
    //    }
    
    private IBinder binder;
    private IAbsoluteLocation loc;
    private IvHikeWebservice ws;
    private INotification noti;
    private IContact con;
    private IBluetooth bt;
    
    
    public IInterface requestResourceGroup(Activity activity, int resourceGroupId) {
        switch (resourceGroupId) {
            case Constants.RG_LOCATION:
                if (this.loc == null) {
                    reloadResourceGroup(activity, resourceGroupId);
                } else {
                    return this.loc;
                }
                break;
            case Constants.RG_NOTIFICATION:
                if (this.noti == null) {
                    reloadResourceGroup(activity, resourceGroupId);
                } else {
                    return this.noti;
                }
                break;
            case Constants.RG_VHIKE_WEBSERVICE:
                if (this.ws == null) {
                    reloadResourceGroup(activity, resourceGroupId);
                } else {
                    return this.ws;
                }
                break;
            case Constants.RG_CONTACT:
                if (this.con == null) {
                    reloadResourceGroup(activity, resourceGroupId);
                } else {
                    return this.con;
                }
                break;
            case Constants.RG_BLUETOOTH:
                if (this.bt == null) {
                    reloadResourceGroup(activity, resourceGroupId);
                } else {
                    return this.bt;
                }
                break;
        }
        return null;
    }
    
    
    public void loadResourceGroup(Activity activity, int resourceGroupId) {
        switch (resourceGroupId) {
            case Constants.RG_LOCATION:
            case Constants.RG_NOTIFICATION:
            case Constants.RG_VHIKE_WEBSERVICE:
            case Constants.RG_CONTACT:
            case Constants.RG_BLUETOOTH:
                reloadResourceGroup(activity, resourceGroupId);
                break;
        }
    }
    
    
    private void getResourceFromCache(Activity activity, int resourceGroupId, IBinder binder) {
        IResourceGroupReady act = (IResourceGroupReady) activity;
        if (binder == null) {
            this.binder = PMP.get(app).getResourceFromCache(resourceGroupIDs[resourceGroupId]);
        } else {
            this.binder = binder;
        }
        switch (resourceGroupId) {
            case Constants.RG_LOCATION:
                this.loc = IAbsoluteLocation.Stub.asInterface(this.binder);
                act.onResourceGroupReady(this.loc, resourceGroupId);
                break;
            case Constants.RG_VHIKE_WEBSERVICE:
                this.ws = IvHikeWebservice.Stub.asInterface(this.binder);
                act.onResourceGroupReady(this.ws, resourceGroupId);
                break;
            case Constants.RG_NOTIFICATION:
                this.noti = INotification.Stub.asInterface(this.binder);
                act.onResourceGroupReady(this.noti, resourceGroupId);
                break;
            case Constants.RG_CONTACT:
                this.con = IContact.Stub.asInterface(this.binder);
                act.onResourceGroupReady(this.con, resourceGroupId);
                break;
            case Constants.RG_BLUETOOTH:
                this.bt = IBluetooth.Stub.asInterface(this.binder);
                act.onResourceGroupReady(this.bt, resourceGroupId);
                break;
        }
    }
    
    
    private void reloadResourceGroup(final Activity activity, final int resourceGroupId) {
        if (PMP.get(app).isResourceCached(resourceGroupIDs[resourceGroupId])) {
            getResourceFromCache(activity, resourceGroupId, null);
        } else {
            PMP.get(app).getResource(resourceGroupIDs[resourceGroupId], new PMPRequestResourceHandler() {
                
                @Override
                public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
                    super.onReceiveResource(resource, binder, isMocked);
                    Log.v(TAG, "onReceiveResource");
                    getResourceFromCache(activity, resourceGroupId, binder);
                }
            });
        }
    }
    
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
}
