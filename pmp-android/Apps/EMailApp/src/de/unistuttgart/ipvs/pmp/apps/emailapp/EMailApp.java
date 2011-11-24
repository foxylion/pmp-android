package de.unistuttgart.ipvs.pmp.apps.emailapp;

import java.io.IOException;
import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.Model;

/**
 * The email app
 * @author Marcus Vetter
 *
 */
public class EMailApp extends App {
	
    static {
        Log.setTagSufix("EMailApp");
    }

	@Override
	public void setActiveServiceLevel(int level) {
		Log.v("Setting service level " + level);
		Model.getInstance().setServiceLevel(level);
	}

	@Override
	public void onRegistrationSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegistrationFailed(String message) {
		// TODO Auto-generated method stub

	}

}
