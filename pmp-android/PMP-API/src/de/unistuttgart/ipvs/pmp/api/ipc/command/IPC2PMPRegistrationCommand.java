package de.unistuttgart.ipvs.pmp.api.ipc.command;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;
import de.unistuttgart.ipvs.pmp.service.pmp.RegistrationResult;

/**
 * Command to register an App with PMP.
 * 
 * @author Tobias Kuhn
 * 
 */
public class IPC2PMPRegistrationCommand extends IPC2PMPCommand<PMPRegistrationHandler> {
    
    public IPC2PMPRegistrationCommand(PMPRegistrationHandler handler, String sourceService, boolean includeUpdate,
            long timeout) {
        super(handler, sourceService, timeout);
    }
    
    
    @Override
    protected void executeOnPMP(IPMPService pmp) throws RemoteException {
        if (pmp.isRegistered(getSourceService())) {
            getPMPHandler().onAlreadyRegistered();
            
        } else {
            
            RegistrationResult rr = pmp.registerApp(getSourceService());
            if (rr.getSuccess()) {
                getPMPHandler().onSuccess();
            } else {
                getPMPHandler().onFailure(rr.getMessage());
            }
            
        }
    }
}
