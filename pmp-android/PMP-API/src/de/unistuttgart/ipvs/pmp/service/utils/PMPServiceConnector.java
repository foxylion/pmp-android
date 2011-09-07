package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;

public class PMPServiceConnector extends AbstractConnector {
    
    public PMPServiceConnector(Context context, PMPSignee signature) {
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
