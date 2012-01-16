package de.unistuttgart.ipvs.pmp.api.ipc;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class IPCConnection implements ServiceConnection {
    
    private boolean connected;
    private String destinationService;
    public IPCScheduler connection;
    
    
    @Override
    public void onServiceDisconnected(ComponentName name) {
        throw new UnsupportedOperationException();
    }
    
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        throw new UnsupportedOperationException();
    }
    
    
    public void setDestinationService(String service) {
        throw new UnsupportedOperationException();
    }
    
    
    public IBinder getBinder() {
        throw new UnsupportedOperationException();
    }
    
    
    private boolean connect() {
        throw new UnsupportedOperationException();
    }
    
    
    public void disconnect() {
        throw new UnsupportedOperationException();
    }
    
}
