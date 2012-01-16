package de.unistuttgart.ipvs.pmp.api.ipc.command;

import de.unistuttgart.ipvs.pmp.api.handler.PMPHandler;
import de.unistuttgart.ipvs.pmp.api.ipc.IPCScheduler;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;

public abstract class IPCCommand {
    
    private long timeout;
    private String application;
    private PMPHandler handler;
    public IPCScheduler queue;
    
    
    protected void execute(IPMPService interface_22) {
        throw new UnsupportedOperationException();
    }
}
