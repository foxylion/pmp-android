package de.unistuttgart.ipvs.pmp.model.server;

/**
 * Storage for a cached download.
 * 
 * @author Tobias Kuhn
 * 
 */
public class CachedDownload {
    
    /**
     * Last access time, i.e. caching time
     */
    private final long lastTime;
    
    /**
     * Actual content of the download
     */
    private final byte[] content;
    
    
    public CachedDownload(long lastTime, byte[] content) {
        this.lastTime = lastTime;
        this.content = content;
    }
    
    
    public boolean isValid(long duration) {
        return this.lastTime + duration <= System.currentTimeMillis();
    }
    
    
    public byte[] getContent() {
        return this.content;
    }
    
}
