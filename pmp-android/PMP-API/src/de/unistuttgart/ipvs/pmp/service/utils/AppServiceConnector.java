package de.unistuttgart.ipvs.pmp.service.utils;

import de.unistuttgart.ipvs.pmp.service.app.IAppService;
import android.content.Context;

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
    public IAppService getAppService() {
	if (getService() == null) {
	    return null;
	}
	return IAppService.Stub.asInterface(getService());
    }

    @Override
    protected void serviceConnected() {

    }

    @Override
    protected void serviceDisconnected() {

    }

}
