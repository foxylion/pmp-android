package de.unistuttgart.ipvs.pmp.service;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.service.IPMPServiceRegistration;

/**
 * AIDL-Interface implementation of the {@link PMPService}'s {@link IBinder}.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceRegistrationStubImpl extends IPMPServiceRegistration.Stub {

	private String identifier = null;

	public PMPServiceRegistrationStubImpl(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String registerApp(String url) throws RemoteException {
		/*
		 * Note: The Identifier of the App can be found in the identifier
		 * variable.
		 */

		// TODO IMPLEMENT
		throw new UnsupportedOperationException();
	}

	@Override
	public String registerResourceGroup(String url) throws RemoteException {
		/*
		 * Note: The Identifier of the ResourceGroup can be found in the
		 * identifier variable.
		 */

		// TODO IMPLEMENT
		throw new UnsupportedOperationException();
	}
}
