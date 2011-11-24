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
import android.content.Context;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

/**
 * A PMP-compatible App that uses the parsed service levels.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class App extends Application {
    
    /**
     * Overwrite this method to react whenever PMP changes your active service level. When not received any service
     * level yet, assume you are on level 0.
     * 
     * @param level
     *            the new service level level according to your specification in {@link App#getXMLInputStream()}.
     */
    public abstract void setActiveServiceLevel(int level);
    
    
    // TODO fix this
    
    /**
     * Effectively starts this app and registers it with PMP. You can implement reacting to the result of this operation
     * by implementing onRegistrationSuccess() or onRegistrationFailed()
     * 
     * @param context
     *            {@link Context} to use for the connection
     * 
     */
    public void register(Context context) {
        
        // connect to PMP
        final PMPServiceConnector pmpsc = new PMPServiceConnector(context);
        
        pmpsc.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
            }
            
            
            @Override
            public void connected() {
                try {
                    IPMPServiceApp ipmpsa = pmpsc.getAppService();
                    if (!ipmpsa.isRegistered()) {
                        // register with PMP
                        ipmpsa.registerApp();
                        
                    }
                } catch (RemoteException e) {
                    Log.e("RemoteException during registering app", e);
                }
                
                pmpsc.unbind();
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e("Binding failed during registering app.");
            }
        });
        pmpsc.bind();
    }
    
    
    /**
     * Callback called when the preceding call to start() registered this app successfully with PMP.
     */
    public abstract void onRegistrationSuccess();
    
    
    /**
     * Callback called when the preceding call to start() could not register this app with PMP due to errors.
     * 
     * @param message
     *            returned message from the PMP service
     */
    public abstract void onRegistrationFailed(String message);
    
}
