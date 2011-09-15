package de.unistuttgart.ipvs.pmp.app.appUtil.connector;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.AppApplication;
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
	AppApplication.getInstance().setContext(context);
	AppApplication.getInstance().setAppServiceIdentifier(appServiceIdentifier);
	
	PMPServiceConnector serviceCon = new PMPServiceConnector(context,
		AppApplication.getInstance().getSignee());
	serviceCon.addCallbackHandler(new AppConnectorCallback());
	AppApplication.getInstance().setServiceConnector(serviceCon);

	Log.v(context.getPackageName() + " tries to connect to PMP service");
	if (!serviceCon.bind()) {
	    Log.e(context.getPackageName() + " failed binding");
	}
    }

}
