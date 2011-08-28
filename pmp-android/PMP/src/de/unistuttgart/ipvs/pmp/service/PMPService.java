package de.unistuttgart.ipvs.pmp.service;

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
 * intent.putExtraString("identifier", <App/ResourceGroup-Identifier>);
 * intent.putExtraString("type", <APP|RESOURCEGROUP>);
 * intent.putExtraString("token", Generated Token for PMPService);
 * </pre>
 * 
 * The token is optional, if you do not sent a token, the Service will handle
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
		String type = intent.getStringExtra("type");
		String identifier = intent.getStringExtra("identifier");
		String token = intent.getStringExtra("token");

		if (identifier == null || type == null) {
			return null;
		} else if (token == null) {
			return new PMPServiceRegistrationStubImpl(identifier);
		} else {
			/* Should be a normal authentification */
			if(type.toLowerCase().equals("app")) {
				if (ModelSingleton.getInstance().checkAppToken(identifier, token)) {
					return new PMPServiceAppStubImpl(identifier);
				} else {
					return null;
				}
			} else if (type.toLowerCase().equals("resourcegroup")) {
				if (ModelSingleton.getInstance().checkResourceGroupToken(identifier, token)) {
					return new PMPServiceResourceGroupStubImpl(identifier);
				} else {
					return null;
				}
			}  else {
				return null;
			}
		}
	}
}