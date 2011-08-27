package de.unistuttgart.ipvs.pmp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ResourceGroupService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO IMPLEMENT
		
		/* This Binder should only be returned when PMP is connecting. */
		return new ResourceGroupServiceStubImpl();
	}
}
