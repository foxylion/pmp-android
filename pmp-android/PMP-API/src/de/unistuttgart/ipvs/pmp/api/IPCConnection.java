package de.unistuttgart.ipvs.pmp.api;

public class IPCConnection {
	private boolean connected;
	private String destinationService;
	public IPCScheduler connection;

	public void onServiceDisconnected(ComponentName name) {
		throw new UnsupportedOperationException();
	}

	public void onServiceConnected(ComponentName name) {
		throw new UnsupportedOperationException();
	}

	public void setDestinationService(String service) {
		throw new UnsupportedOperationException();
	}

	public IBinder getBinder() {
		throw new UnsupportedOperationException();
	}

	private boolean connect() {
		throw new UnsupportedOperationException();
	}

	public void disconnect() {
		throw new UnsupportedOperationException();
	}
}