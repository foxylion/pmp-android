package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.BluetoothResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

public class BluetoothMockImpl extends IBluetooth.Stub {

	@Override
	public void enableBluetooth(boolean state) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isEnabled() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void makeDiscoverable(String name, int time) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discover(String name, int time) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDiscovering() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DeviceArrayParcelable getPairedDevices() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceArrayParcelable getFoundDevices() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connect(String address) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MessageArrayParcelable getReceivedMessages() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBluetoothAvailable() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConnected() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDeviceBonded(String address) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setName(String name) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
