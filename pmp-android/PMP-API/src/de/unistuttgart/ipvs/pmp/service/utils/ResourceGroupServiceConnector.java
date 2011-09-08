package de.unistuttgart.ipvs.pmp.service.utils;

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

    public ResourceGroupServiceConnector(Context context, PMPSignee signature,
	    String targetIdentifier) {
	super(context, signature, targetIdentifier);
    }

    @Override
    protected void serviceConnected() {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void serviceDisconnected() {
	// TODO Auto-generated method stub
	
    }

}
