package de.unistuttgart.ipvs.pmp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AppService extends Service {

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
	 * Called when another application is going to bind the service.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return new IAppServiceStubImpl();
	}

}
