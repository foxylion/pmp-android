package de.unistuttgart.ipvs.pmp.model.ipc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Bundle;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
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
    private AtomicInteger updateSession;
    
    /**
     * The map containing the IPC operations to be performed.
     */
    private ConcurrentMap<String, Bundle> queue;
    
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
        this.updateSession = new AtomicInteger(0);
        this.queue = new ConcurrentHashMap<String, Bundle>();
    }
    
    
    /**
     * Starts one cumulative update session. This means, the IPC provider will start buffering IPC messages instead of
     * directly delivering them directly. Be sure to always call {@link IPCProvider#endUpdate()} afterwards.
     */
    public synchronized void startUpdate() {
        this.updateSession.incrementAndGet();
        Log.d("IPC delayed update layer " + String.valueOf(this.updateSession) + " started.");
    }
    
    
    /**
     * Ends one cumulative update session started by {@link IPCProvider#startUpdate()}.
     */
    public synchronized void endUpdate() {
        Log.d("IPC delayed update layer " + String.valueOf(this.updateSession) + " ended.");
        if (this.updateSession.get() > 0) {
            this.updateSession.decrementAndGet();
        }
        if (this.updateSession.intValue() == 0) {
            rollout();
        }
    }
    
    
    /**
     * Rolls-out all queued up IPC operations.
     */
    private synchronized void rollout() {
        Log.d("Performing IPC rollout...");
        
        // for each entry, create a new binder        
        Set<String> latestKeySet = this.queue.keySet();
        for (final String key : latestKeySet) {
            // N.B. we must not use an entry set because due to concurrency the entry could get deleted
            //      by another execution before we access it.
            final AppServiceConnector asc = new AppServiceConnector(PMPApplication.getContext(), key);
            
            asc.addCallbackHandler(new AbstractConnectorCallback() {
                
                @Override
                public void onConnect(AbstractConnector connector) throws RemoteException {
                    // remove is atomic
                    Bundle value = IPCProvider.this.queue.remove(key);
                    if (value != null) {
                        asc.getAppService().updateServiceFeatures(value);
                    }
                }
            });
            // this call is asynchronous
            asc.bind();
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
        Assert.nonNull(appPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "appPackage", appPackage));
        Assert.nonNull(verification, new ModelMisuseError(Assert.ILLEGAL_NULL, "verification", verification));
        
        // create the new bundle
        Bundle b = new Bundle();
        for (Entry<ServiceFeature, Boolean> e : verification.entrySet()) {
            b.putBoolean(e.getKey().getLocalIdentifier(), e.getValue());
        }
        
        this.queue.put(appPackage, b);
        
        // run, if no session
        if (this.updateSession.intValue() == 0) {
            rollout();
        } else {
            Log.d("IPC connection queued.");
        }
    }
    
}
