package de.unistuttgart.ipvs.pmp.api.handler;

public abstract class PMPHandler {
    
    public void onPrepare() {
        throw new UnsupportedOperationException();
    }
    
    
    public void onTimeout() {
        throw new UnsupportedOperationException();
    }
    
    
    public void onBindingFailed() {
        throw new UnsupportedOperationException();
    }
    
    
    public void onFinalize() {
        throw new UnsupportedOperationException();
    }
}
