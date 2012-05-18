package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl;

import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

interface IBluetooth {
	
	boolean isBluetoothAvailable();
	
	void enableBluetooth(boolean state);
	
	boolean isEnabled();
	
	void makeDiscoverable(int time);
	
	void discover();
	boolean isDiscovering();
	
	DeviceArrayParcelable getPairedDevices();
	
	DeviceArrayParcelable getFoundDevices();
	
	void connect(String address);
	
	boolean isConnected();
	
	void sendMessage(String message);
	
	MessageArrayParcelable getReceivedMessages();	
}