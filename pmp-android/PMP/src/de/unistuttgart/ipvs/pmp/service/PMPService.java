package de.unistuttgart.ipvs.pmp.service;

import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceResourceGroup;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * External service for communication between PMP, the Resources and Apps. <br/>
 * The Service requires several informations in the intent, used to bind the
 * {@link PMPService}, put as extra into the {@link Intent}.
 * 
 * <pre>
 * intent.putExtraString(Constants.INTENT_TYPE, PMPComponentType.*.toString());
 * intent.putExtraString(Constants.INTENT_IDENTIFIER, &lt;App/ResourceGroup-Identifier>);
 * intent.putExtraByteArray("signature", PMPSignature signing PMPService identifier);
 * </pre>
 * 
 * The signature is optional, if you do not sent a signature, the Service will
 * handle the binding as an registration and gives back the
 * {@link IPMPServiceRegistration} Binder.<br/>
 * With a valid token the {@link IPMPServiceResourceGroup} or
 * {@link IPMPServiceApp} Binder will be given back.
 * 
 * If an authentification fails the Service will give back NULL.
 * 
 * @author Jakob Jarosch
 */
public class PMPService extends PMPSignedService {

    @Override
    public IBinder onSignedBind(Intent intent) {
	PMPComponentType type = (PMPComponentType) intent
		.getSerializableExtra(Constants.INTENT_TYPE);
	String identifier = intent.getStringExtra(Constants.INTENT_IDENTIFIER);

	/* Should be a normal authentification */
	if (type.equals(PMPComponentType.APP)) {
	    return new PMPServiceAppStubImpl(identifier);
	} else if (type.equals(PMPComponentType.RESOURCE_GROUP)) {
	    return new PMPServiceResourceGroupStubImpl(identifier);
	} else {
	    /* no valid type identifier found */
	    return null;
	}
    }

    @Override
    public IBinder onUnsignedBind(Intent intent) {
	String identifier = intent.getStringExtra(Constants.INTENT_IDENTIFIER);
	return new PMPServiceRegistrationStubImpl(identifier);
    }

    @Override
    protected PMPSignee createSignee() {
	return PMPApplication.getSignee();
    }

}