package de.unistuttgart.ipvs.pmp.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import android.app.Application;
import android.os.Bundle;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestServiceFeaturesHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPServiceFeatureUpdateHandler;
import de.unistuttgart.ipvs.pmp.api.ipc.IPCScheduler;

/**
 * The main PMP API implementing all the calls.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMP implements IPMP {
    
    /**
     * The static instance of the API
     */
    private static PMP instance = null;
    
    /**
     * The {@link Application} for which this API shall perform operations.
     */
    private Application application;
    
    /**
     * The {@link IPCScheduler} used to communicate with PMP.
     */
    public IPCScheduler scheduler;
    
    /**
     * The cache of Service Feature states.
     */
    private ConcurrentMap<String, Boolean> sfs;
    
    /**
     * The cache of Resource {@link IBinder}s.
     */
    private ConcurrentMap<PMPResourceIdentifier, IBinder> res;
    
    
    /**
     * Gets the API for an application.
     * 
     * @param application
     *            the {@link Application} for which the API shall perform operations
     * @return the {@link IPMP} API for the application
     */
    public static IPMP get(Application application) {
        if (instance == null) {
            instance = new PMP();
        }
        instance.setApplication(application);
        return instance;
    }
    
    
    /**
     * Gets the API, if an API for an application was previously requested. If not, consider calling
     * {@link PMP#get(Application))} before.
     * 
     * @return the {@link IPMP} API for the last application supplied
     * @throws IllegalAccessError
     *             if this method is called without ever specifying an {@link Application} via
     *             {@link PMP#get(Application)}.
     */
    public static IPMP get() {
        if (instance == null) {
            throw new IllegalAccessError("Tried to fetch an API without ever specifying an Application.");
        } else {
            return instance;
        }
    }
    
    
    private void setApplication(Application application) {
        this.application = application;
    }
    
    
    /**
     * Method to be called when service features change, so they can be cached. Can be called from an arbitrary thread.
     * 
     * @param update
     */
    protected void onServiceFeatureUpdate(Bundle update) {
        throw new UnsupportedOperationException();
    }
    
    
    /**
     * Method to be called when binders are received, so they can be cached. Can be called from an arbitrary thread.
     * 
     * @param resource
     * @param binder
     */
    protected void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
        
    }
    
    
    /*
     * interface methods
     */
    
    @Override
    public void register() {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void register(PMPRegistrationHandler handler) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void register(PMPRegistrationHandler handler, int timeout) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void updateServiceFeatures() {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void updateServiceFeatures(PMPServiceFeatureUpdateHandler handler) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void updateServiceFeatures(PMPServiceFeatureUpdateHandler handler, int timeout) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void requestServiceFeatures(List<String> serviceFeatures) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void requestServiceFeatures(String... serviceFeatures) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void requestServiceFeatures(List<String> serviceFeatures, PMPRequestServiceFeaturesHandler handler) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void requestServiceFeatures(List<String> serviceFeatures, PMPRequestServiceFeaturesHandler handler, boolean showDialog) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void requestServiceFeatures(List<String> serviceFeatures, PMPRequestServiceFeaturesHandler handler, boolean showDialog,
            int timeout) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void getResource(PMPResourceIdentifier resource) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void getResource(PMPResourceIdentifier resource, PMPRequestResourceHandler handler) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void getResource(PMPResourceIdentifier resource, PMPRequestResourceHandler handler, int timeout) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public Map<String, Boolean> getServiceFeatures() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean isServiceFeatureEnabled(String serviceFeature) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean areServiceFeaturesEnabled(List<String> serviceFeatures) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean areServiceFeaturesEnabled(String... serviceFeatures) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean areServiceFeaturesDisabled(List<String> serviceFeatures) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean areServiceFeaturesDisabled(String... serviceFeatures) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public List<String> listEnabledServiceFeatures(List<String> serviceFeatures) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public List<String> listEnabledServiceFeatures(String... serviceFeatures) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public List<String> listDisabledServiceFeatures(List<String> serviceFeatures) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public List<String> listDisabledServiceFeatures(String... serviceFeatures) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public Set<String> listAllServiceFeatures() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean isResourceCached(PMPResourceIdentifier resource) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public IBinder getResourceFromCache(PMPResourceIdentifier resource) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
