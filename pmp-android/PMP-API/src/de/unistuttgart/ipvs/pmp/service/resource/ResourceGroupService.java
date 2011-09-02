package de.unistuttgart.ipvs.pmp.service.resource;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
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
 * This service requires several informations in the intent, used to bind the
 * {@link PMPService} or the {@link AppService}, put as extra into the
 * {@link Intent}.
 * </p>
 * 
 * <pre>
 * intent.putExtraString("identifier", &lt;App/PMP-Identifier>);
 * intent.putExtraString("type", &lt;APP|PMP>);
 * intent.putExtraByteArray("signature", PMPSignature signing identifier);
 * </pre>
 * 
 * <p>
 * On first contact of PMP, you will not need to transmit "signature", but you
 * will only receive an {@link IResourceGroupServiceRegister} binder to transmit
 * your public key fetched from {@link PMPSignature#getLocalPublicKey()} or
 * inform the resource that the registration has failed..
 * </p>
 * 
 * <p>
 * Subsequently, use {@link PMPSignature#signContent(byte[])} with the
 * identifier you're sending the Intent to (likely
 * ResourceGroupService.class.toString().getBytes()). Transmit the result of the
 * signing in "signature".
 * </p>
 * 
 * <p>
 * If you are PMP, you will then receive {@link IResourceGroupServicePMP}; if
 * you are an app authorized by PMP you will receive
 * {@link IResourceGroupServiceApp}.
 * </p>
 * 
 * @author Tobias Kuhn
 */
public class ResourceGroupService extends Service {

    private PMPSignature resgrpSig = null;
    private ResourceGroup rg = null;

    public void assignResourceGroup(ResourceGroup rg) {
	this.rg = rg;
    }

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

	// you have to assign a resource group for this to work, buddy
	if (rg == null) {
	    return null;
	}

	if (resgrpSig.isSignatureValid(identifier, ResourceGroupService.class
		.toString().getBytes(), signature)) {
	    // this caller is authorized correctly

	    if (type.equals(Constants.TYPE_PMP)) {
		ResourceGroupServicePMPStubImpl rgspmpsi = new ResourceGroupServicePMPStubImpl();
		rgspmpsi.setResourceGroup(rg);
		return rgspmpsi;

	    } else if (type.equals(Constants.TYPE_APP)) {
		ResourceGroupServiceAppStubImpl rgsasi = new ResourceGroupServiceAppStubImpl();
		rgsasi.setResourceGroup(rg);
		rgsasi.setAppIdentifier(identifier);
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
