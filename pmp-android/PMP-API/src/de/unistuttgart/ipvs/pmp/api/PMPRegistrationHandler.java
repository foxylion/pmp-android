package de.unistuttgart.ipvs.pmp.api;

public class PMPRegistrationHandler extends PMPHandler {
	public IPCRegistrationCommand handler;

	protected void onSuccess() {
		throw new UnsupportedOperationException();
	}

	protected void onFailure(String message) {
		throw new UnsupportedOperationException();
	}

	protected void onAlreadyRegistered() {
		throw new UnsupportedOperationException();
	}

	protected void onBindingFailed() {
		throw new UnsupportedOperationException();
	}
}