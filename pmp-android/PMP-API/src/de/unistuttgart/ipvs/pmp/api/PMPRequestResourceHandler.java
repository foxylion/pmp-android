package de.unistuttgart.ipvs.pmp.api;

public class PMPRequestResourceHandler extends PMPHandler {
	public IPCRequestResourceCommand handler;

	protected void onReceiveResource(PMPResourceIdentifier res) {
		throw new UnsupportedOperationException();
	}
}