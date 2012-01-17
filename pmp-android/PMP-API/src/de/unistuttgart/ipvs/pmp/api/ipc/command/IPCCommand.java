package de.unistuttgart.ipvs.pmp.api.ipc.command;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.api.handler.PMPHandler;

public abstract class IPCCommand {
    
    private long timeout;
    private String callingApplication;
    private PMPHandler handler;
    
    
    public long getTimeout() {
        return this.timeout;
    }
    
    
    public String getCallingApplication() {
        return this.callingApplication;
    }
    
    
    public PMPHandler getHandler() {
        return this.handler;
    }
    
    
    public void execute(IBinder binder) {
        throw new UnsupportedOperationException();
    }
}
