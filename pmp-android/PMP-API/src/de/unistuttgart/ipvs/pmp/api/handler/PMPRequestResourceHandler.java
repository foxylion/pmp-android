package de.unistuttgart.ipvs.pmp.api.handler;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.ipc.command.IPC2PMPRequestResourceCommand;

/**
 * Handles reactions for the {@link IPC2PMPRequestResourceCommand}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPRequestResourceHandler extends PMPHandler {
    
    /**
     * Called when the resource is received.
     * 
     * @param resource
     *            the identifier of the resource
     * @param binder
     *            the AIDL interface for the resource
     */
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
    }
}
