/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import de.unistuttgart.ipvs.pmp.Log;

/**
 * {@link AbstractConnector} is used for connecting (in this case binding) to services. Add your
 * {@link AbstractConnectorCallback} for interacting with the service. Call {@link AbstractConnector#bind} to start the
 * connection.
 * 
 * @author Jakob Jarosch
 */
@Deprecated
public abstract class AbstractConnector {
    
    /**
     * The {@link ServiceConnection} used by this {@link AbstractConnector}.
     * 
     * @author Tobias Kuhn
     * 
     */
    @Deprecated
    private final class ConnectorServiceConnection implements ServiceConnection {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(this + " was disconnected from service for " + AbstractConnector.this.targetIdentifier);
            
            AbstractConnector.this.connectedService = null;
            AbstractConnector.this.connected = false;
            informCallback(ConnectionState.DISCONNECTED);
        }
        
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            
            AbstractConnector.this.connectedService = service;
            AbstractConnector.this.connected = true;
            
            Log.v(this + " was connected to the service " + AbstractConnector.this.targetIdentifier + " with binder "
                    + getInterfaceDescriptor() + ".");
            
            serviceConnected();
            
            informCallback(ConnectionState.CONNECTED);
            
            AbstractConnector.this.semaphore.release();
        }
        
        
        @Override
        public String toString() {
            // avoid all the package names
            return getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
        }
    }
    
    @Deprecated
    public enum ConnectionState {
        CONNECTED,
        DISCONNECTED,
        BINDING_FAILED
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
     * List of all {@link AbstractConnectorCallback} handler which will receive a connection message.
     */
    private List<AbstractConnectorCallback> callbackHandler = new ArrayList<AbstractConnectorCallback>();
    
    /**
     * The context used to initiate the binding.
     */
    private Context context;
    
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
    protected ServiceConnection serviceConnection = new ConnectorServiceConnection();
    
    
    @Override
    public String toString() {
        // avoid all the package names
        return getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
    }
    
    
    public AbstractConnector(Context context, String targetIdentifier) {
        this.context = context;
        this.targetIdentifier = targetIdentifier;
        
        Log.v(this + " for " + targetIdentifier + " requested.");
    }
    
    
    /**
     * Binds the {@link Service} asynchronously.
     */
    @Deprecated
    public void bind() {
        if (!isBound()) {
            final Intent intent = new Intent(this.targetIdentifier);
            
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    if (AbstractConnector.this.context.bindService(intent, AbstractConnector.this.serviceConnection,
                            Context.BIND_AUTO_CREATE)) {
                        
                        Log.d(AbstractConnector.this + " bound to " + AbstractConnector.this.targetIdentifier);
                    } else {
                        Log.d(AbstractConnector.this + " failed binding to " + AbstractConnector.this.targetIdentifier);
                        informCallback(ConnectionState.BINDING_FAILED);
                        AbstractConnector.this.semaphore.release();
                    }
                }
            }).start();
        }
    }
    
    
    @Deprecated
    public boolean bind(boolean blocking) {
        if (blocking) {
            this.semaphore.drainPermits();
        }
        
        bind();
        
        if (blocking) {
            try {
                this.semaphore.acquire();
            } catch (InterruptedException ie) {
                Log.e(this + " was interrupted while waiting for binding.", ie);
            }
        }
        
        return isBound();
    }
    
    
    /**
     * Unbind the {@link Service}.
     */
    @Deprecated
    public void unbind() {
        if (isBound()) {
            this.context.unbindService(this.serviceConnection);
            this.connected = false;
        }
    }
    
    
    /**
     * @return true when the service is bound, false if not.
     */
    @Deprecated
    public boolean isBound() {
        return this.connected;
    }
    
    
    /**
     * Adds a {@link AbstractConnectorCallback} instance to the list.
     * 
     * @param connectorCallback
     *            {@link AbstractConnectorCallback} object which should be added
     */
    @Deprecated
    public void addCallbackHandler(AbstractConnectorCallback connectorCallback) {
        this.callbackHandler.add(connectorCallback);
    }
    
    
    /**
     * Removes an {@link AbstractConnectorCallback} instance from the list.
     * 
     * @param connectorCallback
     *            {@link AbstractConnectorCallback} object which should be removed
     */
    @Deprecated
    public void removeCallbackHandler(AbstractConnectorCallback connectorCallback) {
        this.callbackHandler.remove(connectorCallback);
    }
    
    
    /**
     * @return Returns the Service as {@link IBinder}, should be casted to the correct interface. Returns null if the
     *         Service returned a {@link INullService} - {@link IBinder}.
     */
    protected IBinder getService() {
        return this.connectedService;
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
     * Inform all {@link AbstractConnectorCallback} instances which are registered that something has changed.
     */
    private void informCallback(ConnectionState state) {
        for (AbstractConnectorCallback handler : this.callbackHandler) {
            switch (state) {
                case CONNECTED:
                    try {
                        handler.onConnect(this);
                    } catch (RemoteException re) {
                        Log.e("Remote exception during connected callback handling.", re);
                    }
                    unbind();
                    break;
                
                case DISCONNECTED:
                    handler.onDisconnect(this);
                    break;
                
                case BINDING_FAILED:
                    handler.onBindingFailed(this);
                    break;
            }
        }
    }
}
