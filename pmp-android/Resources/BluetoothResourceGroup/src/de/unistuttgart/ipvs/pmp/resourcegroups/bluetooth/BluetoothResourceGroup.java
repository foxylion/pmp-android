package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth;

import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.resource.BluetoothResource;

public class BluetoothResourceGroup extends ResourceGroup {
	/**
	 * PKG Name of this ResourceGroup
	 */
	public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth";
	/**
	 * ResourceIdentifier
	 */
	public static final String R_BLUETOOTH = "bluetoothResource";
	/**
	 * Privacy Setting Identifier
	 */
	public static final String PS_USE_BLUETOOTH = "useBluetooth";

	/**
	 * {@link BluetoothResourceGroup} allows the use of Bluetooth
	 * 
	 * @param pmpci
	 */
	public BluetoothResourceGroup(IPMPConnectionInterface pmpci) {

		super(PACKAGE_NAME, pmpci);

		registerResource(R_BLUETOOTH, new BluetoothResource(this));

		registerPrivacySetting(PS_USE_BLUETOOTH, new BooleanPrivacySetting());

	}
}
