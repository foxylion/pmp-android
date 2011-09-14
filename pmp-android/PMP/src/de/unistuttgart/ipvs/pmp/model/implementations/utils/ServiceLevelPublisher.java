package de.unistuttgart.ipvs.pmp.model.implementations.utils;

import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;

public class ServiceLevelPublisher {

    private IApp app;
    private boolean asynchronous;

    public ServiceLevelPublisher(IApp app, boolean asynchronous) {
	this.app = app;
	this.asynchronous = asynchronous;
    }

    public void publish() {
	if (asynchronous) {
	    new Thread(new Runnable() {

		@Override
		public void run() {
		    executePublishing();
		}
	    }).start();
	} else {
	    executePublishing();
	}
    }
    
    private void executePublishing() {
	
    }
}
