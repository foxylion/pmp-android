package de.unistuttgart.ipvs.pmp.model;

/**
 * General IPC provider which provides all the inter process communciation necessary for the model.
 * 
 * @author Tobias Kuhn
 * 
 */
public class IPCProvider {
    
    /**
     * Singleton stuff
     */
    private static final IPCProvider instance = new IPCProvider();
    
    
    public static IPCProvider getInstance() {
        return instance;
    }
    
    
    /**
     * Singleton constructor
     */
    private IPCProvider() {
    }
    
    
    /**
     * Starts a cumulative update session. This means, the IPC provider will start buffering IPC messages instead of
     * directly delivering them directly. Be sure to always call {@link IPCProvider#endUpdate()} afterwards.
     */
    public void startUpdate() {
    }
    
    
    /**
     * Ends a cumulative update session started by {@link IPCProvider#startUpdate()}.
     */
    public void endUpdate() {
    }
    
}
