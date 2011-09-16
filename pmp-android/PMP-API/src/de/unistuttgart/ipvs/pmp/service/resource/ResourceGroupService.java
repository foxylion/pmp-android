package de.unistuttgart.ipvs.pmp.service.resource;

import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupApp;
import de.unistuttgart.ipvs.pmp.service.NullServiceStubImpl;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
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
    protected PMPSignee createSignee() {
	ResourceGroup rg = findContextResourceGroup();
	if (rg == null) {
	    // invalid context
	    Log.e(this.toString()
		    + " tried to connect to its resource group and failed.");
	    // die with NullPointerExceptions instead of confusing everyone
	    return null;
	} else {
	    return rg.getSignee();
	}
    }

    /**
     * Called on startup of the service.
     */
    @Override
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
	    return new NullServiceStubImpl();
	}

	PMPComponentType boundType = (PMPComponentType) intent
		.getSerializableExtra(Constants.INTENT_TYPE);
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
	    return new NullServiceStubImpl();
	}
    }

    @Override
    public IBinder onUnsignedBind(Intent intent) {
	// go away, I don't like you!
	return new NullServiceStubImpl();
    }

    private ResourceGroup findContextResourceGroup() {
	if (!(getApplication() instanceof ResourceGroupApp)) {
	    Log.e("ResourceGroupService finds its app to be " + getApplication().toString() + ", should be ResourceGroupApp");
	    return null;
	} else {
	    ResourceGroupApp rga = (ResourceGroupApp) getApplication();
	    return rga.getResourceGroupForService(this);
	}
    }
}
