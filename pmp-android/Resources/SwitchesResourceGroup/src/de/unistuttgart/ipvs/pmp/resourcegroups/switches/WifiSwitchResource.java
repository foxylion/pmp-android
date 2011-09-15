package de.unistuttgart.ipvs.pmp.resourcegroups.switches;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * {@link Resource} for the Wifi Switch.
 * 
 * @author Tobias Kuhn
 * 
 */
public class WifiSwitchResource extends Resource {

	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		// we want to pass some value from the RG
		SwitchesResourceGroup srg = (SwitchesResourceGroup) getResourceGroup();
		return new WifiSwitchStubImpl(appIdentifier, this, srg.getContext());
	}

}
