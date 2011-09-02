package de.unistuttgart.ipvs.pmp.service.resource;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;
import de.unistuttgart.ipvs.pmp.service.app.AppService;
import de.unistuttgart.ipvs.pmp.service.helper.PMPSignature;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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

    /**
     * Called on startup the service.
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
	if (getBoundType().equals(Constants.TYPE_PMP)) {
	    ResourceGroupServicePMPStubImpl rgspmpsi = new ResourceGroupServicePMPStubImpl();
	    rgspmpsi.setResourceGroup(rg);
	    return rgspmpsi;

	} else if (getBoundType().equals(Constants.TYPE_APP)) {
	    ResourceGroupServiceAppStubImpl rgsasi = new ResourceGroupServiceAppStubImpl();
	    rgsasi.setResourceGroup(rg);
	    rgsasi.setAppIdentifier(getBoundIdentifier());
	    return new ResourceGroupServiceAppStubImpl();

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
}
