package de.unistuttgart.ipvs.pmp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PMPService extends Service {

	/**
	 * on creation of service called (only once).
	 */
	@Override
	public void onCreate() {

	}

	/**
	 * called, when service is going to shutdown.
	 */
	@Override
	public void onDestroy() {

	}

	/**
	 * called on startup the service.
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {

		/*
		 * We want this service to continue running until it is explicitly
		 * stopped, so return sticky.
		 */
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new PMPServiceStubImpl();
	}
}