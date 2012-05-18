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

	BluetoothAdapter btAdapter = null;

	AcceptThread acceptThread = null;
	ConnectThread connectThread = null;
	ConnectedThread connectedThread = null;
	
	List<String> messages = null;
	private static final UUID MY_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	private static final String NAME = "BluetoothChat";

	boolean discovering = false;
	boolean makingDiscoverable = false;

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
	
	public static final int NO_ROLE = 0;
	public static final int DRIVER_ROLE = 1;
	public static final int PASSENGER_ROLE=2;
	
	public BluetoothResource(BluetoothResourceGroup blueRG) {
		Looper.prepare();
		this.blueRG = blueRG;
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		devices = new ArrayList<BluetoothDevice>();
		messages = new ArrayList<String>();

	}

	private void init() {
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}
		
		if (acceptThread == null) {
			acceptThread = new AcceptThread();
			acceptThread.start();
		}
		
		setState(STATE_LISTEN);

	}
	
	public void connect(String address) throws RemoteException {

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (connectThread != null) {
				connectThread.cancel();
				connectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		Log.i(TAG, "Connect to: " + address);
		
		BluetoothDevice device = btAdapter.getRemoteDevice(address);

		connectThread = new ConnectThread(device);
		connectThread.start();
		setState(STATE_CONNECTING);
	}
	
	private void connected(BluetoothSocket socket, BluetoothDevice remoteDevice) {

		// Cancel the thread that completed the connection
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		// Cancel any thread currently running a connection
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		// Cancel the accept thread because we only want to connect to one
		// device
		if (acceptThread != null) {
			acceptThread.cancel();
			acceptThread = null;
		}

		connectedThread = new ConnectedThread(socket);
		connectedThread.start();
		setState(STATE_CONNECTED);
	}

	public synchronized void stop() {

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        setState(STATE_NONE);
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
	
	private void connectionLost() {
		init();
	}

	private void connectionFailed() {
		init();
	}
	
	private void setState(int state) {
		mState = state;
	}

	public void setBroadcast(String appIdentifier) {
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
			stop();
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

			ROLE = DRIVER_ROLE;
		}
		init();
		setState(STATE_LISTEN);
		
		
	}

	public void discover(String appIdentifier) throws RemoteException {
		
		if (btAdapter.isDiscovering()) {
			btAdapter.cancelDiscovery();
		}
		init();
		btAdapter.startDiscovery();
		Log.i("BluetoothResource", "DISCOVER");
		setBroadcast(appIdentifier);
		discovering = true;
		devices.clear();
		ROLE = PASSENGER_ROLE;
		
		
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
			}else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				try {
					if(ROLE == PASSENGER_ROLE){
					}else if(ROLE == DRIVER_ROLE){
						connect(device.getAddress());
					}
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public String TAG = "BluetoothResource";

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
			while (mState != STATE_CONNECTED) {
				// Listen to the server socket if we're not connected
				try {
					Log.i("BLUETOOTHRESOURCE", "START ACCEPT CLIENTS");
					socket = serverSocket.accept();
					Log.i("BLUETOOTHRESOURCE", "ACCEPTED CLIENTS");
					// If a connection was accepted
					if (socket != null) {
						switch (mState) {
						case STATE_LISTEN:
						case STATE_CONNECTING:
							// Situation normal. Start the connected thread.
							connected(socket, socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							// Either not ready or already connected. Terminate
							// new socket.
							try {
								socket.close();
								Log.e("AcceptedThread: " + this.getClass(),
										"Close socket! 1");
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}

					}
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

		public void cancel() {
			try {
				serverSocket.close();
				Log.e("AcceptedThread: " + this.getClass(),
						"Close socket! 2");
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
					Log.e("ConnectThread: " + this.getClass(),
							"Close socket! 1");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				connectionFailed();
				return;
			}
			synchronized (BluetoothResource.this) {
				connectedThread = null;
			}

			connected(socket, device);
		}

		public void cancel() {
			try {
				socket.close();
				Log.e("ConnectThread: " + this.getClass(),
						"Close socket! 2");
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

					String msg =  new String(buffer, 0, bytes);
					synchronized(messages){
						messages.add(msg);
					}
					
					Log.i(TAG  +" ConnectedThread" , "read: Adding message: " + msg);

				} catch (IOException e) {
					e.printStackTrace();
					connectionLost();
					break;
				}
			}
		}

		public void write(byte[] buffer) {
			try {
				out.write(buffer);

				String msg = new String(buffer);
				synchronized(messages){
					messages.add(msg);
				}
				Log.i(TAG  +" ConnectedThread" , "write: Adding message: "+ msg);

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

	public boolean isMakingDiscoverable() {
		if (mState == STATE_LISTEN)
			return true;
		else
			return false;
	}

	public boolean isDeviceBonded(String address) {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		List<String> pairedDevicesList = new ArrayList<String>();
		boolean bonded = false; 
		for (BluetoothDevice device : pairedDevices) {
			if(device.getAddress().equals(address)){
				bonded = true;
			}
		}
		
		return bonded;
	}
}