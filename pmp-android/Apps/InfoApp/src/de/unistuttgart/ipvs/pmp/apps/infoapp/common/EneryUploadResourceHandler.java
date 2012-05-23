/**
 * 
 */
package de.unistuttgart.ipvs.pmp.apps.infoapp.common;

import java.util.concurrent.Semaphore;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.aidl.IEnergy;

/**
 * @author Thorsten
 * 
 */
public class EneryUploadResourceHandler extends AbstractRequestRessourceHandler {
    
    /**
     * @param sem
     */
    public EneryUploadResourceHandler(Semaphore sem) {
        super(sem);
    }
    
    
    @Override
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
        IEnergy energyRG = IEnergy.Stub.asInterface(binder);
        try {
            this.setURL(energyRG.uploadData());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        sem.release();
    }
    
}
