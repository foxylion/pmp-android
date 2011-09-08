package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;

/**
 * {@link AppServiceConnector} is used for connecting (in this case binding) to
 * services. Add your {@link IConnectorCallback} for interacting with the
 * service. Call {@link AppServiceConnector#bind} to start the connection.
 * 
 * @author Jakob Jarosch
 */
public class AppServiceConnector extends AbstractConnector {

    public AppServiceConnector(Context context, PMPSignee signature,
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
