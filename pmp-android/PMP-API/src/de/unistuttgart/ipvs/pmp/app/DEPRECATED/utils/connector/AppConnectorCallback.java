package de.unistuttgart.ipvs.pmp.app.DEPRECATED.utils.connector;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.DEPRECATED.ApplicationApp;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * Class that the PMP service is calling if the app is connected to the service
 * or not.
 * 
 * @author Thorsten Berberich
 * 
 */
@Deprecated
public class AppConnectorCallback implements IConnectorCallback {

    @Override
    public void connected() {
	try {
	    /*
	     * Register the app only if its not registered yet
	     */
	    if (!ApplicationApp.getInstance().getServiceConnector()
		    .isRegistered()) {
		PMPSignee signee = ApplicationApp.getInstance().getSignee();
		byte[] pmpKey = ApplicationApp.getInstance()
			.getServiceConnector().getRegistrationService()
			.registerApp(signee.getLocalPublicKey());
		signee.setRemotePublicKey(PMPComponentType.PMP,
			Constants.PMP_IDENTIFIER, pmpKey);

		Log.d(ApplicationApp.getInstance().getContext()
			.getPackageName()
			+ " connected");
	    }
	} catch (RemoteException e) {
	    Log.e("Registration failed", e);
	}
    }

    @Override
    public void bindingFailed() {
	Log.e(ApplicationApp.getInstance().getContext().getPackageName()
		+ " failed binding");
    }

    @Override
    public void disconnected() {
	Log.e(ApplicationApp.getInstance().getContext().getPackageName()
		+ " disconnected");
    }

}
