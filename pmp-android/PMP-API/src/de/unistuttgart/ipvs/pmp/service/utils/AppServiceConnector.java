package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;

/**
 * {@link AppServiceConnector} is used for connecting (in this case binding) to
 * an App service. Override {@link AppServiceConnector#serviceConnected()} to
 * implement your interaction with the service. Call
 * {@link AppServiceConnector#bind} to start the connection.
 * 
 * 
 * @author Jakob Jarosch
 */
public abstract class AppServiceConnector extends AbstractConnector {

    public AppServiceConnector(Context context, PMPSignee signature,
	    String targetIdentifier) {
	super(context, signature, targetIdentifier);
    }

}
