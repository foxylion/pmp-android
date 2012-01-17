package de.unistuttgart.ipvs.pmp.api.ipc.command;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.api.handler.PMPHandler;

/**
 * The basic IPC command to issue. All IPC commands must extend this functionality.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class IPCCommand {
    
    /**
     * The timeout point in the {@link System#currentTimeMillis()} domain.
     */
    private final long timeout;
    
    /**
     * The identifier of the IPC source
     */
    private final String sourceService;
    
    /**
     * The identifier of the IPC destination
     */
    private final String destinationService;
    
    /**
     * The handler associated with this {@link IPCCommand}.
     */
    private final PMPHandler handler;
    
    
    /**
     * Creates a new basic {@link IPCCommand}.
     * 
     * @param handler
     *            the handler associated with this command
     * @param sourceService
     *            the application or service creating this command
     * @param destinationService
     *            the service this command is directed to
     */
    public IPCCommand(PMPHandler handler, String sourceService, String destinationService) {
        this(handler, sourceService, destinationService, Long.MAX_VALUE);
    }
    
    
    /**
     * Creates a new basic {@link IPCCommand}.
     * 
     * @param handler
     *            the handler associated with this command
     * @param sourceService
     *            the application or service creating this command
     * @param destinationService
     *            the service this command is directed to
     * @param timeout
     *            timeout point in the {@link System#currentTimeMillis()} domain
     */
    public IPCCommand(PMPHandler handler, String sourceService, String destinationService, long timeout) {
        if (handler != null) {
            this.handler = handler;
        } else {
            this.handler = getNullHandler();
        }
        this.sourceService = sourceService;
        this.destinationService = destinationService;
        this.timeout = timeout;
    }
    
    
    /**
     * 
     * @return a null handler, i.e. a handler that is not literally "null" but performs no operation whatsoever.
     */
    protected abstract PMPHandler getNullHandler();
    
    
    public long getTimeout() {
        return this.timeout;
    }
    
    
    public String getSourceService() {
        return this.sourceService;
    }
    
    
    public String getDestinationService() {
        return this.destinationService;
    }
    
    
    public PMPHandler getHandler() {
        return this.handler;
    }
    
    
    /**
     * Executes the command on the binder binder. Supposed to be overridden.
     * 
     * @param binder
     *            the {@link IBinder} that was got by requesting the binder from the destinationIdentifier service
     */
    public abstract void execute(IBinder binder);
    
}
