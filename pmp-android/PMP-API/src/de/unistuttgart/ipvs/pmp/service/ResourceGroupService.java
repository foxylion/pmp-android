package de.unistuttgart.ipvs.pmp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * External service for communication between Resources and or PMP the Apps. <br/>
 * The Service requires several informations in the intent, used to bind the
 * {@link PMPService}, put as extra into the {@link Intent}.
 * 
 * <pre>
 * intent.putExtraString("identifier", <App/PMP-Identifier>);
 * intent.putExtraString("type", <APP|PMP>);
 * intent.putExtraString("token", Generated Token for ResourceGroup);
 * </pre>
 * 
 * With a valid token the {@link IResourceGroupServicePMP} or !!CREATE
 * ALTERNATIVE BINDER (AIDL)!! Binder will be given back.
 * 
 * If an authentification fails the Service will give back NULL.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupService extends Service {

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

	// TODO IMPLEMENT

	/* This Binder should only be returned when PMP is connecting. */
	return new ResourceGroupServicePMPStubImpl();
    }
}
