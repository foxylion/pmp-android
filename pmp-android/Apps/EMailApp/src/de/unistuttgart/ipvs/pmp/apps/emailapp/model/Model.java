package de.unistuttgart.ipvs.pmp.apps.emailapp.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.emailapp.gui.activities.EMailAppActivity;

public class Model {
	
	/**
	 * Instance of this model (Singleton)
	 */
	private static Model instance = null;
	
    /**
     * The context of the app
     */
    private EMailAppActivity appContext;
	
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
	 * @param serviceLevel the level to store
	 */
	public void setServiceLevel(int serviceLevel) {
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.appContext);
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
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.appContext);
        return app_preferences.getInt("servicelevel", 0);
    }

}
