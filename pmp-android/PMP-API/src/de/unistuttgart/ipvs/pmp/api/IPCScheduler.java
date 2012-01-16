package de.unistuttgart.ipvs.pmp.api;

public class IPCScheduler extends Thread {
	public PMP scheduler;
	public IPCCommand queue;
	public IPCConnection connection;

	protected void queue(IPCCommand command) {
		throw new UnsupportedOperationException();
	}

	public void run() {
		throw new UnsupportedOperationException();
	}
}