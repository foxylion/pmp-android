package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;

public class AppServiceConnector extends AbstractConnector {

    public AppServiceConnector(Context context, PMPSignee signature,
	    String targetIdentifier) {
	super(context, signature, targetIdentifier);
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
