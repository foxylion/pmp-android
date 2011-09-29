package de.unistuttgart.ipvs.systemtest.stmengengeruest4;

import java.io.IOException;
import java.io.InputStream;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;

public class MengengeruestApp extends App{

    @Override
    protected String getServiceAndroidName() {
	return "de.unistuttgart.ipvs.systemtest.stmengengeruest4";
    }

    @Override
    public void setActiveServiceLevel(int level) {
	SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putInt("servicelevel", level);
        editor.commit();
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
    }

    @Override
    public void onRegistrationFailed(String message) {
	Log.d("Registration failed");
    }

}
