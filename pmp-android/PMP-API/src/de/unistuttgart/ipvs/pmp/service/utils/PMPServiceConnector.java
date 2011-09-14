package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
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

    private boolean registred = false;
    
    public PMPServiceConnector(Context context, PMPSignee signee) {
	super(context, signee, Constants.PMP_IDENTIFIER);
    }

    public IPMPServiceRegistration getRegistrationService() {
	if(getService() == null) {
	    return null;
	}
	IPMPServiceRegistration service = IPMPServiceRegistration.Stub
		.asInterface(getService());
	
	return service;
    }

    public IPMPServiceResourceGroup getResourceGroupService() {
	if(getService() == null) {
	    return null;
	}
	IPMPServiceResourceGroup service = IPMPServiceResourceGroup.Stub
		.asInterface(getService());
	
	return service;
    }

    public IPMPServiceApp getAppService() {
	if(getService() == null) {
	    return null;
	}
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
	return registred;
    }

    @Override
    protected void serviceConnected() {
	try {
	    IPMPServiceRegistration service = IPMPServiceRegistration.Stub
			.asInterface(getService()); 
	    service.testBinding();
	} catch(SecurityException e) {
	    registred = true;
	} catch(RemoteException e) {
	    Log.e("Got an RemoteException while testing the connected binder in PMPServiceConnector.", e);
	}
    }

    @Override
    protected void serviceDisconnected() {
	
    }

}
