package de.unistuttgart.ipvs.pmp.service;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceResourceGroup;

;

/**
 * Implementation of the {@link IPMPServiceResourceGroup.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceResourceGroupStubImpl extends
	IPMPServiceResourceGroup.Stub {

    //private String identifier = null;

    public PMPServiceResourceGroupStubImpl(String identifier) {
	//this.identifier = identifier;
    }

    @Override
    public void savePrivacyLevel(String preset, String privacyLevel,
	    String value) throws RemoteException {

	/*
	 * Note: The Identifier of the ResourceGroup can be found in the
	 * identifier variable.
	 */

	// TODO IMPLEMENT
	throw new UnsupportedOperationException();
    }
}