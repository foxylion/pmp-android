package de.unistuttgart.ipvs.pmp.api;

import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPServiceFeatureUpdateHandler;

import android.os.IBinder;

public interface IPMP {
    
    public void register(PMPRegistrationHandler handler, boolean includeUpdate, int timeout);
    
    
    public void updateServiceFeatures(PMPServiceFeatureUpdateHandler handler, int timeout);
    
    
    public void requestServiceFeatures(List<String> sfs, boolean showDialog, int timeout);
    
    
    public Map<String, Boolean> getServiceFeatures();
    
    
    public boolean isServiceFeatureEnabled(String sf);
    
    
    public boolean areServiceFeaturesEnabled(List<String> sfs);
    
    
    public List<String> listAvailableServiceFeatures(List<String> sfs);
    
    
    public List<String> listUnavailableServiceFeatures(List<String> sfs);
    
    
    public List<String> listAllServiceFeatures();
    
    
    public void getResource(PMPResourceIdentifier res, PMPRequestResourceHandler handler, int timeout);
    
    
    public boolean isResourceCached(PMPResourceIdentifier res);
    
    
    public IBinder getResourceFromCache(PMPResourceIdentifier res);
}
