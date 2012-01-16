package de.unistuttgart.ipvs.pmp.api.handler;

import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.ipc.command.IPCRequestResourceCommand;

public class PMPRequestResourceHandler extends PMPHandler {
	public IPCRequestResourceCommand handler;

	protected void onReceiveResource(PMPResourceIdentifier res) {
		throw new UnsupportedOperationException();
	}
}