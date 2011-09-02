package de.unistuttgart.ipvs.pmp.service.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The {@link AppService} is used to allow PMPService a connection where
 * informations about the App are served.
 * 
 * @author Jakob Jarosch
 */
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