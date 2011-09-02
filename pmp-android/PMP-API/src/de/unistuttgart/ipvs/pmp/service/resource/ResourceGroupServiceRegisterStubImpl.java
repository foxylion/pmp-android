package de.unistuttgart.ipvs.pmp.service.resource;

import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServiceRegister;
import de.unistuttgart.ipvs.pmp.service.helper.PMPSignature;
import android.os.RemoteException;

/**
 * Implementation of the {@link IResourceGroupServiceRegister.Stub} stub.
 * 
 * @author Tobias Kuhn
 */
public class ResourceGroupServiceRegisterStubImpl extends
	IResourceGroupServiceRegister.Stub {
    
    /**
     * identifier of the remote sender 
     */
    private String remoteIdentifier;
    
    /**
     * The referenced signature to be updated by this registration.
     */
    private PMPSignature refSig;

    public void setIdentifier(String identifier) {
	remoteIdentifier = identifier;	
    }

    public void setSignature(PMPSignature resgrpSig) {
	refSig = resgrpSig;	
    }

    @Override
    public void register(byte[] publicKey) throws RemoteException {
	if (publicKey != null) {
	    refSig.setRemotePublicKey(remoteIdentifier, publicKey);
	}	
    }

}
