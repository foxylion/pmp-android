/**
 * This service provides methods for checking Service Features and using IPC
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.ctrl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.api.IPMP;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;

/**
 * @author Dang Huynh
 * 
 */
public class vHikeService extends Service {
    
    private static vHikeService instance;
    private static final String TAG = "vHikeService";
    private static final String[] serviceFeatures = {
            "useAbsoluteLocation", "hideExactLocation", "hideContactInfo",
            "contactPremium", "notification", "vhikeWebService" };
    private static final PMPResourceIdentifier RGLocationID = PMPResourceIdentifier.make(
            "de.unistuttgart.ipvs.pmp.resourcegroups.location", "absoluteLocationResource");
    private static final PMPResourceIdentifier[] resourceGroups = {RGLocationID};
    
    
    /**
     * 
     * @return Returns an instance of vHikeService
     */
    public static vHikeService getInstance() {
        if (instance == null)
            instance = new vHikeService();
        return instance;
    }
    
    
    /**
     * Private constructor
     */
    private vHikeService() {
        getPMP();
    }
    
    private static IPMP pmp;
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        getPMP();
        Log.v(TAG, "onstartcommand");
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
        Log.v(TAG, "onstartcommand");
    }
    
    
    /**
     * 
     * @return an instance of PMP
     */
    private IPMP getPMP() {
        try {
            if (pmp == null)
                pmp = PMP.get(getApplication());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pmp;
    }
    
    
    /**
     * Check whether a service feature is enabled
     * 
     * @param serviceFeatureID
     *            See {@link Constants}.SF_
     * @return True if the service feature is enabled, false otherwise
     */
    public boolean isServiceFeatureEnabled(int serviceFeatureID) {
        try {
            return pmp.areServiceFeaturesEnabled(serviceFeatures[serviceFeatureID]);
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
    public boolean areServiceFeatureEnabled(int[] serviceFeatureIDs) {
        try {
            List<String> l = new ArrayList<String>(serviceFeatureIDs.length);
            for (Integer i : serviceFeatureIDs) {
                l.add(serviceFeatures[i]);
            }
            return pmp.areServiceFeaturesEnabled(l);
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
    public void requestServiceFeature(Activity activity, int serviceFeatureID) {
        pmp.requestServiceFeatures(activity, serviceFeatures[serviceFeatureID]);
    }
    
    
    /**
     * Show the activity to request a set of service features
     * 
     * @param activity
     * @param serviceFeatureIDs
     */
    public void requestServiceFeatures(Activity activity, int[] serviceFeatureIDs) {
        List<String> l = new ArrayList<String>(serviceFeatureIDs.length);
        for (int i : serviceFeatureIDs) {
            l.add(serviceFeatures[i]);
        }
        pmp.requestServiceFeatures(activity, l);
    }
    
//    private IBinder binder;
//    private IAbsoluteLocation loc;
    
    
//    public IAbsoluteLocation getLocationResourceGroup() {
//        if (loc == null) {
//            try {
//                binder = PMP.get().getResourceFromCache();
//            } catch (RemoteException re) {
//                // TODO: handle exception
//            }
//        }
//        return loc;
//    }
    
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
}
