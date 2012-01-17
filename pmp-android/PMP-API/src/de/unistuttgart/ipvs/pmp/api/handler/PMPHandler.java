package de.unistuttgart.ipvs.pmp.api.handler;

public abstract class PMPHandler {
    
    protected void onPrepare() {
        throw new UnsupportedOperationException();
    }
    
    
    public void onTimeout() {
        throw new UnsupportedOperationException();
    }
    
    
    protected void onFinalize() {
        throw new UnsupportedOperationException();
    }
}
