/**
 * 
 */
package de.unistuttgart.ipvs.pmp.apps.infoapp.common;

import java.util.concurrent.Semaphore;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.IEnergy;

/**
 * The upload request resource handler
 * 
 * @author Marcus Vetter
 * 
 */
public class UploadRequestResourceHandler extends PMPRequestResourceHandler {
    
    private String URL = null;
    private Semaphore s;
    
    
    public UploadRequestResourceHandler(Semaphore s) {
        this.s = s;
    }
    
    
    @Override
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
        IEnergy energyRG = IEnergy.Stub.asInterface(binder);
        try {
            this.setURL(energyRG.uploadData());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        s.release();
    }
    
    
    /**
     * @return the uRL
     */
    public String getURL() {
        return URL;
    }
    
    
    /**
     * @param uRL
     *            the uRL to set
     */
    public void setURL(String uRL) {
        URL = uRL;
    }
}
