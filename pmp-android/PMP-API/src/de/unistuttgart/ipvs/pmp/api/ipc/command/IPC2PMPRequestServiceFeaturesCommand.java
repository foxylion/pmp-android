package de.unistuttgart.ipvs.pmp.api.ipc.command;

import java.util.List;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestServiceFeaturesHandler;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;

/**
 * Command to request the enabling of service features at PMP.
 * 
 * @author Tobias Kuhn
 * 
 */
public class IPC2PMPRequestServiceFeaturesCommand extends IPC2PMPCommand<PMPRequestServiceFeaturesHandler> {
    
    private final List<String> serviceFeatures;
    
    
    public IPC2PMPRequestServiceFeaturesCommand(List<String> serviceFeatures, PMPRequestServiceFeaturesHandler handler,
            String sourceService, long timeout) {
        super(handler, sourceService, timeout);
        this.serviceFeatures = serviceFeatures;
    }
    
    
    @Override
    protected void executeOnPMP(IPMPService pmp) throws RemoteException {
        if (!pmp.requestServiceFeature(getSourceService(),
                this.serviceFeatures.toArray(new String[this.serviceFeatures.size()]))) {
            getPMPHandler().onBindingFailed();
        }
    }
    
}
