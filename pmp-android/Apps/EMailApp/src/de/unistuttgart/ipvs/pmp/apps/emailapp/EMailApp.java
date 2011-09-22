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
	protected String getServiceAndroidName() {
		return "de.unistuttgart.ipvs.pmp.apps.emailapp";
	}

	@Override
	public void setActiveServiceLevel(int level) {
		Log.v("Setting service level " + level);
		Model.getInstance().setServiceLevel(level);
	}

	@Override
	protected InputStream getXMLInputStream() {
        try {
            return getAssets().open("AppInformation.xml");
        } catch (IOException e) {
            Log.e("IOException during loading App XML", e);
            return null;
        }
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
