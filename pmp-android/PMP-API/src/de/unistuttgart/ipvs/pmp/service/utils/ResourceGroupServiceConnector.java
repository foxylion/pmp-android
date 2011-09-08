package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;

/**
 * {@link ResourceGroupServiceConnector} is used for connecting (in this case
 * binding) to a resource group service. Override
 * {@link ResourceGroupServiceConnector#serviceConnected()} to implement your
 * interaction with the service. Call {@link ResourceGroupServiceConnector#bind}
 * to start the connection.
 * 
 * 
 * @author Jakob Jarosch
 */
public abstract class ResourceGroupServiceConnector extends AbstractConnector {

    public ResourceGroupServiceConnector(Context context, PMPSignee signature,
	    String targetIdentifier) {
	super(context, signature, targetIdentifier);
    }

}
