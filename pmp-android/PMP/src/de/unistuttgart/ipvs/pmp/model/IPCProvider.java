package de.unistuttgart.ipvs.pmp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.AppServiceConnector;

/**
 * General IPC provider which provides all the inter-process communication necessary for the model.
 * 
 * @author Tobias Kuhn
 * 
 */
public class IPCProvider {
    
    /**
     * How many cumulative update sessions are in progress, for > 0 no rollout should be done.
     */
    private int updateSession;
    
    /**
     * The map containing the IPC operations to be performed.
     */
    private Map<String, Bundle> queue;
    
    /**
     * Singleton stuff
     */
    private static final IPCProvider instance = new IPCProvider();
    
    
    public static IPCProvider getInstance() {
        return instance;
    }
    
    
    /**
     * Singleton constructor
     */
    private IPCProvider() {
        this.updateSession = 0;
        this.queue = new HashMap<String, Bundle>();
    }
    
    
    /**
     * Starts one cumulative update session. This means, the IPC provider will start buffering IPC messages instead of
     * directly delivering them directly. Be sure to always call {@link IPCProvider#endUpdate()} afterwards.
     */
    public synchronized void startUpdate() {
        this.updateSession++;
    }
    
    
    /**
     * Ends one cumulative update session started by {@link IPCProvider#startUpdate()}.
     */
    public synchronized void endUpdate() {
        if (this.updateSession > 0) {
            this.updateSession--;
        }
        if (this.updateSession == 0) {
            rollout();
        }
    }
    
    
    /**
     * Rolls-out all queued up IPC operations.
     */
    private synchronized void rollout() {
        for (final Entry<String, Bundle> e : this.queue.entrySet()) {
            final AppServiceConnector asc = new AppServiceConnector(PMPApplication.getContext(), e.getKey());
            
            asc.addCallbackHandler(new AbstractConnectorCallback() {
                
                @Override
                public void onConnect(AbstractConnector connector) throws RemoteException {
                    asc.getAppService().updateServiceFeatures(e.getValue());
                }
            });
            // this call is asynchronous
            asc.bind();
            
            this.queue.remove(e.getKey());
        }
    }
    
    
    /**
     * Queues an IPC operation to be done. Might not be done immediately, if a cumulative update session is in progress.
     * 
     * @param appPackage
     *            the package of the app
     * @param verification
     *            a map from the app's service features to boolean whereas the mapping should be true if and only if the
     *            service feature is active i.e. granted
     */
    public synchronized void queue(String appPackage, Map<ServiceFeature, Boolean> verification) {
        Bundle b = new Bundle();
        
        for (Entry<ServiceFeature, Boolean> e : verification.entrySet()) {
            b.putBoolean(e.getKey().getLocalIdentifier(), e.getValue());
        }
        
        this.queue.put(appPackage, b);
        
        // run, if no session
        if (this.updateSession == 0) {
            rollout();
        }
    }
    
}
