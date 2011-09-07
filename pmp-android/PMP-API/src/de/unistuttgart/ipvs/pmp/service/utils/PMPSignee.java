package de.unistuttgart.ipvs.pmp.service.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;

/**
 * Signature helper class to handle all the signed messages in PMP using an
 * asymmetric crypto system. It is not known whether it is thread-safe.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPSignee {

    /**
     * Algorithm to be used for the keys.
     */
    private final static String ALGORITHM_KEY = "RSA";

    /**
     * Algorithm to be used for the signatures.
     */
    private final static String ALGORITHM_SIGNATURE = "SHA1withRSA";

    private final static String TYPE_IDENTIFIER_SEPARATOR = "::";

    /**
     * The local key pair
     */
    private KeyPair local;

    /**
     * The remote public keys
     */
    private Map<String, PublicKey> remotePublicKeys;

    /**
     * Creates a new PMPSignee ready for mayhem.
     */
    public PMPSignee() {
	remotePublicKeys = new HashMap<String, PublicKey>();

	try {
	    // generate keys
	    KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM_KEY);
	    kpg.initialize(4096);
	    local = kpg.generateKeyPair();
	} catch (NoSuchAlgorithmException e) {
	    Log.e("Algorithm " + ALGORITHM_KEY + " was not supported.");
	    local = null;
	}
    }

    /**
     * Returns the local public key to transmit it to a different, remote
     * {@link PMPSignee}.
     * 
     * @return the local public key, null if the initialization was faulty
     */
    public byte[] getLocalPublicKey() {
	if (local == null) {
	    return null;
	} else {
	    return this.local.getPublic().getEncoded();
	}
    }

    /**
     * <p>
     * Sets the remote public key fetched from a different, remote
     * {@link PMPSignee} that is identified by identifier.
     * </p>
     * 
     * <p>
     * <b>Attention:</b> Only call this method if you are sure the source is
     * valid! This method is a likely target for attackers.
     * </p>
     * 
     * <p>
     * Make sure that remoteType always has the correct type from Constants.*
     * specified and not a value that the remote can freely set!
     * </p>
     * 
     * @param remoteType
     *            the type of the remote sender
     * @param remoteIdentifier
     *            the identifier of the remote sender
     * @param remotePublicKey
     *            the public key belonging to the identifier
     */
    public void setRemotePublicKey(PMPComponentType remoteType, String remoteIdentifier,
	    byte[] remotePublicKey) {

	try {
	    KeyFactory kf = KeyFactory.getInstance(ALGORITHM_KEY);
	    X509EncodedKeySpec x509eks = new X509EncodedKeySpec(remotePublicKey);
	    this.remotePublicKeys.put(remoteType + TYPE_IDENTIFIER_SEPARATOR
		    + remoteIdentifier, kf.generatePublic(x509eks));

	} catch (NoSuchAlgorithmException e) {
	    Log.e("Algorithm " + ALGORITHM_KEY + " was not supported.");
	} catch (InvalidKeySpecException e) {
	    Log.e("Key was invalid.");
	}
    }

    /**
     * Checks whether a signature is valid.
     * 
     * @param boundType
     *            the type of the remote sender
     * @param remoteIdentifier
     *            the identifier of the remote sender
     * @param content
     *            the content of the signature.
     * @param signature
     *            the signature with which content was signed.
     * @return true, iff all values are valid (i.e. not null, correct key) and
     *         the signature fits the content. Therefore false, if the
     *         initialization was faulty, any value is null or the signature is
     *         invalid. Also false, if no public key was set for
     *         remoteIdentifier.
     */
    public boolean isSignatureValid(PMPComponentType boundType, String remoteIdentifier,
	    byte[] content, byte[] signature) {
	// check for nulls
	if ((local == null) || (remotePublicKeys == null) || (content == null)
		|| (signature == null)) {
	    return false;
	}

	// fetch public key for remoteIdentifier
	PublicKey pk = remotePublicKeys.get(boundType
		+ TYPE_IDENTIFIER_SEPARATOR + remoteIdentifier);
	if (pk == null) {
	    return false;
	}

	try {
	    // actual signature check
	    Signature sg = Signature.getInstance(ALGORITHM_SIGNATURE);
	    sg.initVerify(pk);
	    sg.update(content);
	    return sg.verify(signature);

	} catch (NoSuchAlgorithmException e) {
	    Log.e("Algorithm " + ALGORITHM_SIGNATURE + " was not supported.");
	} catch (InvalidKeyException e) {
	    Log.e("Key was invalid.");
	} catch (SignatureException e) {
	    Log.e("Signature was not prepared.");
	}

	return false;
    }

    /**
     * Creates a signature for a given content.
     * 
     * @param content
     *            the content to be signed
     * @return the signature for content, or null if the initialization was
     *         faulty
     */
    public byte[] signContent(byte[] content) {
	// null check
	if ((local == null) || (content == null)) {
	    return null;
	}

	try {
	    // actual signing
	    Signature sg = Signature.getInstance(ALGORITHM_SIGNATURE);
	    sg.initSign(local.getPrivate());
	    sg.update(content);
	    return sg.sign();

	} catch (NoSuchAlgorithmException e) {
	    Log.e("Algorithm " + ALGORITHM_SIGNATURE + " was not supported.");
	} catch (InvalidKeyException e) {
	    Log.e("Key was invalid.");
	} catch (SignatureException e) {
	    Log.e("Signature was not prepared.");
	}

	return null;
    }

    /**
     * Writes the whole signature set to an {@link OutputStream}.
     * 
     * @param os
     * @throws IOException
     */
    public void writeToOutput(OutputStream os) throws IOException {
	ObjectOutputStream oos = new ObjectOutputStream(os);
	oos.writeObject(local);
	oos.writeObject(remotePublicKeys);
    }

    @SuppressWarnings("unchecked")
    /**
     * Reads the whole signature set out of an {@link InputStream}.
     * @param is
     * @throws IOException
     */
    public void readFromInput(InputStream is) throws IOException {
	ObjectInputStream ois = new ObjectInputStream(is);

	try {
	    local = (KeyPair) ois.readObject();
	    remotePublicKeys = (Map<String, PublicKey>) ois.readObject();
	} catch (ClassNotFoundException e) {
	    Log.e("Class not found during load.");
	}

    }

    /**
     * Loads the content of a signature from a file in context for the service
     * service. Pay close attention from where you load, attackers could try to
     * get there!
     * 
     * @param context
     *            the context of service
     * @param service
     *            the service for which this {@link PMPSignee} is kept.
     */
    public final void load(Context context,
	    Class<? extends PMPSignedService> service) {
	// load signature, if exists
	try {
	    InputStream is = context.openFileInput(service.getName());
	    readFromInput(is);
	} catch (FileNotFoundException e) {
	    Log.v("Signature file for " + service.getName() + " not found.");
	} catch (IOException e) {
	    Log.e(e.toString() + " during loading signature for "
		    + service.getName());
	}
    }

    /**
     * Saves the content of a signature from a file in context for the service
     * service. Pay close attention where you save, attackers could try to get
     * there!
     * 
     * @param context
     *            the context of service
     * @param service
     *            the context of service
     */
    public final void save(Context context,
	    Class<? extends PMPSignedService> service) {
	// save signature
	try {
	    OutputStream os = context.openFileOutput(getClass().getName(),
		    Context.MODE_PRIVATE);
	    writeToOutput(os);
	} catch (FileNotFoundException e) {
	    Log.v("Signature file for " + getClass().getName()
		    + " not found (during writing?!).");
	} catch (IOException e) {
	    Log.e(e.toString() + " during writing signature for "
		    + getClass().getName());
	}
    }

    public String getType() {
	// TODO Auto-generated method stub
	return null;
    }

    public String getIdentifier() {
	// TODO Auto-generated method stub
	return null;
    }

}
