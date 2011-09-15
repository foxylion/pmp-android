package de.unistuttgart.ipvs.pmp.app.DEPRECATED.utils.connector;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.DEPRECATED.ApplicationApp;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import android.content.Context;

/**
 * Class that connects to the PMP Service and registers the app
 * 
 * @author Thorsten Berberich
 * 
 */
public class PMPConnector {

    public static void connectToPMP(Context context, String appServiceIdentifier) {
	ApplicationApp.getInstance().setContext(context);
	ApplicationApp.getInstance().setAppServiceIdentifier(
		appServiceIdentifier);

	PMPServiceConnector serviceCon = new PMPServiceConnector(context,
		ApplicationApp.getInstance().getSignee());
	serviceCon.addCallbackHandler(new AppConnectorCallback());
	ApplicationApp.getInstance().setServiceConnector(serviceCon);

	Log.v(context.getPackageName() + " tries to connect to PMP service");
	serviceCon.bind();
    }

}
