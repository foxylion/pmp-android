package de.unistuttgart.ipvs.pmp.service;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;

/**
 * Implementation of the {@link IPMPServiceRegistration.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceRegistrationStubImpl extends
	IPMPServiceRegistration.Stub {

    private String identifier = null;

    public PMPServiceRegistrationStubImpl(String identifier) {
	this.identifier = identifier;
    }

    @Override
    public byte[] registerApp(byte[] publicKey) throws RemoteException {
	/*
	 * Note: The Identifier of the App can be found in the identifier
	 * variable. Execution should be done in Background and PublicKey should
	 * be returned immediately.
	 */

	// TODO IMPLEMENT
	throw new UnsupportedOperationException();
    }

    @Override
    public byte[] registerResourceGroup(byte[] publicKey)
	    throws RemoteException {
	/*
	 * Note: The Identifier of the ResourceGroup can be found in the
	 * identifier variable. Execution should be done in Background and
	 * PublicKey should be returned immediately.
	 */

	// TODO IMPLEMENT
	throw new UnsupportedOperationException();
    }
}
