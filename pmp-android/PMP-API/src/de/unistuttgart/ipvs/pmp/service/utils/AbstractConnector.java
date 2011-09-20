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
import android.os.IInterface;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.service.INullService;

/**
 * {@link AbstractConnector} is used for connecting (in this case binding) to services. Add your
 * {@link IConnectorCallback} for interacting with the service. Call {@link AbstractConnector#bind} to start the
 * connection.
 * 
 * @author Jakob Jarosch
 */
public abstract class AbstractConnector {
    
    public enum ConnectionState {
        CONNECTED,
        DISCONNECTED,
        BINDING_FAILED
    };
    
    private AbstractConnector self = this;
    
    /**
     * Reference to the connected service {@link IBinder}.
     */
    private IBinder connectedService = null;
    
    /**
     * Set to true if a {@link Service} has connected.
     */
    private boolean connected = false;
    
    /**
     * List of all {@link IConnectorCallback} handler which will receive a connection message.
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
    private Semaphore semaphore = new Semaphore(0);
    
    /**
     * The {@link ServiceConnection} is used to handle the bound Service {@link IBinder}.
     */
    protected ServiceConnection serviceConnection = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("AbstractConnector - service disconnected: " + AbstractConnector.this.targetIdentifier);
            
            AbstractConnector.this.connectedService = null;
            AbstractConnector.this.connected = false;
            informCallback(ConnectionState.DISCONNECTED);
        }
        
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            
            AbstractConnector.this.connectedService = service;
            AbstractConnector.this.connected = true;
            
            Log.v(AbstractConnector.this.self.getClass().getSimpleName() + " - Service connected, received the binder "
                    + getInterfaceDescriptor() + " for connected service " + AbstractConnector.this.targetIdentifier);
            
            serviceConnected();
            
            AbstractConnector.this.semaphore.release();
            
            informCallback(ConnectionState.CONNECTED);
        }
    };
    
    
    public AbstractConnector(Context context, PMPSignee signee, String targetIdentifier) {
        this.context = context;
        this.signee = signee;
        this.targetIdentifier = targetIdentifier;
        
        Log.v("Created AbstractConnector for " + signee.getType().toString() + "::" + signee.getIdentifier()
                + "; connection will go to " + targetIdentifier);
    }
    
    
    /**
     * Bind the {@link Service}.
     * 
     * @return Returns true if a binding is on the way, otherwise false.
     */
    public void bind() {
        if (!isBound()) {
            final Intent intent = new Intent(this.targetIdentifier);
            
            intent.putExtra(Constants.INTENT_TYPE, this.signee.getType());
            intent.putExtra(Constants.INTENT_IDENTIFIER, this.signee.getIdentifier());
            intent.putExtra(Constants.INTENT_SIGNATURE, this.signee.signContent(this.targetIdentifier.getBytes()));
            
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    if (AbstractConnector.this.context.bindService(intent, AbstractConnector.this.serviceConnection,
                            Context.BIND_AUTO_CREATE)) {
                        
                        Log.d("AbstractConnector successfully sent bind command to "
                                + AbstractConnector.this.targetIdentifier);
                    } else {
                        Log.d("AbstractConnector recognized that binding for "
                                + AbstractConnector.this.targetIdentifier + " has failed");
                        AbstractConnector.this.semaphore.release();
                    }
                }
            }).start();
        }
    }
    
    
    public boolean bind(boolean blocking) {
        if (blocking) {
            this.semaphore.drainPermits();
        }
        
        bind();
        
        if (blocking) {
            try {
                this.semaphore.acquire();
            } catch (InterruptedException e) {
                Log.e("Interrupted while waiting for bind success.", e);
            }
        }
        
        return isBound();
    }
    
    
    /**
     * Unbind the {@link Service}.
     */
    public void unbind() {
        this.context.unbindService(this.serviceConnection);
    }
    
    
    /**
     * @return true when the service is bound, false if not.
     */
    public boolean isBound() {
        return this.connected;
    }
    
    
    /**
     * Adds a {@link IConnectorCallback} instance to the list.
     * 
     * @param connectorCallback
     *            {@link IConnectorCallback} object which should be added
     */
    public void addCallbackHandler(IConnectorCallback connectorCallback) {
        this.callbackHandler.add(connectorCallback);
    }
    
    
    /**
     * Removes an {@link IConnectorCallback} instance from the list.
     * 
     * @param connectorCallback
     *            {@link IConnectorCallback} object which should be removed
     */
    public void removeCallbackHandler(IConnectorCallback connectorCallback) {
        this.callbackHandler.remove(connectorCallback);
    }
    
    
    /**
     * @return Returns the Service as {@link IBinder}, should be casted to the correct interface. Returns NULL if the
     *         Service returned a {@link INullService}- {@link IBinder}.
     */
    protected IBinder getService() {
        return (isCorrectBinder(INullService.class) ? null : this.connectedService);
    }
    
    
    public boolean isCorrectBinder(Class<? extends IInterface> binder) {
        return getInterfaceDescriptor().equals(binder.getName());
    }
    
    
    private String getInterfaceDescriptor() {
        try {
            return this.connectedService.getInterfaceDescriptor();
        } catch (RemoteException e) {
            Log.e("Got an RemoteException while checking the Type of the returned IBinder");
        }
        return "";
    }
    
    
    protected Context getContext() {
        return this.context;
    }
    
    
    /**
     * Is called when a connection to the service has been established. Note that all the required connection handling
     * is done in {@link AbstractConnector}.
     * 
     * @see {@link ServiceConnection#onServiceConnected(ComponentName, IBinder)}
     */
    protected abstract void serviceConnected();
    
    
    /**
     * Is called when a connection to the service has been lost. Note that all the required connection handling is done
     * in {@link AbstractConnector}.
     * 
     * @see {@link ServiceConnection#onServiceDisconnected(ComponentName)}
     */
    protected abstract void serviceDisconnected();
    
    
    /**
     * Inform all {@link IConnectorCallback} instances which are registered that something has changed.
     */
    private void informCallback(ConnectionState state) {
        for (IConnectorCallback handler : this.callbackHandler) {
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
