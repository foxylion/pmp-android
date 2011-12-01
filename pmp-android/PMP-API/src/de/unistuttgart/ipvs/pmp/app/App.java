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
package de.unistuttgart.ipvs.pmp.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

/**
 * A PMP-compatible App that uses the parsed service levels.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class App extends Application {
    
    /**
     * This method is called when the service features are changed. The service features will automatically stored at
     * the {@link SharedPreferences}.
     * 
     * @param features
     *            the Bundle that contains the mappings of strings (the identifiers of the service features in your app
     *            description XML) to booleans (true for granted i.e. active, false for not granted)
     */
    public final void updateServiceFeatures(Bundle features) {
        SharedPreferences app_preferences = this.getSharedPreferences("serviceFeatures", 0);
        SharedPreferences.Editor editor = app_preferences.edit();
        
        // Storing all key value pairs at the preferences
        for (String key : features.keySet()) {
            // Putting the prefix in front of the key
            String prefixKey = Constants.SERVICE_FEATURE_PREFIX + key;
            editor.putBoolean(prefixKey, features.getBoolean(key));
            if (!editor.commit()) {
                Log.e("Service feature couldn't be stored");
            }
        }
    }
    
    
    /**
     * Registers this app with PMP. You must implement reacting to the result of this operation
     * by overriding {@link App#onRegistrationSuccess()} or {@link App#onRegistrationFailed(String)}.
     * 
     */
    protected void register() {
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(getApplicationContext());
        final String name = getApplicationContext().getPackageName();
        
        pmpsc.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect() {
                try {
                    IPMPService ipmps = pmpsc.getAppService();
                    if (!ipmps.isRegistered(name)) {
                        // register with PMP
                        ipmps.registerApp(name);
                    }
                } catch (RemoteException e) {
                    Log.e("RemoteException during registering app", e);
                }
                
                pmpsc.unbind();
            }
            
        });
        pmpsc.bind();
    }
    
    
    /**
     * Retrieves a resource from PMP in blocking mode, i.e. your app will block until this call has completed. You do
     * <b>not</b> have to implement receiveResource() for this call to work.
     * 
     * @param resourceGroup
     * @param resource
     * @return the interface for the resource of the resourceGroup specified, or null, if an error happened (e.g.
     *         resource not found)
     */
    protected IBinder getResourceBlocking(final String resourceGroup, final String resource) {
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(getApplicationContext());
        final String name = getApplicationContext().getPackageName();
        final ResultObject<IBinder> result = new ResultObject<IBinder>();
        
        pmpsc.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect() {
                try {
                    IPMPService ipmps = pmpsc.getAppService();
                    if (!ipmps.isRegistered(name)) {
                        result.result = null;
                    } else {
                        result.result = ipmps.getRessource(name, resourceGroup, resource);
                    }
                } catch (RemoteException e) {
                    Log.e("RemoteException during registering app", e);
                }
                
                pmpsc.unbind();
            }
            
        });
        pmpsc.bind(true);
        
        return result.result;
    }
    
    
    /**
     * <p>
     * Retrieves a resource from PMP in non-blocking mode, i.e. your app will <b>not</b> block until this call has
     * completed. This implies you have to override {@link App#receiveResource(String, String, IBinder)} in order to
     * receive the result of the non-blocking concurrent call.
     * </p>
     * 
     * <p>
     * Simply said: Call this method, it will return immediately, and then receive the actual binder in
     * receiveResource().
     * </p>
     * 
     * @param resourceGroup
     * @param resource
     */
    protected void getResourceNonblocking(final String resourceGroup, final String resource) {
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(getApplicationContext());
        final String name = getApplicationContext().getPackageName();
        
        pmpsc.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect() {
                try {
                    IPMPService ipmps = pmpsc.getAppService();
                    if (!ipmps.isRegistered(name)) {
                        receiveResource(resourceGroup, resource, null);
                    } else {
                        receiveResource(resourceGroup, resource, ipmps.getRessource(name, resourceGroup, resource));
                    }
                } catch (RemoteException e) {
                    Log.e("RemoteException during registering app", e);
                }
                
                pmpsc.unbind();
            }
            
        });
        pmpsc.bind(true);
    }
    
    
    /**
     * <p>
     * Receives the result of a non-blocking resource request from PMP. This implies you have called
     * {@link App#getResourceNonblocking(String, String)} in order to start the non-blocking request.
     * </p>
     * 
     * <p>
     * Simply said: Call getResourceNonblocking(), it will return immediately, and then receive the actual binder in
     * this method.
     * </p>
     * 
     * @param resourceGroup
     * @param resource
     * @param binder
     *            the interface for the resource of the resourceGroup specified, or null, if an error happened (e.g.
     *            resource not found)
     */
    protected void receiveResource(String resourceGroup, String resource, IBinder binder) {
        // override me
    }
    
    
    /**
     * Callback called when the preceding call to register() registered this app successfully with PMP.
     */
    public abstract void onRegistrationSuccess();
    
    
    /**
     * Callback called when the preceding call to register() could not register this app with PMP due to errors.
     * 
     * @param message
     *            returned error message from the PMP service
     */
    public abstract void onRegistrationFailed(String message);
    
    /**
     * Quick and dirty result object to use in methods in anonymous event handlers.
     * 
     * @author Tobias Kuhn
     * 
     * @param <T>
     */
    private static class ResultObject<T> {
        
        protected T result;
    }
    
    
    /**
     * Checks if a service feature is enabled or not
     * 
     * @param featureIdentifier
     *            the identifier of the service feature
     * @return true if the service feature is enabled, false if not enabled, false if the identifier doesn't exist
     */
    public final Boolean isServiceFeatureEnabled(String featureIdentifier) {
        // Putting the prefix in front the key
        String prefixKey = Constants.SERVICE_FEATURE_PREFIX + featureIdentifier;
        SharedPreferences app_preferences = this.getSharedPreferences("serviceFeatures", 0);
        return app_preferences.getBoolean(prefixKey, false);
    }
    
}
