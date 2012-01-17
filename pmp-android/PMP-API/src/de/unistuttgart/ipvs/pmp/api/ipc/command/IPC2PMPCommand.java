package de.unistuttgart.ipvs.pmp.api.ipc.command;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.handler.PMPHandler;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;

/**
 * A helper class for IPC commands directed towards PMP
 * 
 * @author Tobias Kuhn
 * 
 * @param <T>
 *            the handler subclass to have available in execute.
 */
public abstract class IPC2PMPCommand<T extends PMPHandler> extends IPCCommand {
    
    public IPC2PMPCommand(T handler, String sourceService, long timeout) {
        super(handler, sourceService, Constants.PMP_IDENTIFIER, timeout);
    }
    
    
    public IPC2PMPCommand(T handler, String sourceService) {
        super(handler, sourceService, Constants.PMP_IDENTIFIER);
    }
    
    
    @Override
    public void execute(IBinder binder) {
        if (binder instanceof IPMPService) {
            try {
                executeOnPMP(IPMPService.Stub.asInterface(binder));
            } catch (RemoteException re) {
                Log.e("Unexpected remote exception: ", re);
            }
        } else {
            
            String id = "?";
            try {
                id = binder.getInterfaceDescriptor();
            } catch (RemoteException re) {
                Log.e("Cannot get interface descriptor: ", re);
            }
            
            Log.e("Got wrong IBinder for PMP: " + id);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // guaranteed by the constructor
    protected T getPMPHandler() {
        return (T) getHandler();
    }
    
    
    protected abstract void executeOnPMP(IPMPService pmp) throws RemoteException;
    
}
