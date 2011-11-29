package de.unistuttgart.ipvs.pmp.apps.vhike;

import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;

/**
 * This is the base of the vHike Application Extends from {@link App}
 * 
 * @author Alexander Wassiljew
 * 
 */
public class VHikeApp extends App {

	/**
	 * Called when the Application is started (Activity-Call or
	 * Service-StartUp).
	 */
	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void updateServiceFeatures(Bundle features) {
		Log.d("Updating ServiceFeatures");
	}

	@Override
	public void onRegistrationSuccess() {
		Log.d("Registration succes!");
	}

	@Override
	public void onRegistrationFailed(String message) {
		Log.d("Registration failed:" + message);
	}

}
