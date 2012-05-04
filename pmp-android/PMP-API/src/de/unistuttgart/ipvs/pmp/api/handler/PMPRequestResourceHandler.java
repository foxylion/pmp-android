package de.unistuttgart.ipvs.pmp.api.handler;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.ipc.command.IPC2PMPRequestResourceCommand;

/**
 * Handles reactions for the {@link IPC2PMPRequestResourceCommand}. Handlers are guaranteed to be called in a separate
 * {@link Thread}.
 * 
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
     *            the AIDL interface for the resource, or null, if no such resource exists
     * @param isMocked
     *            whether the resource returns obvious fake data
     */
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
    }
}
