package de.unistuttgart.ipvs.pmp.api.ipc.command;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;

/**
 * Command to request a resource from PMP.
 * 
 * @author Tobias Kuhn
 * 
 */
public class IPC2PMPRequestResourceCommand extends IPC2PMPCommand<PMPRequestResourceHandler> {
    
    private final PMPResourceIdentifier resource;
    
    
    public IPC2PMPRequestResourceCommand(PMPResourceIdentifier resource, PMPRequestResourceHandler handler,
            String sourceService, long timeout) {
        super(handler, sourceService, timeout);
        this.resource = resource;
    }
    
    
    @Override
    protected void executeOnPMP(IPMPService pmp) throws RemoteException {
        getPMPHandler().onReceiveResource(this.resource,
                pmp.getResource(getSourceService(), this.resource.getResourceGroup(), this.resource.getResource()));
        
    }
}
