package de.unistuttgart.ipvs.pmp.resource;

import java.io.Serializable;

public class ResourceGroupAccessHeader implements Serializable {
    
    private static final long serialVersionUID = -7482979934059030704L;
    
    private String identifier;
    private byte[] publicKey;
    
    
    /**
     * Creates a new {@link ResourceGroupAccessHeader}.
     * 
     * @param identifier
     *            identifier of the app
     * @param publicKey
     *            public key of the app
     */
    public ResourceGroupAccessHeader(String identifier, byte[] publicKey) {
        this.identifier = identifier;
        this.publicKey = publicKey.clone();
    }
    
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    public byte[] getPublicKey() {
        return this.publicKey.clone();
    }
}
