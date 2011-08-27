package de.unistuttgart.ipvs.pmp.service;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.service.IPMPService;
import de.unistuttgart.ipvs.pmp.app.App;

public class PMPServiceStubImpl extends IPMPService.Stub {

	@Override
	public boolean registerApp(App app) throws RemoteException {
		// TODO IMPLEMENT
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean registerResourceGroup(String resourceGroup)
			throws RemoteException {
		// TODO IMPLEMENT
		throw new UnsupportedOperationException();
	}

	@Override
	public void savePrivacyLevel(String app, String resourceGroup,
			String privacyLevel, String value) throws RemoteException {
		// TODO IMPLEMENT
		throw new UnsupportedOperationException();
	}

}
