package de.unistuttgart.ipvs.pmp.api.handler;

/**
 * General handler that reacts on any IPC command. Handlers are guaranteed to be called in a separate {@link Thread}.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class PMPHandler {
    
    /**
     * Called before the IPC connection is established.
     * 
     * @throws AbortIPCException
     *             if and only if you wish to abort the IPC before it starts
     */
    public void onPrepare() throws AbortIPCException {
    }
    
    
    /**
     * Called after the command was executed.
     */
    public void onFinalize() {
    }
    
    
    /**
     * Called whenever a binding failure occurs. Typical reasons are for example the destination service (e.g. PMP) is
     * not installed on the device or an error occured during the message transmit.
     */
    public void onBindingFailed() {
    }
    
    
    /**
     * Called when a specified timeout was exceeded.
     */
    public void onTimeout() {
    }
}
