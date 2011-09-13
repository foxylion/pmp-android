package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceResourceGroup;

/**
 * {@link PMPServiceConnector} is used for connecting (in this case binding) to
 * the PMP service. Add your {@link IConnectorCallback} for interacting with the
 * service. Call {@link PMPServiceConnector#bind} to start the connection.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceConnector extends AbstractConnector {

    public PMPServiceConnector(Context context, PMPSignee signee) {
	super(context, signee, Constants.PMP_IDENTIFIER);
    }

    public IPMPServiceRegistration getRegistrationService() {
	IPMPServiceRegistration service = IPMPServiceRegistration.Stub
		.asInterface(getService());
	return service;
    }

    public IPMPServiceResourceGroup getResourceGroupService() {
	IPMPServiceResourceGroup service = IPMPServiceResourceGroup.Stub
		.asInterface(getService());
	return service;
    }

    public IPMPServiceApp getAppService() {
	IPMPServiceApp service = IPMPServiceApp.Stub.asInterface(getService());
	return service;
    }

    /**
     * 
     * @return true, if the class connecting to PMP is already registered and
     *         does not require any registration action via the
     *         {@link PMPServiceConnector#getRegistrationService()} interface.
     */
    public boolean isRegistered() {
	// TODO: implement me
	return false;
    }

    @Override
    protected void serviceConnected() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void serviceDisconnected() {
	// TODO Auto-generated method stub

    }

}
