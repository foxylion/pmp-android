package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceResourceGroup;

/**
 * {@link PMPServiceConnector} is used for connecting (in this case binding) to the PMP service. Add your
 * {@link IConnectorCallback} for interacting with the service. Call {@link PMPServiceConnector#bind} to start the
 * connection.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceConnector extends AbstractConnector {
    
    public PMPServiceConnector(Context context, PMPSignee signee) {
        super(context, signee, Constants.PMP_IDENTIFIER);
    }
    
    
    public IPMPServiceRegistration getRegistrationService() {
        if (isCorrectBinder(IPMPServiceRegistration.class)) {
            return IPMPServiceRegistration.Stub.asInterface(getService());
        } else {
            return null;
        }
    }
    
    
    public IPMPServiceResourceGroup getResourceGroupService() {
        if (isCorrectBinder(IPMPServiceResourceGroup.class)) {
            return IPMPServiceResourceGroup.Stub.asInterface(getService());
        } else {
            return null;
        }
    }
    
    
    public IPMPServiceApp getAppService() {
        if (isCorrectBinder(IPMPServiceApp.class)) {
            return IPMPServiceApp.Stub.asInterface(getService());
        } else {
            return null;
        }
    }
    
    
    /**
     * 
     * @return true, if the class connecting to PMP is already registered and does not require any registration action
     *         via the {@link PMPServiceConnector#getRegistrationService()} interface.
     */
    public boolean isRegistered() {
        if (isCorrectBinder(IPMPServiceRegistration.class)) {
            return false;
        } else {
            return true;
        }
    }
    
    
    @Override
    protected void serviceConnected() {
        
    }
    
    
    @Override
    protected void serviceDisconnected() {
        
    }
    
}
