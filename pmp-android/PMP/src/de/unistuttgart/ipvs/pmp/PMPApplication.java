package de.unistuttgart.ipvs.pmp;

import android.app.Application;
import android.content.Context;
import de.unistuttgart.ipvs.pmp.service.PMPService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * This acts like an internal provider for {@link Context}.
 * 
 * @author Jakob Jarosch
 * 
 */
public class PMPApplication extends Application {

    /**
     * Singleton instance of the Class.
     */
    private static PMPApplication instance;

    /**
     * Called when the Application is started (Activity-Call or
     * Service-StartUp).
     */
    @Override
    public void onCreate() {
	super.onCreate();

	PMPApplication.instance = this;
    }

    /**
     * @return Returns an actual {@link Context} of the Application.
     */
    public static Context getContext() {
	return instance.getApplicationContext();
    }

    public static PMPSignee getSignee() {
	PMPSignee signee = new PMPSignee(PMPComponentType.PMP,
		getContext());
	signee.setIdentifier(Constants.PMP_IDENTIFIER);

	return signee;
    }
}
