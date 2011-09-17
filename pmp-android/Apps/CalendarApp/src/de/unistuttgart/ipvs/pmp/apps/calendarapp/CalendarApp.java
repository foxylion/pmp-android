package de.unistuttgart.ipvs.pmp.apps.calendarapp;

import java.io.IOException;
import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;

public class CalendarApp extends App {

    static {
	Log.setTagSufix("CalendarApp");
    }

    @Override
    protected String getServiceAndroidName() {
	return "de.unistuttgart.ipvs.pmp.apps.calendarapp";
    }

    @Override
    public void setActiveServiceLevel(int level) {
	Log.v("ServiceLevel set to: " + String.valueOf(level));
	Model.getInstance().setServiceLevel(level);
	changeFunctionalityAccordingToServiceLevel();
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
	Log.d("Registration succeed");
	DialogManager.getInstance().dismissWaitingDialog();
    }

    @Override
    public void onRegistrationFailed(String message) {
	Log.d("Registration failed:" + message);
	DialogManager.getInstance().dismissWaitingDialog();
    }

    /**
     * Changes the functionality of the app according to its set ServiceLevel
     */
    public void changeFunctionalityAccordingToServiceLevel() {
	Log.d("Changing ServiceLevel to level "
		+ String.valueOf(Model.getInstance().getServiceLevel()));

	int level = Model.getInstance().getServiceLevel();

	switch (level) {
	case 0:
	    Model.getInstance().clearLocalList();
	    Model.getInstance().getNewDateButton().setEnabled(false);
	    break;
	case 1:
	    Model.getInstance().loadDates();
	    Model.getInstance().getNewDateButton().setEnabled(true);
	    break;
	default:
	    break;
	}
    }

}
