package de.unistuttgart.ipvs.pmp.service.resource;

import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupApp;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * <h2>Implementing your resource</h2>
 * 
 * To implement your resource, create a {@link ResourceGroupService} and use
 * assignResourceGroup() to assign a {@link ResourceGroup} that stores the data
 * you intent to use for this resource.
 * 
 * <h2>PMP internal view</h2>
 * 
 * <p>
 * This is an external service for communication between resource groups and PMP
 * or apps. <br/>
 * 
 * <p>
 * If you are PMP, you will then receive {@link IResourceGroupServicePMP}; if
 * you are an app authorized by PMP you will receive
 * {@link IResourceGroupServiceApp}.
 * </p>
 * 
 * @author Tobias Kuhn
 */
public class ResourceGroupService extends PMPSignedService {

    @Override
    public void onCreate() {
	super.onCreate();
	ResourceGroup rg = findContextResourceGroup();
	if (rg == null) {
	    // invalid context
	    Log.e(this.toString()
		    + " tried to connect to its resource group and failed.");
	}
    }

    @Override
    protected PMPSignee createSignature() {
	ResourceGroup rg = findContextResourceGroup();
	if (rg == null) {
	    // invalid context
	    Log.e(this.toString()
		    + " tried to connect to its resource group and failed.");
	    // die with NullPointerExceptions instead of confusing everyone
	    return null;
	} else {
	    return rg.getSignature();
	}
    }

    /**
     * Called on startup of the service.
     */
    public int onStartCommand(Intent intent, int flags, int startId) {

	/*
	 * We want this service to continue running until it is explicitly
	 * stopped, so return sticky.
	 */
	return START_STICKY;
    }

    @Override
    public IBinder onSignedBind(Intent intent) {
	ResourceGroup rg = findContextResourceGroup();
	if (rg == null) {
	    // invalid context
	    Log.e(this.toString()
		    + " tried to connect to its resource group and failed.");
	    return null;
	}

	PMPComponentType boundType = PMPComponentType.valueOf(intent.getStringExtra(Constants.INTENT_TYPE));
	String boundIdentifier = intent
		.getStringExtra(Constants.INTENT_IDENTIFIER);

	if (boundType.equals(PMPComponentType.PMP)) {
	    ResourceGroupServicePMPStubImpl rgspmpsi = new ResourceGroupServicePMPStubImpl();
	    rgspmpsi.setResourceGroup(rg);
	    return rgspmpsi;

	} else if (boundType.equals(PMPComponentType.APP)) {
	    ResourceGroupServiceAppStubImpl rgsasi = new ResourceGroupServiceAppStubImpl();
	    rgsasi.setResourceGroup(rg);
	    rgsasi.setAppIdentifier(boundIdentifier);
	    return rgsasi;

	} else {
	    // wait, what?
	    return null;
	}
    }

    @Override
    public IBinder onUnsignedBind(Intent intent) {
	// go away, I don't like you!
	return null;
    }

    private ResourceGroup findContextResourceGroup() {
	if (!(getApplication() instanceof ResourceGroupApp)) {
	    return null;
	} else {
	    ResourceGroupApp rga = (ResourceGroupApp) getApplication();
	    return rga.getResourceGroupForService(this);
	}
    }
}
