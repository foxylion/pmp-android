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
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.BluetoothResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

public class BluetoothResource extends Resource {
	/**
	 * If Logs should be logged
	 */
	private static final boolean D = true;
	/**
	 * BluetoothAdapter represents Bluetooth of the device
	 */
	BluetoothAdapter btAdapter = null;
	/**
	 * Threads used by Bluetooth
	 */
	AcceptThread acceptThread = null;
	ConnectThread connectThread = null;
	ConnectedThread connectedThread = null;

	ConnectedThread r;

	boolean stopReading = false;
	List<String> messages = null;
	/**
	 * UUID needed for Bluetooth Connections
	 */
	private static final UUID MY_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	/**
	 * Name for the Bleutooth Connections
	 */
	private static final String NAME = "BluetoothChat";
	/**
	 * Found Devices
	 */
	List<BluetoothDevice> devices;

	/**
	 * {@link BluetoothResourceGroup}
	 */
	private BluetoothResourceGroup blueRG;

	/**
	 * State of the {@link BluetoothResource}
	 */
	private int mState;

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming
												// connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing
													// connection
	public static final int STATE_CONNECTED = 3; // now connected to a remote
													// device

	int ROLE = 0;

	/**
	 * True if discovering, false otherwise
	 */
	public boolean discovering = false;

	public static final int NO_ROLE = 0;
	public static final int DRIVER_ROLE = 1;
	public static final int PASSENGER_ROLE = 2;

	/**
	 * Constructor
	 * 
	 * @param blueRG
	 */
	public BluetoothResource(BluetoothResourceGroup blueRG) {
		Looper.prepare();
		if (D) {
			Log.i(this.TAG, "Constructor called");
		}

		this.blueRG = blueRG;
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		this.devices = new ArrayList<BluetoothDevice>();
		this.messages = new ArrayList<String>();

	}

	/**
	 * Setting the Broadcasts for Bluetooth
	 * 
	 * @param appIdentifier
	 */
	public void setBroadcast(String appIdentifier) {
		if (D) {
			Log.i(this.TAG, "setBroadcast for " + appIdentifier);
		}
		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.blueRG.getContext(appIdentifier).registerReceiver(this.mReceiver,
				filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.blueRG.getContext(appIdentifier).registerReceiver(this.mReceiver,
				filter);
	}

	public void sendMessage(String message) throws RemoteException {
		if (D) {
			Log.i(this.TAG, "send Message" + message);
		}
		// Create temporary object
		// Synchronize a copy of the ConnectedThread
		this.r = this.connectedThread;
		// Perform the write unsynchronized

		byte[] send = message.getBytes();
		if (this.r != null) {
			this.r.write(send);
		}
	}

	private void setState(int state) {
		this.mState = state;
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
			if (!this.btAdapter.isEnabled()) {
				if (D) {
					Log.i(this.TAG, "Enable BT");
				}
			}
			this.btAdapter.enable();
			this.stopReading = false;

		} else {
			if (D) {
				Log.i(this.TAG, "Disable BT");
			}
			this.btAdapter.disable();
			this.stopReading = true;
		}
		setupBluetoothThreads();
	}

	private void setupBluetoothThreads() {
		if (this.acceptThread != null) {
			this.acceptThread.cancel();
			// acceptThread.start();
			this.acceptThread = null;
		}
		if (this.connectThread != null) {
			this.connectThread.cancel();
			this.connectThread = null;
		}
		if (this.connectedThread != null) {
			this.connectedThread.cancel();
			this.connectedThread = null;
		}
		if (this.r != null) {
			this.r.cancel();
			this.r = null;
		}

		this.messages.clear();

		this.discovering = false;

		setState(STATE_NONE);
	}

	public void connect(String address) {
		BluetoothDevice device = this.btAdapter.getRemoteDevice(address);

		this.connectThread = new ConnectThread(device);
		this.connectThread.start();

		setState(STATE_CONNECTING);
	}

	public boolean isEnabled() throws RemoteException {
		if (D) {
			Log.i(this.TAG, "isEnabled:" + this.btAdapter.isEnabled());
		}
		return this.btAdapter.isEnabled();
	}

	public void makeDiscoverable(String appIdentifier, String name, int time)
			throws RemoteException {
		if (D) {
			Log.i(this.TAG, "Before setName()");
		}
		setName(name);

		setupBluetoothThreads();

		if (D) {
			Log.i(this.TAG, "After setupBluetoothThreads()");
		}
		// Waiting for enabled Bluetooth
		while (!this.btAdapter.isEnabled()) {
		}

		if (D) {
			Log.i(this.TAG, "Make Discoverable");
		}
		if (this.btAdapter != null) {
			Log.i(this.TAG, "BTADAPTER NOT NULL");
		} else {
			Log.i(this.TAG, "BTADAPTER NULL");
		}
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();

		if (D) {
			Log.i(this.TAG, "Before btAdapter.getScanMode()");
		}

		this.btAdapter.getScanMode();
		if (D) {
			Log.i(this.TAG, "After btAdapter.getScanMode()");
		}
		if (this.btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			if (D) {
				Log.i(this.TAG, "Before INTENT");
			}
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			if (D) {
				Log.i(this.TAG, "Before PUTEXTRA()");
			}
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time * 60);
			if (D) {
				Log.i(this.TAG, "Before adding Flags");
			}
			discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (D) {
				Log.i(this.TAG, "Starting activity");
			}
			this.blueRG.getContext(appIdentifier).startActivity(
					discoverableIntent);
		}
		if (D) {
			Log.i(this.TAG, "Before AcceptThread");
		}
		this.acceptThread = new AcceptThread();
		this.acceptThread.start();
		if (D) {
			Log.i(this.TAG, "AcceptThread started!");
		}
	}

	public void discover(String appIdentifier, String name, int time)
			throws RemoteException {
		if (D) {
			Log.i(this.TAG, "Discovering");
		}
		if (this.btAdapter.isDiscovering()) {
			this.btAdapter.cancelDiscovery();
		}
		setName(name);

		setBroadcast(appIdentifier);
		this.btAdapter.startDiscovery();
		this.devices.clear();
		this.discovering = true;
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
					Log.i("BluetoothResource",
							"Found Device!" + device.getName());
					BluetoothResource.this.devices.add(device);
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				Log.i("BluetoothResource", "DISCOVERY FINISHED!");
				BluetoothResource.this.discovering = false;

			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				// try {
				// if(ROLE == PASSENGER_ROLE){
				// }else if(ROLE == DRIVER_ROLE){
				// connect(device.getAddress());
				// }
				//
				// } catch (RemoteException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}
	};

	public String TAG = "BluetoothResource";

	public boolean isDiscovering() throws RemoteException {
		if (D) {
			Log.i(this.TAG, "isDiscovering: " + this.discovering);
		}
		return this.discovering;
	}

	public DeviceArrayParcelable getPairedDevices() throws RemoteException {
		Set<BluetoothDevice> pairedDevices = this.btAdapter.getBondedDevices();
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

	public synchronized DeviceArrayParcelable getFoundDevices()
			throws RemoteException {

		List<String> devicesList = new ArrayList<String>();

		for (BluetoothDevice device : this.devices) {
			devicesList.add(device.getName());
			devicesList.add(device.getAddress());
		}

		DeviceArray array = new DeviceArray(devicesList);
		DeviceArrayParcelable arrayParcelable = new DeviceArrayParcelable(array);
		return arrayParcelable;
	}

	public MessageArrayParcelable getReceivedMessages() throws RemoteException {
		Log.i(this.TAG, "Messages in queue: " + this.messages.size());
		List<String> copy = new ArrayList<String>();
		for (String msg : this.messages) {
			copy.add(msg);
		}

		MessageArray array = new MessageArray(copy);
		MessageArrayParcelable arrayParcelable = new MessageArrayParcelable(
				array);
		this.messages.clear();

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
				if (D) {
					Log.i(BluetoothResource.this.TAG,
							"listenUsingRfcommWithServiceRecord");
				}
				tmp = BluetoothResource.this.btAdapter
						.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.serverSocket = tmp;
		}

		@Override
		public void run() {
			setName("AcceptThread");
			BluetoothSocket socket = null;
			try {
				if (D) {
					Log.i(BluetoothResource.this.TAG, "Accept clients");
				}
				socket = this.serverSocket.accept();

				connected(socket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void cancel() {
			try {
				if (D) {
					Log.i(BluetoothResource.this.TAG,
							"Socket close by cancel AcceptThread");
				}
				this.serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ConnectThread extends Thread {
		BluetoothSocket socket = null;

		public ConnectThread(BluetoothDevice device) {
			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				if (D) {
					Log.i(BluetoothResource.this.TAG,
							"createRfcommSocketToServiceRecord");
				}
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.socket = tmp;
		}

		@Override
		public void run() {
			setName("ConnectThread");

			BluetoothResource.this.btAdapter.cancelDiscovery();

			try {
				if (D) {
					Log.i(BluetoothResource.this.TAG, "Connect to socket");
				}
				this.socket.connect();

				connected(this.socket);
			} catch (IOException e) {

			}
		}

		public void cancel() {
			try {
				if (D) {
					Log.i(BluetoothResource.this.TAG,
							"Socket close by cancel ConnectThread");
				}
				this.socket.close();
			} catch (IOException e) {
			}
		}
	}

	public void connected(BluetoothSocket socket) {
		Log.i(this.TAG, "connected");

		// acceptThread.cancel();

		this.connectedThread = new ConnectedThread(socket);
		this.connectedThread.start();

		Log.i(this.TAG, "set state connected");

		setState(STATE_CONNECTED);
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

			this.in = tmpIn;
			this.out = tmpOut;
		}

		@Override
		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;

			// Keep listening to the InputStream while connected
			while (!BluetoothResource.this.stopReading) {
				try {
					// Read from the InputStream
					bytes = this.in.read(buffer);

					String msg = new String(buffer, 0, bytes);
					synchronized (BluetoothResource.this.messages) {
						try {
							BluetoothResource.this.messages.add(msg);
						} catch (Exception e) {
							Log.i(BluetoothResource.this.TAG, "OUTOFMEMORY");
							this.in.close();
						}
					}

					Log.i(BluetoothResource.this.TAG + " ConnectedThread",
							"read: Adding message: " + msg);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void write(byte[] buffer) {
			try {
				this.out.write(buffer);

				String msg = new String(buffer);
				synchronized (BluetoothResource.this.messages) {
					BluetoothResource.this.messages.add(msg);
				}
				Log.i(BluetoothResource.this.TAG + " ConnectedThread",
						"write: Adding message: " + msg);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void cancel() {
			try {
				this.socket.close();
				Log.e("BluetoothResource: " + this.getClass(), "Close socket!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected() {
		if (this.mState == STATE_CONNECTED) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDeviceBonded(String address) {
		Set<BluetoothDevice> pairedDevices = this.btAdapter.getBondedDevices();
		new ArrayList<String>();
		boolean bonded = false;
		for (BluetoothDevice device : pairedDevices) {
			if (device.getAddress().equals(address)) {
				bonded = true;
			}
		}

		return bonded;
	}

	public void setName(String name) {
		if (D) {
			if (this.btAdapter != null) {
				Log.i(this.TAG, "setName btAdapter not null");
			}
		}
		this.btAdapter.setName(name);

		Log.i(this.TAG, "Name gesetzt:" + name);
		Log.i(this.TAG, "Tatsächliche Name:" + this.btAdapter.getName());
	}
}