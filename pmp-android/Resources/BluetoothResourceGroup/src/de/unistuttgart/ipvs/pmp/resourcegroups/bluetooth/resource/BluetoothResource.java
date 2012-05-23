package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
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
import android.os.ParcelUuid;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.BluetoothResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

public class BluetoothResource extends Resource {

	private static final boolean D = true;

	BluetoothAdapter btAdapter = null;

	AcceptThread acceptThread = null;
	ConnectThread connectThread = null;
	ConnectedThread connectedThread = null;
	ConnectedThread r;
	boolean stopReading = false;
	List<String> messages = null;
	private static final UUID MY_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	private static final String NAME = "BluetoothChat";

	List<BluetoothDevice> devices;

	private BluetoothResourceGroup blueRG;

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

	public boolean discovering = false;

	public static final int NO_ROLE = 0;
	public static final int DRIVER_ROLE = 1;
	public static final int PASSENGER_ROLE = 2;

	public BluetoothResource(BluetoothResourceGroup blueRG) {
		Looper.prepare();
		if (D) {
			Log.i(TAG, "Constructor called");
		}

		this.blueRG = blueRG;
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		devices = new ArrayList<BluetoothDevice>();
		messages = new ArrayList<String>();

	}

	public void setBroadcast(String appIdentifier) {
		if (D) {
			Log.i(TAG, "setBroadcast for " + appIdentifier);
		}
		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		blueRG.getContext(appIdentifier).registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		blueRG.getContext(appIdentifier).registerReceiver(mReceiver, filter);
	}

	public void sendMessage(String message) throws RemoteException {
		if (D) {
			Log.i(TAG, "send Message" + message);
		}
		// Create temporary object
		// Synchronize a copy of the ConnectedThread
		r = connectedThread;
		// Perform the write unsynchronized

		byte[] send = message.getBytes();
		if (r != null) {
			r.write(send);
		}
	}

	private void setState(int state) {
		mState = state;
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
				if (D) {
					Log.i(TAG, "Enable BT");
				}
			btAdapter.enable();
			stopReading = false;

		} else {
			if (D) {
				Log.i(TAG, "Disable BT");
			}
			btAdapter.disable();
			stopReading = true;
		}
		setupBluetoothThreads();
	}

	private void setupBluetoothThreads() {
		if (acceptThread != null) {
			acceptThread.cancel();
			// acceptThread.start();
			acceptThread = null;
		}
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}
		if(r!= null){
			r.cancel();
			r=null;
		}
		
		messages.clear();
		
		discovering = false;

		setState(STATE_NONE);
	}

	public void connect(String address) {
		BluetoothDevice device = btAdapter.getRemoteDevice(address);

		connectThread = new ConnectThread(device);
		connectThread.start();

		setState(STATE_CONNECTING);
	}

	public boolean isEnabled() throws RemoteException {
		if (D) {
			Log.i(TAG, "isEnabled:" + btAdapter.isEnabled());
		}
		return btAdapter.isEnabled();
	}

	public void makeDiscoverable(String appIdentifier, String name, int time)
			throws RemoteException {
		if (D) {
			Log.i(TAG, "Before setName()");
		}
		setName(name);
		
		setupBluetoothThreads();

		if (D) {
			Log.i(TAG, "After setupBluetoothThreads()");
		}
		// Waiting for enabled Bluetooth
		while (!btAdapter.isEnabled()) {
		}

		if (D) {
			Log.i(TAG, "Make Discoverable");
		}
		if (btAdapter != null) {
			Log.i(TAG, "BTADAPTER NOT NULL");
		} else {
			Log.i(TAG, "BTADAPTER NULL");
		}
		btAdapter = BluetoothAdapter.getDefaultAdapter();

		if (D) {
			Log.i(TAG, "Before btAdapter.getScanMode()");
		}

		int zahl = btAdapter.getScanMode();
		if (D) {
			Log.i(TAG, "After btAdapter.getScanMode()");
		}
		if (btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			if (D) {
				Log.i(TAG, "Before INTENT");
			}
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			if (D) {
				Log.i(TAG, "Before PUTEXTRA()");
			}
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time * 60);
			if (D) {
				Log.i(TAG, "Before adding Flags");
			}
			discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (D) {
				Log.i(TAG, "Starting activity");
			}
			blueRG.getContext(appIdentifier).startActivity(discoverableIntent);
		}
		if (D) {
			Log.i(TAG, "Before AcceptThread");
		}
		acceptThread = new AcceptThread();
		acceptThread.start();
		if (D) {
			Log.i(TAG, "AcceptThread started!");
		}
	}

	public void discover(String appIdentifier, String name, int time) throws RemoteException {
		if (D) {
			Log.i(TAG, "Discovering");
		}
		if (btAdapter.isDiscovering()) {
			btAdapter.cancelDiscovery();
		}
		setName(name);
		
		setBroadcast(appIdentifier);
		btAdapter.startDiscovery();
		devices.clear();
		discovering = true;
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
					devices.add(device);
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				Log.i("BluetoothResource", "DISCOVERY FINISHED!");
				discovering = false;

			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

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
			Log.i(TAG, "isDiscovering: " + discovering);
		}
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

	public synchronized DeviceArrayParcelable getFoundDevices()
			throws RemoteException {

		List<String> devicesList = new ArrayList<String>();

		for (BluetoothDevice device : devices) {
			devicesList.add(device.getName());
			devicesList.add(device.getAddress());
		}

		DeviceArray array = new DeviceArray(devicesList);
		DeviceArrayParcelable arrayParcelable = new DeviceArrayParcelable(array);
		return arrayParcelable;
	}

	public MessageArrayParcelable getReceivedMessages() throws RemoteException {
		Log.i(TAG, "Messages in queue: " + messages.size());
		List<String> copy = new ArrayList<String>();
		for (String msg : messages) {
			copy.add(msg);
		}

		MessageArray array = new MessageArray(copy);
		MessageArrayParcelable arrayParcelable = new MessageArrayParcelable(
				array);
		messages.clear();
		
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
					Log.i(TAG, "listenUsingRfcommWithServiceRecord");
				}
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
			try {
				if (D) {
					Log.i(TAG, "Accept clients");
				}
				socket = serverSocket.accept();

				connected(socket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void cancel() {
			try {
				if (D) {
					Log.i(TAG, "Socket close by cancel AcceptThread");
				}
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ConnectThread extends Thread {
		BluetoothSocket socket = null;
		BluetoothDevice device = null;

		public ConnectThread(BluetoothDevice device) {
			this.device = device;

			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				if (D) {
					Log.i(TAG, "createRfcommSocketToServiceRecord");
				}
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			socket = tmp;
		}

		public void run() {
			setName("ConnectThread");

			btAdapter.cancelDiscovery();

			try {
				if (D) {
					Log.i(TAG, "Connect to socket");
				}
				socket.connect();

				connected(socket);
			} catch (IOException e) {

			}
		}

		public void cancel() {
			try {
				if (D) {
					Log.i(TAG, "Socket close by cancel ConnectThread");
				}
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	public void connected(BluetoothSocket socket) {
		Log.i(TAG, "connected");

		// acceptThread.cancel();

		connectedThread = new ConnectedThread(socket);
		connectedThread.start();

		Log.i(TAG, "set state connected");

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

			in = tmpIn;
			out = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;

			// Keep listening to the InputStream while connected
			while (!stopReading) {
				try {
					// Read from the InputStream
					bytes = in.read(buffer);

					String msg = new String(buffer, 0, bytes);
					synchronized (messages) {
						try{
						messages.add(msg);
						}catch (Exception e) {
							Log.i(TAG, "OUTOFMEMORY");
							in.close();
						}
					}

					Log.i(TAG + " ConnectedThread", "read: Adding message: "
							+ msg);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void write(byte[] buffer) {
			try {
				out.write(buffer);

				String msg = new String(buffer);
				synchronized (messages) {
					messages.add(msg);
				}
				Log.i(TAG + " ConnectedThread", "write: Adding message: " + msg);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void cancel() {
			try {
				socket.close();
				Log.e("BluetoothResource: " + this.getClass(), "Close socket!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected() {
		if (mState == STATE_CONNECTED)
			return true;
		else
			return false;
	}

	public boolean isDeviceBonded(String address) {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		List<String> pairedDevicesList = new ArrayList<String>();
		boolean bonded = false;
		for (BluetoothDevice device : pairedDevices) {
			if (device.getAddress().equals(address)) {
				bonded = true;
			}
		}

		return bonded;
	}

	public void setName(String name) {
		if(D){
			if(btAdapter != null){
				Log.i(TAG, "setName btAdapter not null");
			}
		}
		btAdapter.setName(name);

		Log.i(TAG, "Name gesetzt:" + name);
		Log.i(TAG, "Tatsächliche Name:" + btAdapter.getName());
	}
}