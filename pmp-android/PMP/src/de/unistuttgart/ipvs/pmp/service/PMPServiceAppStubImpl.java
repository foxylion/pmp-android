package de.unistuttgart.ipvs.pmp.service;

import android.content.Intent;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.activities.ServiceLvlActivity;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;

/**
 * Implementation of the {@link IPMPServiceApp.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceAppStubImpl extends IPMPServiceApp.Stub {
    
    private String identifier = null;
    
    
    public PMPServiceAppStubImpl(String identifier) {
        this.identifier = identifier;
    }
    
    
    @Override
    public void getInitialServiceLevel() throws RemoteException {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(PMPApplication.getContext(), ServiceLvlActivity.class);
        intent.putExtra(Constants.INTENT_IDENTIFIER, this.identifier);
        PMPApplication.getContext().startActivity(intent);
    }
}
