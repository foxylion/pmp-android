/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * Signature helper class to handle all the signed messages in PMP for a specific {@link PMPComponentType} and
 * {@link PMPSignedService} using an asymmetric crypto system. It should be thread-safe in a way that the safety is
 * ensured. This means if one operation is performed on the signee instance, no other operation can be performed until
 * the first one has completed. How good this works with the liveness property is unknown. It will automatically save on
 * changes.
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
     * The android:name identifier for this signee
     */
    private String identifier;
    
    /**
     * The context of this signee (used for saving & loading)
     */
    private Context context;
    
    
    /**
     * Creates a new PMPSignee ready for mayhem.
     * 
     * @param type
     *            the type of the signee
     * @param androidName
     *            the android:name identifier of this signee
     * @param context
     *            the context this signee should use to save
     */
    public PMPSignee(PMPComponentType type, String androidName, Context context) {
        this.remotePublicKeys = new HashMap<String, PublicKey>();
        this.type = type;
        this.context = context;
        this.identifier = androidName;
        
        // try loading an old state
        if (!load()) {
            
            // if that fails, generate new ones
            try {
                // generate keys
                KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM_KEY);
                kpg.initialize(1024);
                this.local = kpg.generateKeyPair();
                save();
            } catch (NoSuchAlgorithmException e) {
                Log.e("Algorithm " + ALGORITHM_KEY + " was not supported.", e);
                this.local = null;
            }
        }
    }
    
    
    /**
     * Returns the local public key to transmit it to a different, remote {@link PMPSignee}.
     * 
     * @return the local public key, null if the initialization was faulty
     */
    public synchronized byte[] getLocalPublicKey() {
        if (this.local == null) {
            Log.e("PMPSignee tried to fetch local public key, but had null values present.");
            return null;
        } else {
            return this.local.getPublic().getEncoded();
        }
    }
    
    
    /**
     * <p>
     * Sets the remote public key fetched from a different, remote {@link PMPSignee} that is identified by identifier.
     * </p>
     * 
     * <p>
     * <b>Attention:</b> Only call this method if you are sure the source is valid! This method is a likely target for
     * attackers.
     * </p>
     * 
     * <p>
     * Make sure that remoteType always has the correct type from Constants.* specified and not a value that the remote
     * can freely set!
     * </p>
     * 
     * @param boundType
     *            the type of the remote sender
     * @param boundIdentifier
     *            the identifier of the remote sender
     * @param remotePublicKey
     *            the public key belonging to the identifier
     * @throws NullPointerException
     *             if one of the supplied arguments was null
     */
    public synchronized void setRemotePublicKey(PMPComponentType boundType, String boundIdentifier,
            byte[] remotePublicKey) {
        
        if ((boundType == null) || (boundIdentifier == null) || (remotePublicKey == null)) {
            throw new NullPointerException();
        }
        
        try {
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM_KEY);
            X509EncodedKeySpec x509eks = new X509EncodedKeySpec(remotePublicKey);
            this.remotePublicKeys.put(boundType + TYPE_IDENTIFIER_SEPARATOR + boundIdentifier,
                    kf.generatePublic(x509eks));
            save();
            
        } catch (NoSuchAlgorithmException e) {
            Log.e("Algorithm " + ALGORITHM_KEY + " was not supported.", e);
        } catch (InvalidKeySpecException e) {
            Log.e("Key was invalid.", e);
        }
    }
    
    
    /**
     * <p>
     * Gets the remote public key for a different, remote {@link PMPSignee} that is identified by identifier.
     * </p>
     * 
     * @param boundType
     *            the type of the remote sender
     * @param boundIdentifier
     *            the identifier of the remote sender
     * @return the public key belonging to the identifier, or null if none present
     */
    public synchronized byte[] getRemotePublicKey(PMPComponentType boundType, String boundIdentifier) {
        // check for nulls
        if (this.remotePublicKeys == null) {
            Log.e("PMPSignee ordered to fetch a remote public key for " + boundType.toString()
                    + TYPE_IDENTIFIER_SEPARATOR + boundIdentifier + ", but had null values present.");
            return null;
        }
        PublicKey pk = this.remotePublicKeys.get(boundType + TYPE_IDENTIFIER_SEPARATOR + boundIdentifier);
        return pk != null ? pk.getEncoded() : null;
    }
    
    
    /**
     * <p>
     * Removes the remote public key for a different, remote {@link PMPSignee} that is identified by identifier.
     * </p>
     * 
     * @param boundType
     *            the type of the remote sender
     * @param boundIdentifier
     *            the identifier of the remote sender
     */
    public synchronized void removeRemotePublicKey(PMPComponentType boundType, String boundIdentifier) {
        if (this.remotePublicKeys == null) {
            Log.e("PMPSignee ordered to fetch a remote public key for " + boundType.toString()
                    + TYPE_IDENTIFIER_SEPARATOR + boundIdentifier + ", but had null values present.");
            return;
        }
        this.remotePublicKeys.put(boundType + TYPE_IDENTIFIER_SEPARATOR + boundIdentifier, null);
        save();
    }
    
    
    /**
     * Removes all remote public keys which are currently present from this signee.
     */
    public synchronized void clearRemotePublicKeys() {
        this.remotePublicKeys = new HashMap<String, PublicKey>();
        save();
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
     * @return true, iff all values are valid (i.e. not null, correct key) and the signature fits the content. Therefore
     *         false, if the initialization was faulty, any value is null or the signature is invalid. Also false, if no
     *         public key was set for remoteIdentifier.
     */
    public synchronized boolean isSignatureValid(PMPComponentType boundType, String boundIdentifier, byte[] content,
            byte[] signature) {
        // check for nulls
        if ((this.local == null) || (this.remotePublicKeys == null) || (content == null) || (signature == null)) {
            Log.e("PMPSignee tried to verify a signature from " + boundType.toString() + TYPE_IDENTIFIER_SEPARATOR
                    + boundIdentifier + ", but had null values present.");
            return false;
        }
        
        // fetch public key for remoteIdentifier
        PublicKey pk = this.remotePublicKeys.get(boundType + TYPE_IDENTIFIER_SEPARATOR + boundIdentifier);
        if (pk == null) {
            Log.e("PMPSignee tried to verify a signature from " + boundType.toString() + TYPE_IDENTIFIER_SEPARATOR
                    + boundIdentifier + ", but did not know the remote.");
            return false;
        }
        
        try {
            // actual signature check
            Signature sg = Signature.getInstance(ALGORITHM_SIGNATURE);
            sg.initVerify(pk);
            sg.update(content);
            boolean result = sg.verify(signature);
            Log.d("PMPSignee found signature " + (result ? "valid" : "INVALID") + " for remote " + boundType.toString()
                    + TYPE_IDENTIFIER_SEPARATOR + boundIdentifier + ", expecting content \"" + new String(content)
                    + "\".");
            return result;
            
        } catch (NoSuchAlgorithmException e) {
            Log.e("Algorithm " + ALGORITHM_SIGNATURE + " was not supported.", e);
        } catch (InvalidKeyException e) {
            Log.e("Key was invalid.", e);
        } catch (SignatureException e) {
            Log.e("Signature was not prepared.", e);
        }
        
        return false;
    }
    
    
    /**
     * Creates a signature for a given content.
     * 
     * @param content
     *            the content to be signed
     * @return the signature for content, or null if the initialization was faulty
     */
    public synchronized byte[] signContent(byte[] content) {
        // null check
        if ((this.local == null) || (content == null)) {
            Log.e("PMPSignee tried to sign content, but had null values present.");
            return null;
        }
        
        try {
            // actual signing
            Signature sg = Signature.getInstance(ALGORITHM_SIGNATURE);
            sg.initSign(this.local.getPrivate());
            sg.update(content);
            Log.d(toString() + " is signing content \"" + new String(content) + "\" with local private key");
            return sg.sign();
            
        } catch (NoSuchAlgorithmException e) {
            Log.e("Algorithm " + ALGORITHM_SIGNATURE + " was not supported.", e);
        } catch (InvalidKeyException e) {
            Log.e("Key was invalid.", e);
        } catch (SignatureException e) {
            Log.e("Signature was not prepared.", e);
        }
        
        return null;
    }
    
    
    /**
     * Writes the whole signee set to an {@link OutputStream}.
     * 
     * @param os
     * @throws IOException
     */
    private final void writeToOutput(OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(this.local);
        oos.writeObject(this.remotePublicKeys);
        oos.close();
    }
    
    
    @SuppressWarnings("unchecked")
    /**
     * Reads the whole signee set out of an {@link InputStream}.
     * @param is
     * @throws IOException
     */
    private final void readFromInput(InputStream is) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(is);
        
        try {
            this.local = (KeyPair) ois.readObject();
            this.remotePublicKeys = (Map<String, PublicKey>) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException e) {
            Log.e("Class not found during load.", e);
        }
        
    }
    
    
    /**
     * Loads the content of a signee from a file in context for the service service. Pay close attention from where you
     * load, attackers could try to get there!
     * 
     * @return true, iff the file was succesfully loaded
     */
    public synchronized final boolean load() {
        if (this.context == null) {
            Log.e(toString() + " had context = null for loading.");
            return false;
        }
        
        // load signee, if exists
        try {
            InputStream is = this.context.openFileInput(getIdentifier());
            readFromInput(is);
            is.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.v("Signee file for " + getIdentifier() + " not found.", e);
        } catch (IOException e) {
            Log.e(e.toString() + " during loading Signee for " + getIdentifier(), e);
        } catch (UnsupportedOperationException e) {
            Log.e(e.toString() + " during loading Signee for " + getIdentifier(), e);
        }
        return false;
    }
    
    
    /**
     * Saves the content of a signee from a file in context for the service service. Pay close attention where you save,
     * attackers could try to get there!
     */
    public synchronized final void save() {
        if (this.context == null) {
            Log.e(toString() + " had context = null for saving.");
            return;
        }
        
        // save signee
        try {
            OutputStream os = this.context.openFileOutput(getIdentifier(), Context.MODE_PRIVATE);
            writeToOutput(os);
            os.close();
        } catch (FileNotFoundException e) {
            Log.v("Signee file for " + getIdentifier() + " not found (during writing?!).", e);
        } catch (IOException e) {
            Log.e(e.toString() + " during writing signee for " + getIdentifier(), e);
        } catch (UnsupportedOperationException e) {
            Log.e(e.toString() + " during loading Signee for " + getIdentifier(), e);
        }
    }
    
    
    /**
     * 
     * @return the type of this signee
     */
    public synchronized PMPComponentType getType() {
        return this.type;
    }
    
    
    /**
     * 
     * @param androidName
     *            the android:name identifier of this signee
     */
    public synchronized void setIdentifier(String androidName) {
        this.identifier = androidName;
    }
    
    
    /**
     * 
     * @return the identifier of this signee
     */
    public synchronized String getIdentifier() {
        return this.identifier;
    }
    
    
    /**
     * 
     * @return the context of this signee
     */
    public synchronized Context getContext() {
        return this.context;
    }
    
}
