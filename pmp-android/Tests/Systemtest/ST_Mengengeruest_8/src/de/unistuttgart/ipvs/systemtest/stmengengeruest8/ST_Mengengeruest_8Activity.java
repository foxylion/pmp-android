package de.unistuttgart.ipvs.systemtest.stmengengeruest8;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class ST_Mengengeruest_8Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	// Connector to check if the app is registered yet
	final PMPServiceConnector connector = new PMPServiceConnector(
		this.getApplicationContext(),
		((MengengeruestApp) this.getApplicationContext()).getSignee());
	connector.addCallbackHandler(new IConnectorCallback() {

	    @Override
	    public void disconnected() {
		Log.e("Disconnected");
	    }

	    @Override
	    public void connected() {

		// Check if the service is registered yet
		if (!connector.isRegistered()) {
		    // Register the service
		    ((App) getApplication()).register(getApplicationContext());

		    connector.unbind();
		} else {
		    Log.v("App already registered");
		    connector.unbind();
		}
	    }

	    @Override
	    public void bindingFailed() {
		Log.e("Binding failed during registering app.");
	    }
	});
	connector.bind();
    }

    @Override
    public void onResume() {
	super.onResume();
	// Get the service level
	TextView sl = (TextView) findViewById(R.id.servicelevel);
	SharedPreferences app_preferences = PreferenceManager
		.getDefaultSharedPreferences(this.getApplicationContext());
	String serviceLevel = String.valueOf(app_preferences.getInt(
		"servicelevel", 0));
	sl.setText(serviceLevel);
    }
}