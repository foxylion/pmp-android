package de.unistuttgart.ipvs.pmp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.service.app.AppService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * This service encapsulates all the dirty signee stuff necessary for
 * authentication of services.
 * 
 * This service requires several informations in the intent, used to bind the
 * {@link PMPService} or the {@link AppService}, put as extra into the
 * {@link Intent}. </p>
 * 
 * <pre>
 * intent.putExtraString(Constants.INTENT_IDENTIFIER, &lt;App/PMP-Identifier>);
 * intent.putExtraString(Constants.INTENT_TYPE, &lt;APP|PMP>);
 * intent.putExtraByteArray(Constants.INTENT_SIGNATURE, PMPSignee signed identifier);
 * </pre>
 * 
 * *
 * <p>
 * Use {@link PMPSignee#signContent(byte[])} with the identifier you're sending
 * the Intent to (likely targetIdentifier.getBytes()). Transmit the result of
 * the signing in "signee".
 * </p>
 * 
 * <p>
 * If you need to have access to a correct copy (one which changes will be
 * reflected in the service and vice versa) create one and override
 * {@link PMPSignedService#createSignee()} to copy that signee to this service (return the
 * signee). Note that signees should be consistently the same object!
 * </p>
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class PMPSignedService extends Service {

    /**
     * The signee used for this service.
     */
    private PMPSignee signee;

    /**
     * Overwrite this method to use your own signee object.
     * 
     * @return a reference to a signee for the service to use.
     */
    protected abstract PMPSignee createSignee();

    @Override
    public void onCreate() {
	this.signee = createSignee();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public final IBinder onBind(Intent intent) {
	PMPComponentType boundType = (PMPComponentType) intent
		.getSerializableExtra(Constants.INTENT_TYPE);
	String boundIdentifier = intent
		.getStringExtra(Constants.INTENT_IDENTIFIER);
	byte[] boundSignature = intent
		.getByteArrayExtra(Constants.INTENT_SIGNATURE);

	if ((boundSignature != null)
		&& (signee.isSignatureValid(boundType, boundIdentifier,
			getClass().getName().getBytes(), boundSignature))) {
	    return onSignedBind(intent);
	} else {
	    return onUnsignedBind(intent);
	}
    }

    /**
     * 
     * @return the signee used for signing messages.
     */
    public final PMPSignee getSignee() {
	return this.signee;
    }

    /**
     * onBind() callback for all trusted and correctly signed messages.
     * 
     * @see {@link Service#onBind(Intent)}
     */
    public abstract IBinder onSignedBind(Intent intent);

    /**
     * onBind() callback for all messages of unknown origin.
     * 
     * @see {@link Service#onBind(Intent)}
     */
    public abstract IBinder onUnsignedBind(Intent intent);

}
