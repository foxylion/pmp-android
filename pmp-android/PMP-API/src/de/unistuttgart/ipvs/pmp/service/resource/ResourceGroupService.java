package de.unistuttgart.ipvs.pmp.service.resource;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;
import de.unistuttgart.ipvs.pmp.service.helper.PMPSignature;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * External service for communication between Resources and or PMP the Apps. <br/>
 * The Service requires several informations in the intent, used to bind the
 * {@link PMPService}, put as extra into the {@link Intent}.
 * 
 * <pre>
 * intent.putExtraString("identifier", &lt;App/PMP-Identifier>);
 * intent.putExtraString("type", &lt;APP|PMP>);
 * intent.putExtraByteArray("resgrpSig", PMPSignature signing identifier);
 * </pre>
 * 
 * With a valid token the {@link IResourceGroupServicePMP} or !!CREATE
 * ALTERNATIVE BINDER (AIDL)!! Binder will be given back.
 * 
 * If an authentification fails the Service will give back NULL.
 * 
 * @author Tobias Kuhn
 */
public class ResourceGroupService extends Service {

    private PMPSignature resgrpSig;

    /**
     * On creation of service called (only once).
     */
    @Override
    public void onCreate() {
	resgrpSig = new PMPSignature();
    }

    /**
     * Called when service is going to shutdown.
     */
    @Override
    public void onDestroy() {

    }

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

    /**
     * Called when another application is going to bind the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
	String type = intent.getStringExtra(Constants.INTENT_TYPE);
	String identifier = intent.getStringExtra(Constants.INTENT_IDENTIFIER);
	byte[] signature = intent.getByteArrayExtra(Constants.INTENT_SIGNATURE);

	if (resgrpSig.isSignatureValid(identifier, ResourceGroupService.class
		.toString().getBytes(), signature)) {
	    // this caller is authorized correctly
	
	    if (type.equals(Constants.TYPE_PMP)) {
		return new ResourceGroupServicePMPStubImpl();
		
	    } else if (type.equals(Constants.TYPE_APP)) {
		return new ResourceGroupServiceAppStubImpl();
		
	    } else {
		// wait, what?
		return null;
		
	    }

	} else {
	    // registration required
	    ResourceGroupServiceRegisterStubImpl rgsrsi = new ResourceGroupServiceRegisterStubImpl();
	    rgsrsi.setIdentifier(identifier);
	    rgsrsi.setSignature(resgrpSig);
	    return rgsrsi;

	}

    }
}
