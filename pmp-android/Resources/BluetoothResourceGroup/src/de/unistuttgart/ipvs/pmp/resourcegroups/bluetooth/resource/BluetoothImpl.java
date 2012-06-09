/*
 * Copyright 2012 pmp-android development team
 * Project: BluetoothResourceGroup
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.resource;

import android.content.Context;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.BluetoothResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

public class BluetoothImpl extends IBluetooth.Stub {

	BluetoothResourceGroup blueRG;
	BluetoothResource btResource;
	String appIdentifier;
	PermissionValidator psv;
	Context context;

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
		this.btResource.enableBluetooth(state);
	}

	@Override
	public boolean isEnabled() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return this.btResource.isEnabled();
	}

	@Override
	public void makeDiscoverable(String name, int time) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		this.btResource.makeDiscoverable(this.appIdentifier, name, time);
	}

	@Override
	public void discover(String name, int time) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		this.btResource.discover(this.appIdentifier, name, time);
	}

	@Override
	public boolean isDiscovering() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return this.btResource.isDiscovering();
	}

	@Override
	public DeviceArrayParcelable getPairedDevices() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return this.btResource.getPairedDevices();
	}

	@Override
	public DeviceArrayParcelable getFoundDevices() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return this.btResource.getFoundDevices();
	}

	@Override
	public void connect(String address) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		this.btResource.connect(address);
	}

	@Override
	public void sendMessage(String message) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		this.btResource.sendMessage(message);
	}

	@Override
	public MessageArrayParcelable getReceivedMessages() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return this.btResource.getReceivedMessages();
	}

	@Override
	public boolean isBluetoothAvailable() throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return this.btResource.isBluetoothAvailable();
	}

	@Override
	public boolean isConnected() throws RemoteException {
		// TODO Auto-generated method stub
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return this.btResource.isConnected();
	}

	@Override
	public boolean isDeviceBonded(String address) throws RemoteException {
		// TODO Auto-generated method stub
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		return this.btResource.isDeviceBonded(address);
	}

	@Override
	public void setName(String name) throws RemoteException {
		this.psv.validate(BluetoothResourceGroup.PS_USE_BLUETOOTH, "true");
		this.btResource.setName(name);
	}
}
