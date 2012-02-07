package de.unistuttgart.ipvs.pmp.jpmpps.io.request;

import java.io.Serializable;

/**
 * AbstactClass for all requests.
 * 
 * @author Jakob Jarosch
 */
public abstract class AbstractRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private byte[] cacheHash;
    
    
    public void setCacheHash(byte[] hash) {
        this.cacheHash = hash;
    }
    
    
    public byte[] getCacheHash() {
        return this.cacheHash;
    }
}
