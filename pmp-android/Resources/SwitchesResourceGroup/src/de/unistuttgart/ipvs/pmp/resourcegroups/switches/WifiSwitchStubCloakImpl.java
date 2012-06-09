/*
 * Copyright 2012 pmp-android development team
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
package de.unistuttgart.ipvs.pmp.resourcegroups.switches;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.switches.IWifiSwitch.Stub;

/**
 * Implementation of the {@link IWifiSwitch} binder interface.
 * 
 * @author Tobias Kuhn
 * 
 */
public class WifiSwitchStubCloakImpl extends Stub {
    
    String appIdentifier;
    private WifiSwitchResource resource;
    
    
    public WifiSwitchStubCloakImpl(String appIdentifier, WifiSwitchResource resource) {
        this.appIdentifier = appIdentifier;
        this.resource = resource;
    }
    
    
    @Override
    public boolean getState() throws RemoteException {
        if (!this.resource.verifyAccessAllowed(this.appIdentifier, Switches.PRIVACY_SETTING_WIFI_STATE)) {
            throw new SecurityException();
        }
        
        return Math.random() > 0.5;
    }
    
    
    @Override
    public void setState(boolean newState) throws RemoteException {
        if (!this.resource.verifyAccessAllowed(this.appIdentifier, Switches.PRIVACY_SETTING_WIFI_SWITCH)) {
            throw new SecurityException();
        }
        
        // do nothing
    }
}
