package de.unistuttgart.ipvs.pmp.model.implementations.utils;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupAccess;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupAccessHeader;
import de.unistuttgart.ipvs.pmp.service.utils.AppServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

public class ServiceLevelPublisher {

    private IApp app;
    private IServiceLevel oldServiceLevel;

    public ServiceLevelPublisher(IApp app, IServiceLevel oldServiceLevel) {
	this.app = app;
	this.oldServiceLevel = oldServiceLevel;
    }

    public void publish(boolean asynchronous) {
	if (asynchronous) {
	    Log.v("Starting ServiceLevel publishing as asynchronous thread");
	    new Thread(new Runnable() {

		@Override
		public void run() {
		    executePublishing();
		}
	    }).start();
	} else {
	    Log.v("Starting ServiceLevel publishing");
	    executePublishing();
	}
    }

    private void executePublishing() {
	if (oldServiceLevel.getLevel() < app.getActiveServiceLevel().getLevel()) {
	    publishToApp();
	    publishToResourceGroups();
	} else {
	    publishToResourceGroups();
	    publishToApp();
	}

    }

    private void publishToResourceGroups() {
	Log.v("ResoureceGroup-AccessSet-Publishing now starts");

	IResourceGroup[] affectedResourceGroups = app
		.getAllResourceGroupsUsedByServiceLevels();

	for (IResourceGroup affectedResourceGroup : affectedResourceGroups) {
	    Log.v("ResoureceGroup-AccessSet-Publishing ("
		    + affectedResourceGroup.getIdentifier()
		    + "): AccessSet is beeing created");

	    List<ResourceGroupAccess> accesses = new ArrayList<ResourceGroupAccess>();

	    for (IApp app : affectedResourceGroup
		    .getAllAppsUsingThisResourceGroup()) {
		byte[] publicKey = PMPApplication.getSignee().getPublicKeyFor(
			PMPComponentType.APP, app.getIdentifier());

		Bundle privacyLevels = new Bundle();

		for (IPrivacyLevel appPl : app
			.getAllPrivacyLevelsUsedByActiveServiceLevel(affectedResourceGroup)) {
		    privacyLevels.putString(appPl.getIdentifier(),
			    appPl.getValue());
		}

		ResourceGroupAccessHeader header = new ResourceGroupAccessHeader(
			app.getIdentifier(), publicKey);

		ResourceGroupAccess access = new ResourceGroupAccess(header,
			privacyLevels);

		accesses.add(access);
	    }

	    /*
	     * Publish the Accesses to the ResourceGroupService.
	     */
	    Log.v("ResoureceGroup-AccessSet-Publishing ("
		    + affectedResourceGroup.getIdentifier()
		    + "): AccessSet is beeing published");

	    ResourceGroupServiceConnector rgsc = new ResourceGroupServiceConnector(
		    PMPApplication.getContext(), PMPApplication.getSignee(),
		    affectedResourceGroup.getIdentifier());

	    if (!rgsc.bind(true)) {
		Log.e("ResoureceGroup-AccessSet-Publishing ("
			+ affectedResourceGroup.getIdentifier()
			+ "): FAILED - Connection to the AppService failed. More details can be found in the log.");
	    } else {
		Log.d("ResoureceGroup-AccessSet-Publishing ("
			+ affectedResourceGroup.getIdentifier()
			+ "): Successfully bound.");

		if (rgsc.getAppService() == null) {
		    Log.e("ResoureceGroup-AccessSet-Publishing ("
			    + affectedResourceGroup.getIdentifier()
			    + "): FAILED - Binding to the AppService failed, only got a NULL IBinder.");
		} else {
		    Log.d("ResoureceGroup-AccessSet-Publishing ("
			    + affectedResourceGroup.getIdentifier()
			    + "): Successfully connected, got IResourceGroupServicePMP.");

		    try {
			Log.v("Calling setAccesses()...");
			rgsc.getPMPService()
				.setAccesses(
					accesses.toArray(new ResourceGroupAccess[accesses
						.size()]));
			Log.d("ResoureceGroup-AccessSet-Publishing ("
				+ affectedResourceGroup.getIdentifier()
				+ "): Successfully set new Accesses.");
		    } catch (RemoteException e) {
			Log.e("ResoureceGroup-AccessSet-Publishing ("
				+ affectedResourceGroup.getIdentifier()
				+ "): FAILED - got a RemoteException, by calling setAccesses()",
				e);
		    }
		}
	    }
	}
    }

    private void publishToApp() {
	Log.v("ServiceLevel-Publishing (" + app.getIdentifier()
		+ "): publishing ServiceLevel...");

	AppServiceConnector asc = new AppServiceConnector(
		PMPApplication.getContext(), PMPApplication.getSignee(),
		app.getIdentifier());

	if (!asc.bind(true)) {
	    Log.e("ServiceLevel-Publishing ("
		    + app.getIdentifier()
		    + "): FAILED - Connection to the AppService failed. More details can be found in the log.");
	} else {
	    Log.d("ServiceLevel-Publishing (" + app.getIdentifier()
		    + "): Successfully bound.");

	    if (asc.getAppService() == null) {
		Log.e("ServiceLevel-Publishing ("
			+ app.getIdentifier()
			+ "): FAILED - Binding to the AppService failed, only got a NULL IBinder.");
	    } else {
		Log.d("ServiceLevel-Publishing (" + app.getIdentifier()
			+ "): Successfully connected, got IAppServicePMP.");

		try {
		    Log.v("Calling setActiveServiceLevel()...");
		    asc.getAppService().setActiveServiceLevel(
			    app.getActiveServiceLevel().getLevel());
		    Log.d("ServiceLevel-Publishing (" + app.getIdentifier()
			    + "): Successfully set new ServiceLevel.");
		} catch (RemoteException e) {
		    Log.e("ServiceLevel-Publishing ("
			    + app.getIdentifier()
			    + "): FAILED - got a RemoteException, by calling setActiveServiceLevel()",
			    e);
		}
	    }
	}
    }
}
