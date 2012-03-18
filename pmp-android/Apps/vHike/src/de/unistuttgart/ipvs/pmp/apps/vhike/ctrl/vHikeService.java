/**
 * This service provides methods for checking Service Features and using IPC
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.api.IPMP;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

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
    private static final PMPResourceIdentifier RGVHikeID = PMPResourceIdentifier.make(
            "de.unistuttgart.ipvs.pmp.resourcegroups.vhikewebservice", "enable");
    private static final PMPResourceIdentifier[] resourceGroups = { RGLocationID };
    
    
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
        app = de.unistuttgart.ipvs.pmp.apps.vhike.Application.getApp();
    }
    
    private static IPMP pmp;
    private Application app = null;
    
    
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
     * 
     * @return an instance of PMP
     */
    private IPMP getPMP() {
        try {
            if (pmp == null) {
                app = getApplication();
                Log.v(TAG, app.getPackageName());
                if (app == null) {
                    throw new NullPointerException("Application is null. Please start the vHikeService first");
                }
                pmp = PMP.get(app);
            }
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
    public boolean areServiceFeatureEnabled(int[] serviceFeatureIDs) {
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
    public void requestServiceFeature(Activity activity, int serviceFeatureID) {
        Log.v(TAG, app.getPackageName());
        Log.v(TAG, activity.getLocalClassName());
        PMP.get(app).requestServiceFeatures(activity, serviceFeatures[serviceFeatureID]);
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
        PMP.get(app).requestServiceFeatures(activity, l);
    }
    
    private IBinder binder = null;
    private IAbsoluteLocation loc;
    private IvHikeWebservice ws;
    private CountDownLatch latch;
    
    private void resourceReady() {
        System.out.println("resource ready");
        latch.countDown();
    }
    
    private IBinder getResourceGroup(final PMPResourceIdentifier id) {
        try {
            binder = null;
            //            PMP.get(app) = PMP.get(getApplication());
            if (PMP.get(app).isResourceCached(id)) {
                binder = PMP.get(app).getResourceFromCache(id);
            } else {
                PMP.get(app).getResource(id, new PMPRequestResourceHandler() {
                    @Override
                    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                        super.onReceiveResource(resource, binder);
                        Log.i(TAG, "on receiving");
                        resourceReady();
                        
                    }
                });
                latch = new CountDownLatch(1);
                ResourceGroupBinder rgb = new ResourceGroupBinder(id, latch);
                new Thread(rgb).start();
                Log.i(TAG, "waiting...");
                latch.await(20, TimeUnit.SECONDS);
                Log.v(TAG, "returned");
                //                synchronized (Thread.currentThread()) {
                //                    wait(10000);
                //                }
//                binder = rgb.getBinder();
                binder = PMP.get(app).getResourceFromCache(id);
            }
        } catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }
        return binder;
    }
    
    
    //    private IBinder getRG(PMPResourceIdentifier id) {
    //        try {
    //            PMP.get(app).getResource(id);
    //            int wait = 100;
    //            while (!PMP.get(app).isResourceCached(id) && wait > 0) {
    //                Thread.currentThread().wait(100);
    //                wait--;
    //            }
    //            if (PMP.get(app).isResourceCached(id)) {
    //                return PMP.get(app).getResourceFromCache(id);
    //            }
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }
    
    public IAbsoluteLocation getLocationResourceGroup() {
        if (loc == null) {
            loc = IAbsoluteLocation.Stub.asInterface(getResourceGroup(RGLocationID));
        }
        return loc;
    }
    
    
    public IvHikeWebservice getVHikeWebServiceResourceGroup() {
        if (ws == null) {
            ws = IvHikeWebservice.Stub.asInterface(getResourceGroup(RGVHikeID));
        }
        return ws;
    }
    
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private class ResourceGroupBinder implements Runnable {
        
        PMPResourceIdentifier id;
        CountDownLatch latch;
        IBinder binder;
        
        
        public ResourceGroupBinder(PMPResourceIdentifier id, CountDownLatch latch) {
            this.id = id;
            this.latch = latch;
        }
        
        
        public IBinder getBinder() {
            return binder;
        }
        
        
        @Override
        public void run() {
            try {
                Log.i(TAG, "getting");
                Log.i(TAG, app.getPackageName());
                Log.i(TAG, id.getResourceGroup());
                int wait = 100;
                while (!PMP.get(app).isResourceCached(id) && wait > 0) {
                    System.out.println("sleeping");
                    Thread.sleep(100);
                    System.out.println("wake");
                    wait--;
                }
                vHikeService.this.resourceReady();
                
                //                PMP.get(app).getResource(id, new PMPRequestResourceHandler() {
                //
                //                    @Override
                //                    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                //                        Log.d(TAG, "received");
                //                        ResourceGroupBinder.this.binder = PMP.get(app).getResourceFromCache(id);
                //                        ResourceGroupBinder.this.latch.countDown();
                //                    }
                //
                //
                //                    @Override
                //                    public void onBindingFailed() {
                //                        Log.v(TAG, "failed");
                //                        Toast.makeText(getApplicationContext(), "Binding FAILED", Toast.LENGTH_LONG);
                //                        ResourceGroupBinder.this.latch.countDown();
                //                    }
                //                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
