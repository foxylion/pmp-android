package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceResourceGroup;

/**
 * {@link PMPServiceConnector} is used for connecting (in this case binding) to
 * the PMP service. Override {@link PMPServiceConnector#serviceConnected()} to
 * implement your interaction with the service. Call
 * {@link PMPServiceConnector#bind} to start the connection.
 * 
 * 
 * @author Jakob Jarosch
 */
public abstract class PMPServiceConnector extends AbstractConnector {

    public PMPServiceConnector(Context context, PMPSignee signature) {
	super(context, signature, Constants.PMP_IDENTIFIER);
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

}
