package de.unistuttgart.ipvs.pmp.resourcegroups.email;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

public class EmailResource extends Resource {
	
	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		// we want to pass some value from the RG
		EmailResourceGroup srg = (EmailResourceGroup) getResourceGroup();
        return new EmailOperationsStubImpl(appIdentifier, this, srg.getContext());
	}

}
