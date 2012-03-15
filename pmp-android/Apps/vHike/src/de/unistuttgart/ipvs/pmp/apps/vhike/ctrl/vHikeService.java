/**
 * This service provides methods for checking Service Features and using IPC
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.ctrl;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.api.IPMP;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;

/**
 * @author Dang Huynh
 * 
 */
public class vHikeService extends Service {
    
    private static vHikeService instance;
    private static String TAG = "vHikeService";
    private static String[] serviceFeatures = {
            "useAbsoluteLocation", "hideExactLocation", "hideContactInfo",
            "contactPremium", "notification", "vhikeWebService" };
    
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
    public boolean areServiceFeatureEnabled(ArrayList<Integer> serviceFeatureIDs) {
        try {
            List<String> l = new ArrayList<String>(serviceFeatureIDs.size());
            for (Integer i : serviceFeatureIDs) {
                l.add(serviceFeatures[i]);
            }
            return pmp.areServiceFeaturesEnabled(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}