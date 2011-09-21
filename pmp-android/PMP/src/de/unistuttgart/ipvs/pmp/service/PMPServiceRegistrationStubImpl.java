package de.unistuttgart.ipvs.pmp.service;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;

/**
 * Implementation of the {@link IPMPServiceRegistration.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceRegistrationStubImpl extends IPMPServiceRegistration.Stub {
    
    private String identifier = null;
    
    
    public PMPServiceRegistrationStubImpl(String identifier) {
        this.identifier = identifier;
    }
    
    
    @Override
    public byte[] registerApp(final byte[] publicKey) throws RemoteException {
        if (this.identifier == null || this.identifier.length() == 0) {
            Log.e("An App tried to register at PMPService but the identifier was NULL or a String with length 0");
            return null;
        }
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                ModelSingleton.getInstance().getModel()
                        .addApp(PMPServiceRegistrationStubImpl.this.identifier, publicKey);
            }
        }).start();
        
        return PMPApplication.getSignee().getLocalPublicKey();
    }
    
    
    @Override
    public byte[] registerResourceGroup(final byte[] publicKey) throws RemoteException {
        if (this.identifier == null || this.identifier.length() == 0) {
            Log.e("An ResourceGroup tried to register at PMPService but the identifier was NULL or a String with length 0");
            return null;
        }
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                ModelSingleton.getInstance().getModel()
                        .addResourceGroup(PMPServiceRegistrationStubImpl.this.identifier, publicKey);
            }
        }).start();
        
        return PMPApplication.getSignee().getLocalPublicKey();
    }
}
