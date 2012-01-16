package de.unistuttgart.ipvs.pmp.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPServiceFeatureUpdateHandler;
import de.unistuttgart.ipvs.pmp.api.ipc.IPCScheduler;

import android.app.Application;
import android.os.Bundle;
import android.os.IBinder;

public class PMP implements IPMP {
    
    private static PMP instance;
    private ConcurrentMap<String, Boolean> sfs;
    private ConcurrentMap<PMPResourceIdentifier, IBinder> res;
    private Application application;
    public IPCScheduler scheduler;
    
    
    public static IPMP get(Application application) {
        throw new UnsupportedOperationException();
    }
    
    
    public static IPMP get() {
        throw new UnsupportedOperationException();
    }
    
    
    protected void onServiceFeatureUpdate(Bundle update) {
        throw new UnsupportedOperationException();
    }
    
    
    private void setApplication(Application application) {
        throw new UnsupportedOperationException();
    }
    
    
    public void register(PMPRegistrationHandler handler, boolean includeUpdate, int timeout) {
        throw new UnsupportedOperationException();
    }
    
    
    public void updateServiceFeatures(PMPServiceFeatureUpdateHandler handler, int timeout) {
        throw new UnsupportedOperationException();
    }
    
    
    public void requestServiceFeatures(List<String> sfs, boolean showDialog, int timeout) {
        throw new UnsupportedOperationException();
    }
    
    
    public Map<String, Boolean> getServiceFeatures() {
        throw new UnsupportedOperationException();
    }
    
    
    public boolean isServiceFeatureEnabled(String sf) {
        throw new UnsupportedOperationException();
    }
    
    
    public boolean areServiceFeaturesEnabled(List<String> sfs) {
        throw new UnsupportedOperationException();
    }
    
    
    public List<String> listAvailableServiceFeatures(List<String> sfs) {
        throw new UnsupportedOperationException();
    }
    
    
    public List<String> listUnavailableServiceFeatures(List<String> sfs) {
        throw new UnsupportedOperationException();
    }
    
    
    public List<String> listAllServiceFeatures() {
        throw new UnsupportedOperationException();
    }
    
    
    public void getResource(PMPResourceIdentifier res, PMPRequestResourceHandler handler, int timeout) {
        throw new UnsupportedOperationException();
    }
    
    
    public boolean isResourceCached(PMPResourceIdentifier res) {
        throw new UnsupportedOperationException();
    }
    
    
    public IBinder getResourceFromCache(PMPResourceIdentifier res) {
        throw new UnsupportedOperationException();
    }
}
