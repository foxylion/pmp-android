package de.unistuttgart.ipvs.pmp.app.appUtil.connector;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.AppApplication;
import de.unistuttgart.ipvs.pmp.service.app.AppService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;
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
	PMPSignee signee = new PMPSignee(PMPComponentType.APP,
		AppService.class, context);
	
	signee.setIdentifier(appServiceIdentifier);
	AppApplication.getInstance().setSignee(signee);
	
	PMPServiceConnector serviceCon = new PMPServiceConnector(context,
		signee);
	serviceCon.addCallbackHandler(new AppConnectorCallback());
	AppApplication.getInstance().setServiceConnector(serviceCon);

	Log.v(context.getPackageName() + " tries to connect to PMP service");
	if (!serviceCon.bind()) {
	    Log.e(context.getPackageName() + " failed binding");
	}
    }

}
