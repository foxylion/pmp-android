package de.unistuttgart.ipvs.pmp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AppService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO IMPLEMENT
		return new IAppServiceStupImpl();
	}

}
