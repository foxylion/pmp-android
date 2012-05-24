package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth;

import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.resource.BluetoothResource;

public class BluetoothResourceGroup extends ResourceGroup {

	public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth";

	public static final String R_BLUETOOTH = "bluetoothResource";

	public static final String PS_USE_BLUETOOTH = "useBluetooth";

	public BluetoothResourceGroup(IPMPConnectionInterface pmpci) {

		super(PACKAGE_NAME, pmpci);
		
		registerResource(R_BLUETOOTH,
				new BluetoothResource(this));
		
		registerPrivacySetting(PS_USE_BLUETOOTH,
				new BooleanPrivacySetting());
		
		
	}
}
