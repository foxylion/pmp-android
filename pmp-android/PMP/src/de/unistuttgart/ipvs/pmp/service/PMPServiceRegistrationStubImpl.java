package de.unistuttgart.ipvs.pmp.service;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
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
    public byte[] registerApp(final byte[] publicKey) throws RemoteException {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		ModelSingleton.getInstance().getModel()
			.addApp(identifier, publicKey);
	    }
	}).start();

	return PMPApplication.getSignee().getLocalPublicKey();
    }

    @Override
    public byte[] registerResourceGroup(final byte[] publicKey)
	    throws RemoteException {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		ModelSingleton.getInstance().getModel()
			.addResourceGroup(identifier, publicKey);
	    }
	}).start();

	return PMPApplication.getSignee().getLocalPublicKey();
    }

    @Override
    public void testBinding() throws RemoteException {

    }
}
