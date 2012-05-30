package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.BluetoothResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

public class BluetoothImpl extends IBluetooth.Stub {

	BluetoothResourceGroup blueRG;
	BluetoothResource btResource;
	String appIdentifier;
	PermissionValidator psv;
	Context context;

	private static final String NAME = "BluetoothChat";


	public BluetoothImpl(BluetoothResourceGroup blueRG,
			BluetoothResource btResource, String appIdentifier) {

		this.blueRG = blueRG;
		this.btResource = btResource;
		this.appIdentifier = appIdentifier;
		this.psv = new PermissionValidator(this.blueRG, this.appIdentifier);
	}

	@Override
	public void enableBluetooth(boolean state) throws RemoteException {
		
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		btResource.enableBluetooth(state);
	}

	@Override
	public boolean isEnabled() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return btResource.isEnabled();
	}

	@Override
	public void makeDiscoverable(String name, int time) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		btResource.makeDiscoverable(appIdentifier, name, time);
	}

	@Override
	public void discover(String name, int time) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		btResource.discover(appIdentifier,name,time);
	}


	@Override
	public boolean isDiscovering() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return btResource.isDiscovering();
	}

	@Override
	public DeviceArrayParcelable getPairedDevices() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return btResource.getPairedDevices();
	}

	@Override
	public DeviceArrayParcelable getFoundDevices() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return btResource.getFoundDevices();
	}

	@Override
	public void connect(String address) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		btResource.connect(address);
	}

	@Override
	public void sendMessage(String message) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		btResource.sendMessage(message);
	}

	@Override
	public MessageArrayParcelable getReceivedMessages() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return btResource.getReceivedMessages();
	}

	@Override
	public boolean isBluetoothAvailable() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return btResource.isBluetoothAvailable();
	}

	@Override
	public boolean isConnected() throws RemoteException {
		// TODO Auto-generated method stub
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return btResource.isConnected();
	}

	@Override
	public boolean isDeviceBonded(String address) throws RemoteException {
		// TODO Auto-generated method stub
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return btResource.isDeviceBonded(address);
	}

	@Override
	public void setName(String name) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		btResource.setName(name);
	}
}
