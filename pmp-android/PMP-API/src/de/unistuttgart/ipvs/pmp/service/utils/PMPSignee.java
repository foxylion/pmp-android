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
 * Signature helper class to handle all the signed messages in PMP for a
 * specific {@link PMPComponentType} and {@link PMPSignedService} using an
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

    /**
     * Separator between type and identifier.
     */
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
     * The type of the signee
     */
    private PMPComponentType type;
    
    /**
     * The service class the signee should use
     */
    private Class<? extends PMPSignedService> serviceClass;
    
    /**
     * The android:name identifier for this signee
     */
    private String identifier;

    /**
     * Creates a new PMPSignee ready for mayhem.
     * 
     * @param type
     *            the type of the signee
     * @param serviceClass
     *            the service class the signee should use
     */
    public PMPSignee(PMPComponentType type,
	    Class<? extends PMPSignedService> serviceClass) {
	this.remotePublicKeys = new HashMap<String, PublicKey>();
	this.type = type;
	this.serviceClass = serviceClass;

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
     * @param boundType
     *            the type of the remote sender
     * @param boundIdentifier
     *            the identifier of the remote sender
     * @param remotePublicKey
     *            the public key belonging to the identifier
     */
    public void setRemotePublicKey(PMPComponentType boundType,
	    String boundIdentifier, byte[] remotePublicKey) {

	try {
	    KeyFactory kf = KeyFactory.getInstance(ALGORITHM_KEY);
	    X509EncodedKeySpec x509eks = new X509EncodedKeySpec(remotePublicKey);
	    this.remotePublicKeys.put(boundType + TYPE_IDENTIFIER_SEPARATOR
		    + boundIdentifier, kf.generatePublic(x509eks));

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
     * @param boundIdentifier
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
    public boolean isSignatureValid(PMPComponentType boundType,
	    String boundIdentifier, byte[] content, byte[] signature) {
	// check for nulls
	if ((local == null) || (remotePublicKeys == null) || (content == null)
		|| (signature == null)) {
	    return false;
	}

	// fetch public key for remoteIdentifier
	PublicKey pk = remotePublicKeys.get(boundType
		+ TYPE_IDENTIFIER_SEPARATOR + boundIdentifier);
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
    public final void load(Context context) {
	// load signature, if exists
	try {
	    InputStream is = context.openFileInput(serviceClass.getName());
	    readFromInput(is);
	} catch (FileNotFoundException e) {
	    Log.v("Signature file for " + serviceClass.getName() + " not found.");
	} catch (IOException e) {
	    Log.e(e.toString() + " during loading signature for "
		    + serviceClass.getName());
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
    public final void save(Context context) {
	// save signature
	try {
	    OutputStream os = context.openFileOutput(serviceClass.getName(),
		    Context.MODE_PRIVATE);
	    writeToOutput(os);
	} catch (FileNotFoundException e) {
	    Log.v("Signature file for " + serviceClass.getName()
		    + " not found (during writing?!).");
	} catch (IOException e) {
	    Log.e(e.toString() + " during writing signature for "
		    + serviceClass.getName());
	}
    }

    /**
     * 
     * @return the type of this signee
     */
    public PMPComponentType getType() {
	return this.type;
    }
    
    /**
     * 
     * @param androidName the android:name identifier of this signee
     */
    public void setIdentifier(String androidName) {
	this.identifier = androidName;
    }

    /**
     * 
     * @return the identifier of this signee
     */
    public String getIdentifier() {
	return this.identifier;
    }


}
