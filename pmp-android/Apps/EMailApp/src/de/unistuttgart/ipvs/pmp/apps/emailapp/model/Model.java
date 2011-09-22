package de.unistuttgart.ipvs.pmp.apps.emailapp.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.data.EMail;

/**
 * The model of the email app
 * @author Marcus Vetter
 *
 */
public class Model {

	/**
	 * Instance of this model (Singleton)
	 */
	private static Model instance = null;

	/**
	 * The context of the app
	 */
	private Context appContext;

	/**
	 * List of all emails in the inbox
	 */
	private List<EMail> inboxEMails;

	/**
	 * Private constructor (Singleton)
	 */
	private Model() {

	}

	/**
	 * This method returns the one and only instance of the model (Singleton)
	 */
	public static Model getInstance() {
		if (instance == null) {
			instance = new Model();
		}
		return instance;
	}

	/**
	 * Set the current service level and store it (preferences)
	 * 
	 * @param serviceLevel
	 *            the level to store
	 */
	public void setServiceLevel(int serviceLevel) {
		SharedPreferences app_preferences = PreferenceManager
				.getDefaultSharedPreferences(this.appContext);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("servicelevel", serviceLevel);
		if (!editor.commit()) {
			Log.e("Error while commiting preferences");
		}
	}

	/**
	 * Returns the service level of the app
	 * 
	 * @return the service level
	 */
	public int getServiceLevel() {
		SharedPreferences app_preferences = PreferenceManager
				.getDefaultSharedPreferences(this.appContext);
		return app_preferences.getInt("servicelevel", 0);
	}

	/**
	 * Returns the context of the app
	 * 
	 * @return app context
	 */
	public Context getAppContext() {
		return appContext;
	}

	/**
	 * Sets the context of the app
	 * 
	 * @param context
	 *            context of the app
	 */
	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}

	/**
	 * Get all emails of the inbox
	 * 
	 * @return list of emails
	 */
	public List<EMail> getInboxEMails() {
	    inboxEMails = new ArrayList<EMail>();
	    List<String> rec = new ArrayList<String>();
	    rec.add("You");
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
	    inboxEMails.add(new EMail("Me", rec, "Test", "Text"));
		return inboxEMails;
	}

	/**
	 * Set all emails of the inbox
	 * 
	 * @param inboxEMails
	 *            list of emails
	 */
	public void setInboxEMails(List<EMail> inboxEMails) {
		this.inboxEMails = inboxEMails;
	}

}
