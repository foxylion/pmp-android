package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util;

import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;

public class DialogManager {

    private static DialogManager instance = null;

    private Dialog waitingDialog;


    private DialogManager() {
    }

    public static DialogManager getInstance() {
	if (instance == null) {
	    instance = new DialogManager();
	}
	return instance;
    }

    /**
     * Shows a {@link ProgressDialog} while registering the app
     */
    public void showWaitingDialog() {
	new Handler().post(new Runnable() {
	    @Override
	    public void run() {
		waitingDialog = ProgressDialog.show(Model.getInstance().getContext(), "Please wait...",
			"Registration is running.", true);
	    }
	});
    }

    /**
     * Disposes the dialog that is shown while registering. This is called when
     * the app is registered succesful and the PMPService calls
     * {@link CalendarApp#onRegistrationSuccess()} or
     * {@link CalendarApp#onRegistrationFailed(String)()}
     */
    public void dismissWaitingDialog() {
	waitingDialog.dismiss();
    }
}
