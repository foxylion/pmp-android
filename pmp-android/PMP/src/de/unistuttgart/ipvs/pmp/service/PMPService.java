package de.unistuttgart.ipvs.pmp.service;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * External service for communication between PMP, the Resources and Apps. <br/>
 * The Service requires several informations in the intent, used to bind the
 * {@link PMPService}, put as extra into the {@link Intent}.
 * 
 * <pre>
 * intent.putExtraString(Constants.INTENT_TYPE, <Constants.TYPE_APP|Constants.TYPE_RESOURCEGROUP>);
 * intent.putExtraString(Constants.INTENT_IDENTIFIER, <App/ResourceGroup-Identifier>);
 * intent.putExtraString(Constants.INTENT_SIGNATURE, Generated signature for PMPService);
 * </pre>
 * 
 * The signature is optional, if you do not sent a signature, the Service will handle
 * the binding as an registration and gives back the
 * {@link IPMPServiceRegistration} Binder.<br/>
 * With a valid token the {@link IPMPServiceResourceGroup} or
 * {@link IPMPServiceApp} Binder will be given back.
 * 
 * If an authentification fails the Service will give back NULL.
 * 
 * @author Jakob Jarosch
 */
public class PMPService extends Service {

    /**
     * On creation of service called (only once).
     */
    @Override
    public void onCreate() {

    }

    /**
     * Called when service is going to shutdown.
     */
    @Override
    public void onDestroy() {

    }

    /**
     * Called on startup the service.
     */
    public int onStartCommand(Intent intent, int flags, int startId) {

	/*
	 * We want this service to continue running until it is explicitly
	 * stopped, so return sticky.
	 */
	return START_STICKY;
    }

    /**
     * Called when another application is going to bind the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
	String type = intent.getStringExtra(Constants.INTENT_TYPE);
	String identifier = intent.getStringExtra(Constants.INTENT_IDENTIFIER);
	String signature = intent.getStringExtra(Constants.INTENT_SIGNATURE);

	if (identifier == null || type == null) {
	    return null;
	} else if (signature == null) {
	    return new PMPServiceRegistrationStubImpl(identifier);
	} else {
	    /* Should be a normal authentification */
	    if (type.equals(Constants.TYPE_APP)) {
		/* Authentification from an app */
		if (ModelSingleton.getInstance().checkAppToken(identifier,
			signature)) {
		    return new PMPServiceAppStubImpl(identifier);
		} else {
		    return null;
		}
	    } else if (type.equals(Constants.TYPE_RESOURCEGROUP)) {
		/* Authentification from a resourcegroup */
		if (ModelSingleton.getInstance().checkResourceGroupToken(
			identifier, signature)) {
		    return new PMPServiceResourceGroupStubImpl(identifier);
		} else {
		    return null;
		}
	    } else {
		/* no valid type identifier found */
		return null;
	    }
	}
    }
}