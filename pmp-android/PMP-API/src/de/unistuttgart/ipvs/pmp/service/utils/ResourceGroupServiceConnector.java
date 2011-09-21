package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServiceApp;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;

/**
 * {@link ResourceGroupServiceConnector} is used for connecting (in this case binding) to a resource group service. Add
 * your {@link IConnectorCallback} for interacting with the service. Call {@link ResourceGroupServiceConnector#bind} to
 * start the connection.
 * 
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupServiceConnector extends AbstractConnector {
    
    public ResourceGroupServiceConnector(Context context, PMPSignee signee, String targetIdentifier) {
        super(context, signee, targetIdentifier);
    }
    
    
    public IResourceGroupServiceApp getAppService() {
        if (isCorrectBinder(IResourceGroupServiceApp.class)) {
            return IResourceGroupServiceApp.Stub.asInterface(getService());
        } else {
            return null;
        }
    }
    
    
    public IResourceGroupServicePMP getPMPService() {
        if (isCorrectBinder(IResourceGroupServicePMP.class)) {
            return IResourceGroupServicePMP.Stub.asInterface(getService());
        } else {
            return null;
        }
    }
    
    
    @Override
    protected void serviceConnected() {
        
    }
    
    
    @Override
    protected void serviceDisconnected() {
        
    }
    
}
