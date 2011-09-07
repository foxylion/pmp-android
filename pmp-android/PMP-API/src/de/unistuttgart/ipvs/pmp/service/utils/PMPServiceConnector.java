package de.unistuttgart.ipvs.pmp.service.utils;

import de.unistuttgart.ipvs.pmp.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class PMPServiceConnector extends AbstractConnector {
    
    public PMPServiceConnector(Context context, PMPSignature signature) {
	super(context, signature, Constants.PMP_IDENTIFIER);
    }
    
    @Override
    public IBinder getService() {
        return super.getService();
    }
    
    @Override
    protected void serviceConnected() {
	// TODO Auto-generated method stub
    }

    @Override
    protected void serviceDisconnected() {
	// TODO Auto-generated method stub
    }

}
