package de.unistuttgart.ipvs.pmp.resourcegroups.switches;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;
import de.unistuttgart.ipvs.pmp.resourcegroups.switches.IWifiSwitch.Stub;

/**
 * Implementation of the {@link IWifiSwitch} binder interface.
 * 
 * @author Tobias Kuhn
 * 
 */
public class WifiSwitchStubImpl extends Stub {

	private String appIdentifier;
	private WifiSwitchResource resource;
	private Context context;

	public WifiSwitchStubImpl(String appIdentifier,
			WifiSwitchResource resource, Context context) {
		this.appIdentifier = appIdentifier;
		this.resource = resource;
		this.context = context;
	}

	@Override
	public boolean getState() throws RemoteException {
		if (!verifyAccessAllowed()) {
			throw new IllegalAccessError();
		}

		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		switch (wm.getWifiState()) {
		case WifiManager.WIFI_STATE_ENABLED:
		case WifiManager.WIFI_STATE_ENABLING:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void setState(boolean newState) throws RemoteException {
		if (!verifyAccessAllowed()) {
			throw new IllegalAccessError();
		}

		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wm.setWifiEnabled(newState);
	}

	/**
	 * Verifies that the access is allowed.
	 * 
	 * @return True, iff the access was allowed.
	 */
	private boolean verifyAccessAllowed() {
		BooleanPrivacyLevel bpl = (BooleanPrivacyLevel) resource
				.getPrivacyLevel(SwitchesResourceGroup.PRIVACY_LEVEL_WIFI_SWITCH);
		return bpl.permits(appIdentifier, true);
	}
}
