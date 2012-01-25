package de.unistuttgart.ipvs.pmp.api.handler._default;

import java.util.concurrent.Semaphore;

import android.app.Activity;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.gui.servicefeature.ServiceFeatureDialog;
import de.unistuttgart.ipvs.pmp.api.handler.AbortIPCException;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestServiceFeaturesHandler;

public class PMPDefaultRequestSFHandler extends PMPRequestServiceFeaturesHandler {
    
    private Activity activity;
    private Semaphore semaphore = new Semaphore(0);
    private volatile boolean killServiceFeatureRequest = false;
    
    
    public PMPDefaultRequestSFHandler(Activity activity) {
        this.activity = activity;
    }
    
    
    @Override
    public void onPrepare() throws AbortIPCException {
        super.onPrepare();
        
        this.activity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                new ServiceFeatureDialog(activity, PMPDefaultRequestSFHandler.this).show();
            }
        });
        
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Log.e("Interrupted the ServiceFeatureHandler", e);
        }
        
        if (killServiceFeatureRequest) {
            throw new AbortIPCException();
        }
    }
    
    
    public void killServiceFeatureRequest() {
        this.killServiceFeatureRequest = true;
    }
    
    
    public void unblockHandler() {
        semaphore.release();
    }
    
}
