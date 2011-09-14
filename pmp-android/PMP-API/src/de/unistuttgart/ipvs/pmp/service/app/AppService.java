package de.unistuttgart.ipvs.pmp.service.app;

import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * The {@link AppService} is used to allow PMPService a connection where
 * informations about the App are served.
 * 
 * @author Jakob Jarosch
 */
public class AppService extends PMPSignedService {

    @Override
    protected PMPSignee createSignee() {
	return null;
    }

    @Override
    public IBinder onSignedBind(Intent intent) {
	return new IAppServiceStubImpl();
    }

    @Override
    public IBinder onUnsignedBind(Intent intent) {
	return null;
    }
}
