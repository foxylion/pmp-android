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

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
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
