package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl;

import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

interface IBluetooth {
	
	boolean isBluetoothAvailable();
	
	void setName(String name);
	
	
	void enableBluetooth(boolean state);
	
	boolean isEnabled();
	
	boolean isDeviceBonded(String address);
	
	void makeDiscoverable(String name, int time);
	boolean isMakingDiscoverable();
	
	void discover();
	boolean isDiscovering();
	
	DeviceArrayParcelable getPairedDevices();
	
	DeviceArrayParcelable getFoundDevices();
	
	void connect(String address);
	
	boolean isConnected();
	
	void sendMessage(String message);
	
	MessageArrayParcelable getReceivedMessages();	
}