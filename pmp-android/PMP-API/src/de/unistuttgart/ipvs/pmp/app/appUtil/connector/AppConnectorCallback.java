package de.unistuttgart.ipvs.pmp.app.appUtil.connector;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.AppApplication;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * Class that the PMP service is calling if the app is connected to the service
 * or not.
 * 
 * @author Thorsten Berberich
 * 
 */
public class AppConnectorCallback implements IConnectorCallback {

    @Override
    public void connected() {
	try {
	    PMPSignee signee = AppApplication.getInstance().getSignee();
	    byte[] pmpKey = AppApplication.getInstance().getServiceConnector()
		    .getRegistrationService()
		    .registerApp(signee.getLocalPublicKey());
	    signee.setRemotePublicKey(PMPComponentType.PMP,
		    Constants.PMP_IDENTIFIER, pmpKey);

	    Log.d(AppApplication.getInstance().getContext().getPackageName()
		    + " connected");
	} catch (RemoteException e) {
	    Log.e("Registration failed", e);
	}
    }

    @Override
    public void bindingFailed() {
	Log.e(AppApplication.getInstance().getContext().getPackageName()
		+ " failed binding");
    }

    @Override
    public void disconnected() {
	Log.e(AppApplication.getInstance().getContext().getPackageName()
		+ " disconnected");
    }

}
