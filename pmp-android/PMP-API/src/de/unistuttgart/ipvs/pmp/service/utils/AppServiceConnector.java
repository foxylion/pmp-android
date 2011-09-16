package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.service.app.IAppServicePMP;

/**
 * {@link AppServiceConnector} is used for connecting (in this case binding) to
 * services. Add your {@link IConnectorCallback} for interacting with the
 * service. Call {@link AppServiceConnector#bind} to start the connection.
 * 
 * @author Jakob Jarosch
 */
public class AppServiceConnector extends AbstractConnector {

    public AppServiceConnector(Context context, PMPSignee signee,
	    String targetIdentifier) {
	super(context, signee, targetIdentifier);
    }

    /**
     * @return Returns the AppService or NULL if no Service was returned
     *         (authentication failed?).
     */
    public IAppServicePMP getAppService() {
	if (isCorrectBinder(IAppServicePMP.class)) {
	    return IAppServicePMP.Stub.asInterface(getService());
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
