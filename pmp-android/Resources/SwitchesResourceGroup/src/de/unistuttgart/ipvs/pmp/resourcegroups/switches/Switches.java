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
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;

/**
 * Resource Group for typical switches in Android (wifi, etc.). Has the privacy settings
 * 
 * <ul>
 * <li>SwitchesResourceGroup.PRIVACY_SETTING_WIFI_SWITCH (true or false)</li>
 * </ul>
 * 
 * And the resources
 * 
 * <ul>
 * <li>SwitchesResourceGroup.RESOURCE_WIFI_SWITCH (IWifiSwitch)</li>
 * </ul>
 * 
 * @author Tobias Kuhn
 * 
 */
public class Switches extends ResourceGroup {
    
    public static final String PRIVACY_SETTING_WIFI_SWITCH = "CanWifiSwitch";
    public static final String RESOURCE_WIFI_SWITCH = "WifiSwitch";
    
    /**
     * Context for forwarding to resources
     */
    private Context context;
    
    
    public Switches(IPMPConnectionInterface pmpci) {
        super("de.unistuttgart.ipvs.pmp.resourcegroups.switches", pmpci);
        
        registerResource(RESOURCE_WIFI_SWITCH, new WifiSwitchResource());
    }
    
    
    public String getName(String locale) {
        return "Switches Resource Group";
    }
    
    
    public String getDescription(String locale) {
        return "Resource group for using the switches available in Android.";
    }
    
    
    protected String getServiceAndroidName() {
        return "de.unistuttgart.ipvs.pmp.resourcegroups.switches";
    }
    
    
    public void onRegistrationSuccess() {
        Log.d("Registration success.");
    }
    
    
    public void onRegistrationFailed(String message) {
        Log.e("Registration failed with \"" + message + "\"");
    }
    
    
    public void setContext(Context context) {
        this.context = context;
    }
    
    
    public Context getContext() {
        return this.context;
    }
    
}
