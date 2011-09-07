package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.Context;
import android.content.Intent;

public class ResourceGroupServiceConnector extends AbstractConnector {

    public ResourceGroupServiceConnector(Context context, PMPSignature signature,
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
