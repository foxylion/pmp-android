package de.unistuttgart.ipvs.pmp.apps.calendarapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;

public class CalendarApp extends App {

	@Override
	protected String getServiceAndroidName() {
		return "de.unistuttgart.ipvs.pmp.apps.calendarapp";
	}

	@Override
	public void setActiveServiceLevel(int level) {
		// TODO Auto-generated method stub

	}

	@Override
	protected InputStream getXMLInputStream() {
		try {
			return getAssets().open("/AppInformation.xml");
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
