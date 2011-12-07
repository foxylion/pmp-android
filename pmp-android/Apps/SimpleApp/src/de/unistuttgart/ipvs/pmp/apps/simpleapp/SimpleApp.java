/*
 * Copyright 2011 pmp-android development team
 * Project: SimpleApp
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
package de.unistuttgart.ipvs.pmp.apps.simpleapp;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class SimpleApp extends App {
    
    static {
        Log.setTagSufix("SimpleApp");
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.unistuttgart.ipvs.pmp.app.App#onRegistrationSuccess() Is called
     * when the registration was successful. The method then tries to receive
     * the initial service level from the PMP service.
     */
    @Override
    public void onRegistrationSuccess() {
        Log.d("Registration succeed");
        
        // Connector to get the initial service level
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(getApplicationContext());
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                pmpconnector.getAppService().getServiceFeatureUpdate(getPackageName());
            }
        });
        
        // Connect to the service
        pmpconnector.bind();
    }
    
    
    @Override
    public void onRegistrationFailed(String message) {
        Log.d("Registration failed:" + message);
    }
    
    
    /**
     * Changes the functionality of the app according to its set ServiceLevel
     */
    public void changeFunctionalityAccordingToServiceLevel() {
        // TODO   
    }

}
