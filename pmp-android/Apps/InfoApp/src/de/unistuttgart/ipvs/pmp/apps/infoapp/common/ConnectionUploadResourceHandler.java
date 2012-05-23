/**
 * 
 */
package de.unistuttgart.ipvs.pmp.apps.infoapp.common;

import java.util.concurrent.Semaphore;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection;

/**
 * @author Thorsten
 * 
 */
public class ConnectionUploadResourceHandler extends AbstractRequestRessourceHandler {
    
    /**
     * @param sem
     */
    public ConnectionUploadResourceHandler(Semaphore sem) {
        super(sem);
    }
    
    
    @Override
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
        IConnection connectionRG = IConnection.Stub.asInterface(binder);
        try {
            this.setURL(connectionRG.uploadData());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        sem.release();
    }
    
}
