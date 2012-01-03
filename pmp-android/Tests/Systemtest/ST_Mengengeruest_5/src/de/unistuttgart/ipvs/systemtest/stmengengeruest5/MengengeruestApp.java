package de.unistuttgart.ipvs.systemtest.stmengengeruest5;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class MengengeruestApp extends App {

    @Override
    public void onRegistrationSuccess() {
	// Connector to get the initial service feature
	final PMPServiceConnector pmpconnector = new PMPServiceConnector(
		getApplicationContext());
	pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {

	    @Override
	    public void onConnect(AbstractConnector connector)
		    throws RemoteException {
		pmpconnector.getAppService().getServiceFeatureUpdate(
			getPackageName());
	    }
	});

	// Connect to the service
	pmpconnector.bind();
	Log.d("Registration succeed");
    }

    @Override
    public void onRegistrationFailed(String message) {
	Log.d("Registration failed");
    }
}
