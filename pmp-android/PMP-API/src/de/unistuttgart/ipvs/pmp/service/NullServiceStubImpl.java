package de.unistuttgart.ipvs.pmp.service;

import android.os.RemoteException;

public class NullServiceStubImpl extends INullService.Stub {
    
    private String cause;
    
    
    public NullServiceStubImpl(String cause) {
        this.cause = cause;
    }
    
    
    @Override
    public String getCause() throws RemoteException {
        return this.cause;
    }
    
}
