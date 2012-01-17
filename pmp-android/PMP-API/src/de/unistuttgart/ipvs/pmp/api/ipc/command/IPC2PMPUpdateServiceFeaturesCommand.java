package de.unistuttgart.ipvs.pmp.api.ipc.command;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.handler.PMPHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPServiceFeatureUpdateHandler;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;

/**
 * Command to get the current service features.
 * 
 * @author Tobias Kuhn
 * 
 */
public class IPC2PMPUpdateServiceFeaturesCommand extends IPC2PMPCommand<PMPServiceFeatureUpdateHandler> {
    
    public IPC2PMPUpdateServiceFeaturesCommand(PMPServiceFeatureUpdateHandler handler, String sourceService,
            long timeout) {
        super(handler, sourceService, timeout);
    }
    
    
    @Override
    protected void executeOnPMP(IPMPService pmp) throws RemoteException {
        if (!pmp.getServiceFeatureUpdate(getSourceService())) {
            getPMPHandler().onUpdateFailed();
        }
    }
    
    
    @Override
    protected PMPHandler getNullHandler() {
        return new PMPServiceFeatureUpdateHandler();
    }
    
}
