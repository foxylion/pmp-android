package de.unistuttgart.ipvs.pmp.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.service.app.AppService;
import de.unistuttgart.ipvs.pmp.service.helper.PMPSignature;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * This service encapsulates all the dirty signature stuff necessary for
 * authentification of services.
 * 
 * This service requires several informations in the intent, used to bind the
 * {@link PMPService} or the {@link AppService}, put as extra into the
 * {@link Intent}. </p>
 * 
 * <pre>
 * intent.putExtraString("identifier", &lt;App/PMP-Identifier>);
 * intent.putExtraString("type", &lt;APP|PMP>);
 * intent.putExtraByteArray("signature", PMPSignature signing identifier);
 * </pre>
 * 
 * 
 * <p>
 * Use {@link PMPSignature#signContent(byte[])} with the identifier you're
 * sending the Intent to (likely
 * YourService.class.getClass().getName().getBytes()). Transmit the result of
 * the signing in "signature".
 * </p>
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class PMPSignedService extends Service {

    /**
     * The signature used for this service.
     */
    private final PMPSignature signature = new PMPSignature();

    /**
     * the type string from the last onBind()
     */
    private String boundType;

    /**
     * the identifier string from the last onBind()
     */
    private String boundIdentifier;

    /**
     * the signature of the bind from the last onBind()
     */
    private byte[] boundSignature;

    @Override
    public void onCreate() {
	// load signature, if exists
	try {
	    InputStream is = openFileInput(getClass().getName());
	    signature.readFromInput(is);
	} catch (FileNotFoundException e) {
	    Log.v("Signature file for " + getClass().getName() + " not found.");
	} catch (IOException e) {
	    Log.e(e.toString() + " during loading signature for "
		    + getClass().getName());
	}
    }

    @Override
    public void onDestroy() {
	// save signature, if exists
	try {
	    OutputStream os = openFileOutput(getClass().getName(),
		    Context.MODE_PRIVATE);
	    signature.writeToOutput(os);
	} catch (FileNotFoundException e) {
	    Log.v("Signature file for " + getClass().getName()
		    + " not found (during writing?!).");
	} catch (IOException e) {
	    Log.e(e.toString() + " during writing signature for "
		    + getClass().getName());
	}
    }

    @Override
    public final IBinder onBind(Intent intent) {
	boundType = intent.getStringExtra(Constants.INTENT_TYPE);
	boundIdentifier = intent.getStringExtra(Constants.INTENT_IDENTIFIER);
	boundSignature = intent.getByteArrayExtra(Constants.INTENT_SIGNATURE);

	if (signature.isSignatureValid(boundIdentifier, getClass().getName()
		.getBytes(), boundSignature)) {
	    return onSignedBind(intent);
	} else {
	    return onUnsignedBind(intent);
	}
    }

    protected String getBoundType() {
	return this.boundType;
    }

    protected String getBoundIdentifier() {
	return this.boundIdentifier;
    }

    /**
     * Adds a publicKey for identifier to the {@link PMPSignature}.
     * 
     * @param remoteIdentifier
     * @param remotePublicKey
     */
    protected void addPublicKey(String remoteIdentifier, byte[] remotePublicKey) {
	signature.setRemotePublicKey(remoteIdentifier, remotePublicKey);
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
