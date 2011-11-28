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

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;

/**
 * {@link PMPServiceConnector} is used for connecting (in this case binding) to the PMP service. Add your
 * {@link IConnectorCallback} for interacting with the service. Call {@link PMPServiceConnector#bind} to start the
 * connection.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceConnector extends AbstractConnector {
    
    public PMPServiceConnector(Context context) {
        super(context, Constants.PMP_IDENTIFIER);
    }
    
    
    public IPMPService getAppService() {
        if (isCorrectBinder(IPMPService.class)) {
            return IPMPService.Stub.asInterface(getService());
        } else {
            return null;
        }
    }
    
    
    @Override
    protected void serviceConnected() {
        
    }
    
    
    @Override
    protected void serviceDisconnected() {
        
    }
    
}
