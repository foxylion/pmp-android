package de.unistuttgart.ipvs.systemtest.stmengengeruest9;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.TextView;

public class ST_Mengengeruest_9Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	final PMPServiceConnector pmpconnector = new PMPServiceConnector(this);
	pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {

	    @Override
	    public void onConnect(AbstractConnector connector)
		    throws RemoteException {

		// Check if the service is registered yet
		if (!pmpconnector.getAppService()
			.isRegistered(getPackageName())) {
		    pmpconnector.getAppService().registerApp(getPackageName());
		} else {
		    Log.v("App registered");
		}
	    }

	    @Override
	    public void onBindingFailed(AbstractConnector connector) {
		Log.e("ERROR while registering");
	    }
	});

	// Connect to the service
	pmpconnector.bind();

    }

    @Override
    public void onResume() {
	super.onResume();
	// Get the service level
	TextView sl = (TextView) findViewById(R.id.servicelevel);
	String serviceFeature = "";
	for (int itr = 0; itr < 5; itr++) {
	    Boolean sf = ((App) getApplication()).isServiceFeatureEnabled("SF"
		    + itr);
	    serviceFeature = serviceFeature + " SF" + itr + "=" + sf + "\n";
	}
	sl.setText(serviceFeature);
    }
}