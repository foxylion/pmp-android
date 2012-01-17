package de.unistuttgart.ipvs.pmp.api.handler;

import de.unistuttgart.ipvs.pmp.api.ipc.command.IPC2PMPRegistrationCommand;

public class PMPRegistrationHandler extends PMPHandler {
    
    public IPC2PMPRegistrationCommand handler;
    
    
    public void onSuccess() {
        throw new UnsupportedOperationException();
    }
    
    
    public void onFailure(String message) {
        throw new UnsupportedOperationException();
    }
    
    
    public void onAlreadyRegistered() {
        throw new UnsupportedOperationException();
    }
    
}
