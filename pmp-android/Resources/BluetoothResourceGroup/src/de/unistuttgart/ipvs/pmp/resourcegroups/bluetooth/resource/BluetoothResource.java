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
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.BluetoothResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

public class BluetoothResource extends Resource {

	BluetoothAdapter btAdapter = null;

	AcceptThread acceptThread = null;
	ConnectThread connectThread = null;
	ConnectedThread connectedThread = null;

	List<String> messages = null;
	private static final UUID MY_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	private static final String NAME = "BluetoothChat";

	boolean discovering = false;

	List<BluetoothDevice> devices;

	private BluetoothResourceGroup blueRG;

	public BluetoothResource(BluetoothResourceGroup blueRG) {
		Looper.prepare();
		this.blueRG = blueRG;
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		devices = new ArrayList<BluetoothDevice>();
		messages = new ArrayList<String>();
		
		

	}

	public void setBroadcast(String appIdentifier){
		Log.i("BluetoothResource", "SET Broadcast");
		 // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        blueRG.getContext(appIdentifier).registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        blueRG.getContext(appIdentifier).registerReceiver(mReceiver, filter);
	}
	
	@Override
	public IBinder getAndroidInterface(String appPackage) {
		return new BluetoothImpl(this.blueRG, this, appPackage);
	}

	@Override
	public IBinder getMockedAndroidInterface(String appPackage) {
		return new BluetoothMockImpl();
	}

	@Override
	public IBinder getCloakedAndroidInterface(String appPackage) {
		return new BluetoothCloakImpl();
	}

	public void enableBluetooth(boolean state) throws RemoteException {
		if (state) {
			if (!btAdapter.isEnabled())
				btAdapter.enable();
		} else {
			btAdapter.disable();
		}
	}

	public boolean isEnabled() throws RemoteException {
		return btAdapter.isEnabled();
	}

	public void makeDiscoverable(String appIdentifier, int time)
			throws RemoteException {
		
		if (btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time * 60);
			discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			blueRG.getContext(appIdentifier).startActivity(discoverableIntent);
		}
		if (acceptThread == null) {
			acceptThread = new AcceptThread();
			acceptThread.start();
		}

	}

	public void discover(String appIdentifier) throws RemoteException {
		if (btAdapter.isDiscovering()) {
			btAdapter.cancelDiscovery();
		}
		btAdapter.startDiscovery();
		Log.i("BluetoothResource", "DISCOVER");
		setBroadcast(appIdentifier);
		discovering = true;
		devices.clear();
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					Log.i("BluetoothResource", "Found Device!" + device.getName());
					devices.add(device);
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				Log.i("BluetoothResource", "DISCOVERY FINISHED!");
				discovering = false;
			}
		}
	};

	public boolean isDiscovering() throws RemoteException {
		return discovering;
	}

	public DeviceArrayParcelable getPairedDevices() throws RemoteException {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		List<String> pairedDevicesList = new ArrayList<String>();

		for (BluetoothDevice device : pairedDevices) {
			System.out.println("In FOR");
			pairedDevicesList.add(device.getName());
			pairedDevicesList.add(device.getAddress());
		}
		
		DeviceArray array = new DeviceArray(pairedDevicesList);
		DeviceArrayParcelable arrayParcelable = new DeviceArrayParcelable(array);
		return arrayParcelable;
	}

	public DeviceArrayParcelable getFoundDevices() throws RemoteException {

		List<String> devicesList = new ArrayList<String>();

		for (BluetoothDevice device : devices) {
			devicesList.add(device.getName());
			devicesList.add(device.getAddress());
		}

		DeviceArray array = new DeviceArray(devicesList);
		DeviceArrayParcelable arrayParcelable = new DeviceArrayParcelable(array);
		return arrayParcelable;
	}

	public void connect(String address) throws RemoteException {
		connectThread = new ConnectThread(address);
		connectThread.start();
	}

	public void sendMessage(String message) throws RemoteException {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		r = connectedThread;
		// Perform the write unsynchronized

		byte[] send = message.getBytes();

		r.write(send);
	}

	public MessageArrayParcelable getReceivedMessages() throws RemoteException {
		MessageArray array = new MessageArray(messages);
		MessageArrayParcelable arrayParcelable = new MessageArrayParcelable(array);
		synchronized (messages) {
			messages.clear();
		}
		return arrayParcelable;
	}

	public boolean isBluetoothAvailable() throws RemoteException {
		if (this.btAdapter == null) {
			return false;
		} else {
			return true;
		}
	}

	private class AcceptThread extends Thread {

		BluetoothServerSocket serverSocket;

		public AcceptThread() {
			BluetoothServerSocket tmp = null;

			// Create a new listening server socket
			try {
				tmp = btAdapter.listenUsingRfcommWithServiceRecord(NAME,
						MY_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			serverSocket = tmp;
		}

		public void run() {
			setName("AcceptThread");
			BluetoothSocket socket = null;

			// Listen to the server socket if we're not connected
			try {
				while ((socket = serverSocket.accept()) != null) {

					// If a connection was accepted
					if (socket != null) {
						connected(socket, socket.getRemoteDevice());
					}
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		public void cancel() {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void connected(BluetoothSocket socket, BluetoothDevice remoteDevice) {

		connectedThread = new ConnectedThread(socket);
		connectedThread.start();
	}

	private class ConnectThread extends Thread {
		BluetoothDevice toConnectDevice = null;
		BluetoothSocket socket = null;

		public ConnectThread(String address) {
			for (BluetoothDevice device : devices) {
				if (device.getName().equals(address)) {
					toConnectDevice = device;
				}
			}
			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				tmp = toConnectDevice
						.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket = tmp;
		}

		public void run() {
			setName("ConnectThread");

			// Always cancel discovery because it will slow down a connection
			btAdapter.cancelDiscovery();

			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				socket.connect();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				return;
			}

			connected(socket, toConnectDevice);
		}

		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ConnectedThread extends Thread {
		BluetoothSocket socket = null;
		InputStream in = null;
		OutputStream out = null;

		public ConnectedThread(BluetoothSocket socket) {
			this.socket = socket;

			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			in = tmpIn;
			out = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// Read from the InputStream
					bytes = in.read(buffer);

					String msg = new String(buffer);
					String[] msgArray = msg.split("#");
					messages.add(msgArray[0]);
					messages.add(msgArray[1]);

				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}

		public void write(byte[] buffer) {
			try {
				out.write(buffer);

				String msg = new String(buffer);
				String[] msgArray = msg.split("#");
				messages.add(msgArray[0]);
				messages.add(msgArray[1]);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
