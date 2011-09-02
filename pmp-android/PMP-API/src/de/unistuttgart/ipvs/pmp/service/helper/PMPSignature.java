package de.unistuttgart.ipvs.pmp.service.helper;

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

import de.unistuttgart.ipvs.pmp.Log;

/**
 * Signature helper class to handle all the signed messages in PMP using an
 * asymmetric crypto system.
 * 
 * TODO: TEST THIS! It will be only of vital importance to PMP...
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPSignature {

    /**
     * Algorithm to be used for the keys.
     */
    private final static String ALGORITHM_KEY = "RSA";

    /**
     * Algorithm to be used for the signatures.
     */
    private final static String ALGORITHM_SIGNATURE = "SHA1withRSA";

    /**
     * The local key pair
     */
    private KeyPair local;

    /**
     * The remote public keys
     */
    private Map<String, PublicKey> remotePublicKeys;

    /**
     * Creates a new PMPSignature ready for mayhem.
     */
    public PMPSignature() {
	remotePublicKeys = new HashMap<String, PublicKey>();

	try {
	    // generate keys
	    KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM_KEY);
	    kpg.initialize(1024);
	    local = kpg.generateKeyPair();
	} catch (NoSuchAlgorithmException e) {
	    Log.e("Algorithm " + ALGORITHM_KEY + " was not supported.");
	    local = null;
	}
    }

    /**
     * Returns the local public key to transmit it to a different, remote
     * {@link PMPSignature}.
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
     * Sets the remote public key fetched from a different, remote
     * {@link PMPSignature} that is identified by identifier.
     * 
     * @param remoteIdentifier
     *            the identifier of the remote sender
     * @param remotePublicKey
     *            the public key belonging to the identifier
     */
    public void setRemotePublicKey(String remoteIdentifier,
	    byte[] remotePublicKey) {

	try {
	    KeyFactory kf = KeyFactory.getInstance(ALGORITHM_KEY);
	    X509EncodedKeySpec x509eks = new X509EncodedKeySpec(remotePublicKey);
	    this.remotePublicKeys.put(remoteIdentifier,
		    kf.generatePublic(x509eks));

	} catch (NoSuchAlgorithmException e) {
	    Log.e("Algorithm " + ALGORITHM_KEY + " was not supported.");
	} catch (InvalidKeySpecException e) {
	    Log.e("Key was invalid.");
	}
    }

    /**
     * Checks whether a signature is valid.
     * 
     * @param remoteIdentifier
     *            the identifier of the remote sender
     * @param content
     *            the content of the signature.
     * @param signature
     *            the signature with which content was signed.
     * @return true, iff all values are valid (i.e. not null, correct key) and
     *         the signature fits the content. Therefore false, if the
     *         initialization was faulty, any value is null or the signature is
     *         invalid. Also false, if no public key was set for remoteIdentifier.
     */
    public boolean isSignatureValid(String remoteIdentifier, byte[] content,
	    byte[] signature) {
	// check for nulls
	if ((local == null) || (remotePublicKeys == null) || (content == null)
		|| (signature == null)) {
	    return false;
	}
	
	// fetch public key for remoteIdentifier
	PublicKey pk = remotePublicKeys.get(remoteIdentifier);
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

}
