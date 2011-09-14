package de.unistuttgart.ipvs.pmp.service.utils;

import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServiceApp;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;
import android.content.Context;

/**
 * {@link ResourceGroupServiceConnector} is used for connecting (in this case
 * binding) to a resource group service. Add your {@link IConnectorCallback} for
 * interacting with the service. Call {@link ResourceGroupServiceConnector#bind}
 * to start the connection.
 * 
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupServiceConnector extends AbstractConnector {

    public ResourceGroupServiceConnector(Context context, PMPSignee signee,
	    String targetIdentifier) {
	super(context, signee, targetIdentifier);
    }

    public IResourceGroupServiceApp getAppService() {
	if (getService() == null) {
	    return null;
	}
	return IResourceGroupServiceApp.Stub.asInterface(getService());
    }
    
    public IResourceGroupServicePMP getPMPService() {
	if (getService() == null) {
	    return null;
	}
	return IResourceGroupServicePMP.Stub.asInterface(getService());
    }
    
    @Override
    protected void serviceConnected() {
	
    }

    @Override
    protected void serviceDisconnected() {
	
    }

}
