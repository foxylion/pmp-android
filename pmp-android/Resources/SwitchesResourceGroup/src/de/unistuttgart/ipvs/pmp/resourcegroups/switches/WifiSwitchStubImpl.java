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
package de.unistuttgart.ipvs.pmp.resourcegroups.switches;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resourcegroups.switches.IWifiSwitch.Stub;

/**
 * Implementation of the {@link IWifiSwitch} binder interface.
 * 
 * @author Tobias Kuhn
 * 
 */
public class WifiSwitchStubImpl extends Stub {
    
    private String appIdentifier;
    private WifiSwitchResource resource;
    private Context context;
    
    
    public WifiSwitchStubImpl(String appIdentifier, WifiSwitchResource resource, Context context) {
        this.appIdentifier = appIdentifier;
        this.resource = resource;
        this.context = context;
    }
    
    
    @Override
    public boolean getState() throws RemoteException {
        if (!verifyAccessAllowed()) {
            throw new SecurityException();
        }
        
        WifiManager wm = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        switch (wm.getWifiState()) {
            case WifiManager.WIFI_STATE_ENABLED:
            case WifiManager.WIFI_STATE_ENABLING:
                return true;
            default:
                return false;
        }
    }
    
    
    @Override
    public void setState(boolean newState) throws RemoteException {
        if (!verifyAccessAllowed()) {
            throw new SecurityException();
        }
        
        WifiManager wm = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(newState);
    }
    
    
    /**
     * Verifies that the access is allowed.
     * 
     * @return True, iff the access was allowed.
     */
    private boolean verifyAccessAllowed() {
        BooleanPrivacySetting bpl = (BooleanPrivacySetting) this.resource
                .getPrivacySetting(Switches.PRIVACY_SETTING_WIFI_SWITCH);
        try {
            return bpl.permits(this.appIdentifier, true);
        } catch (PrivacySettingValueException e) {
            e.printStackTrace();
            return false;
        }
    }
}
