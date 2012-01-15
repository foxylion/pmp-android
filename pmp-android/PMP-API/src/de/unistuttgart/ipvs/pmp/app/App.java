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

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

/**
 * A PMP-compatible App that uses the parsed service features.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class App extends Application {
    
    /**
     * Quick and dirty result object to use in methods in anonymous event handlers.
     * 
     * @author Tobias Kuhn
     * 
     * @param <T>
     */
    private static final class ResultObject<T> {
        
        protected T result;
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
     * Registers this app with PMP. You must implement reacting to the result of this operation
     * by overriding {@link App#onRegistrationSuccess()} or {@link App#onRegistrationFailed(String)}.
     * 
     */
    public final void register() {
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(getApplicationContext());
        final String name = getApplicationContext().getPackageName();
        
        pmpsc.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IPMPService ipmps = pmpsc.getAppService();
                if (!ipmps.isRegistered(name)) {
                    // register with PMP
                    ipmps.registerApp(name);
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                onRegistrationFailed("PMP not found on device.");
            }
            
        });
        pmpsc.bind();
    }
    
    
    /**
     * This method is called when the service features are changed. The service features will automatically stored at
     * the {@link SharedPreferences}.
     * 
     * @param features
     *            the Bundle that contains the mappings of strings (the identifiers of the service features in your app
     *            description XML) to booleans (true for granted i.e. active, false for not granted)
     */
    public final void updateServiceFeatures(Bundle features) {
        SharedPreferences app_preferences = getSharedPreferences("serviceFeatures", 0);
        SharedPreferences.Editor editor = app_preferences.edit();
        
        // Storing all key value pairs at the preferences
        for (String key : features.keySet()) {
            // Putting the prefix in front of the key
            String prefixKey = Constants.SERVICE_FEATURE_PREFIX + key;
            editor.putBoolean(prefixKey, features.getBoolean(key));
            Log.v("Storing ServiceFeature " + key + " : " + String.valueOf(features.getBoolean(key)));
            if (!editor.commit()) {
                Log.e("Service feature " + key + " couldn't be stored");
            }
        }
    }
    
    
    private void assertNotMainThread() {
        if (Thread.currentThread().getName().equalsIgnoreCase("main")) {
            throw new IllegalThreadStateException("You may not call the parent method in the main thread!");
        }
        
    }
    
    
    /**
     * <p>
     * Retrieves a resource from PMP in blocking mode, i.e. your app will block until this call has completed. You do
     * <b>not</b> have to implement receiveResource() for this call to work.
     * </p>
     * 
     * <p>
     * Notice that you must not call this method in the main thread as it will cause a dead lock.
     * </p>
     * 
     * @param resourceGroup
     * @param resource
     * @return the interface for the resource of the resourceGroup specified, or null, if an error happened (e.g.
     *         resource not found)
     */
    public final IBinder getResourceBlocking(final String resourceGroup, final String resource) {
        assertNotMainThread();
        
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(getApplicationContext());
        final String name = getApplicationContext().getPackageName();
        final ResultObject<IBinder> result = new ResultObject<IBinder>();
        
        pmpsc.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IPMPService ipmps = pmpsc.getAppService();
                if (!ipmps.isRegistered(name)) {
                    result.result = null;
                } else {
                    result.result = ipmps.getResource(name, resourceGroup, resource);
                }
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
    public final void getResourceNonblocking(final String resourceGroup, final String resource) {
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(getApplicationContext());
        final String name = getApplicationContext().getPackageName();
        
        pmpsc.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IPMPService ipmps = pmpsc.getAppService();
                if (!ipmps.isRegistered(name)) {
                    receiveResource(resourceGroup, resource, null);
                } else {
                    receiveResource(resourceGroup, resource, ipmps.getResource(name, resourceGroup, resource));
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                receiveResource(resourceGroup, resource, null);
            }
            
        });
        pmpsc.bind();
    }
    
    
    /**
     * <p>
     * Forces to publish a complete update of enabled or disabled service features. In case your app suddenly receives a
     * {@link SecurityException} from a {@link Resource}, it is highly recommended to ensure you have the valid PMP
     * privacy settings.
     * </p>
     * 
     * <p>
     * Notice that you must not call this method in the main thread as it will cause a dead lock.
     * </p>
     * 
     * @return true, if the request was sent, false, if the app is not registered
     */
    public final boolean requestServiceFeatureUpdate() {
        assertNotMainThread();
        
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(getApplicationContext());
        final String name = getApplicationContext().getPackageName();
        final ResultObject<Boolean> result = new ResultObject<Boolean>();
        
        pmpsc.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IPMPService ipmps = pmpsc.getAppService();
                if (!ipmps.isRegistered(name)) {
                    result.result = false;
                } else {
                    ipmps.getServiceFeatureUpdate(name);
                    result.result = true;
                }
            }
            
        });
        pmpsc.bind(true);
        
        return result.result;
    }
    
    
    /**
     * Sends a request to PMP to urge the user to activate these service features.
     * 
     * @param features
     *            the features which shall be requested
     */
    public final void requestServiceFeatures(final String... features) {
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(getApplicationContext());
        final String name = getApplicationContext().getPackageName();
        
        pmpsc.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IPMPService ipmps = pmpsc.getAppService();
                if (ipmps.isRegistered(name)) {
                    ipmps.requestServiceFeature(name, features);
                }
            }
            
        });
        pmpsc.bind();
    }
    
    
    /**
     * Sends a request to PMP to urge the user to activate these service features.
     * 
     * @param features
     *            the features which shall be requested
     */
    public final void requestServiceFeatures(List<String> features) {
        requestServiceFeatures(features.toArray(new String[features.size()]));
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
        SharedPreferences app_preferences = getSharedPreferences("serviceFeatures", 0);
        return app_preferences.getBoolean(prefixKey, false);
    }
    
    
    /**
     * Checks if a bunch of service features is enabled or not
     * 
     * @param featureIdentifiers
     *            the identifiers of the service feature
     * @return true if all the service features are enabled and exist, false otherwise
     */
    public final boolean areServiceFeaturesEnabled(String... featureIdentifiers) {
        for (String featureIdentifier : featureIdentifiers) {
            if (!isServiceFeatureEnabled(featureIdentifier)) {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Checks if a bunch of service features is enabled or not
     * 
     * @param featureIdentifiers
     *            the identifiers of the service feature
     * @return true if all the service features are enabled and exist, false otherwise
     */
    public final boolean areServiceFeaturesEnabled(List<String> featureIdentifiers) {
        for (String featureIdentifier : featureIdentifiers) {
            if (!isServiceFeatureEnabled(featureIdentifier)) {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Finds the available service features of a list
     * 
     * @param ofFeatures
     *            the features to check
     * @return the features in ofFeatures which are actually enabled and exist
     */
    public final List<String> listAvailableServiceFeatures(String... ofFeatures) {
        List<String> result = new ArrayList<String>();
        
        for (String featureIdentifier : ofFeatures) {
            if (isServiceFeatureEnabled(featureIdentifier)) {
                result.add(featureIdentifier);
            }
        }
        return result;
    }
    
    
    /**
     * Finds the available service features of a list
     * 
     * @param ofFeatures
     *            the features to check
     * @return the features in ofFeatures which are actually enabled and exist
     */
    public final List<String> listAvailableServiceFeatures(List<String> ofFeatures) {
        List<String> result = new ArrayList<String>();
        
        for (String featureIdentifier : ofFeatures) {
            if (isServiceFeatureEnabled(featureIdentifier)) {
                result.add(featureIdentifier);
            }
        }
        return result;
    }
    
    
    /**
     * Finds the available service features of a list
     * 
     * @param ofFeatures
     *            the features to check
     * @return the features in ofFeatures which are actually enabled and exist
     */
    public final List<String> listUnavailableServiceFeatures(String... ofFeatures) {
        List<String> result = new ArrayList<String>();
        
        for (String featureIdentifier : ofFeatures) {
            if (!isServiceFeatureEnabled(featureIdentifier)) {
                result.add(featureIdentifier);
            }
        }
        return result;
    }
    
    
    /**
     * Finds the available service features of a list
     * 
     * @param ofFeatures
     *            the features to check
     * @return the features in ofFeatures which are actually enabled and exist
     */
    public final List<String> listUnavailableServiceFeatures(List<String> ofFeatures) {
        List<String> result = new ArrayList<String>();
        
        for (String featureIdentifier : ofFeatures) {
            if (!isServiceFeatureEnabled(featureIdentifier)) {
                result.add(featureIdentifier);
            }
        }
        return result;
    }
}
