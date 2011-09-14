package de.unistuttgart.ipvs.pmp.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;

/**
 * {@link AbstractConnector} is used for connecting (in this case binding) to
 * services. Add your {@link IConnectorCallback} for interacting with the
 * service. Call {@link AbstractConnector#bind} to start the connection.
 * 
 * @author Jakob Jarosch
 */
public abstract class AbstractConnector {

    public enum ConnectionState {
	CONNECTED, DISCONNECTED, BINDING_FAILED
    };

    /**
     * Reference to the connected service {@link IBinder}.
     */
    private IBinder connectedService = null;

    /**
     * Set to true if a {@link Service} has connected.
     */
    private boolean connected = false;

    /**
     * List of all {@link IConnectorCallback} handler which will receive a
     * connection message.
     */
    private List<IConnectorCallback> callbackHandler = new ArrayList<IConnectorCallback>();

    /**
     * The context used to initiate the binding.
     */
    private Context context;

    /**
     * The signee used to sign the connection to the service.
     */
    private PMPSignee signee;

    /**
     * The identifier of the service to which the connection should go.
     */
    private String targetIdentifier;
    
    /**
     * If set to true, the bind will be in blocking mode.
     */
    private boolean blocking = false;
    private Semaphore semaphore = new Semaphore(0);

    /**
     * The {@link ServiceConnection} is used to handle the bound Service
     * {@link IBinder}.
     */
    protected ServiceConnection serviceConnection = new ServiceConnection() {

	@Override
	public void onServiceDisconnected(ComponentName name) {
	    connectedService = null;
	    connected = false;
	    informCallback(ConnectionState.DISCONNECTED);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
	    connectedService = service;
	    connected = true;
	    
	    serviceConnected();
	    
	    if(blocking) {
		semaphore.release();
	    }
	    
	    informCallback(ConnectionState.CONNECTED);
	}
    };

    public AbstractConnector(Context context, PMPSignee signee,
	    String targetIdentifier) {
	this.context = context;
	this.signee = signee;
	this.targetIdentifier = targetIdentifier;
    }

    /**
     * Bind the {@link Service}.
     * 
     * @return Returns true if a binding is on the way, otherwise false.
     */
    public boolean bind() {
	if (!isBound()) {
	    Intent intent = new Intent(targetIdentifier);

	    intent.putExtra(Constants.INTENT_TYPE, this.signee.getType());
	    intent.putExtra(Constants.INTENT_IDENTIFIER,
		    this.signee.getIdentifier());
	    intent.putExtra(Constants.INTENT_SIGNATURE,
		    this.signee.signContent(targetIdentifier.getBytes()));

	    if (!context.bindService(intent, serviceConnection,
		    Context.BIND_AUTO_CREATE)) {
		informCallback(ConnectionState.BINDING_FAILED);
		return false;
	    }
	}
	return true;
    }
    
    public boolean bind(boolean blocking) {
	this.blocking = blocking;
	
	boolean result = bind();
	
	if(blocking && result == true) {
	    try {
		semaphore.acquire();
	    } catch (InterruptedException e) {
		Log.e("Interrupted while waiting for bind success.", e);
	    }
	}
	
	return result;
    }

    /**
     * Unbind the {@link Service}.
     */
    public void unbind() {
	context.unbindService(serviceConnection);
    }

    /**
     * @return true when the service is bound, false if not.
     */
    public boolean isBound() {
	return connected;
    }

    /**
     * Adds a {@link IConnectorCallback} instance to the list.
     * 
     * @param connectorCallback
     *            {@link IConnectorCallback} object which should be added
     */
    public void addCallbackHandler(IConnectorCallback connectorCallback) {
	callbackHandler.add(connectorCallback);
    }

    /**
     * Removes an {@link IConnectorCallback} instance from the list.
     * 
     * @param connectorCallback
     *            {@link IConnectorCallback} object which should be removed
     */
    public void removeCallbackHandler(IConnectorCallback connectorCallback) {
	callbackHandler.remove(connectorCallback);
    }

    /**
     * @return Returns the Service as {@link IBinder}, should be casted to the
     *         correct interface. Returns NULL if the Service returned a NULL-
     *         {@link IBinder}.
     */
    protected IBinder getService() {
	return connectedService;
    }

    protected Context getContext() {
	return context;
    }

    /**
     * Is called when a connection to the service has been established. Note
     * that all the required connection handling is done in
     * {@link AbstractConnector}.
     * 
     * @see {@link ServiceConnection#onServiceConnected(ComponentName, IBinder)}
     */
    protected abstract void serviceConnected();

    /**
     * Is called when a connection to the service has been lost. Note that all
     * the required connection handling is done in {@link AbstractConnector}.
     * 
     * @see {@link ServiceConnection#onServiceDisconnected(ComponentName)}
     */
    protected abstract void serviceDisconnected();

    /**
     * Inform all {@link IConnectorCallback} instances which are registered that
     * something has changed.
     */
    private void informCallback(ConnectionState state) {
	for (IConnectorCallback handler : callbackHandler) {
	    switch (state) {
	    case CONNECTED:
		handler.connected();
		break;

	    case DISCONNECTED:
		handler.disconnected();
		break;

	    case BINDING_FAILED:
		handler.bindingFailed();
		break;
	    }
	}
    }
}
