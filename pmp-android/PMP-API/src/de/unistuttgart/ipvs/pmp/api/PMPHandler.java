package de.unistuttgart.ipvs.pmp.api;

public abstract class PMPHandler {

	protected void onPrepare() {
		throw new UnsupportedOperationException();
	}

	protected void onTimeout() {
		throw new UnsupportedOperationException();
	}

	protected void onFinalize() {
		throw new UnsupportedOperationException();
	}
}