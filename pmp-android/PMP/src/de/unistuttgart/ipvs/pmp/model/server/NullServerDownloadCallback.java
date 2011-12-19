package de.unistuttgart.ipvs.pmp.model.server;

/**
 * Null object to do nothing at all but to save if (obj != null) checks.
 * 
 * @author Tobias Kuhn
 * 
 */
public class NullServerDownloadCallback implements IServerDownloadCallback {
    
    protected static NullServerDownloadCallback instance = new NullServerDownloadCallback();
    
    
    private NullServerDownloadCallback() {
    }
    
    
    @Override
    public void download(int position, int length) {
    }
    
    
    @Override
    public void tasks(int position, int length) {
    }
    
}
