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

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;

/**
 * {@link Resource} for the Wifi Switch.
 * 
 * @author Tobias Kuhn
 * 
 */
public class WifiSwitchResource extends Resource {
    
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        // we want to pass some value from the RG
        Switches srg = (Switches) getResourceGroup();
        return new WifiSwitchStubNormalImpl(appIdentifier, this, srg.getContext(appIdentifier));
    }
    
    
    @Override
    public IBinder getMockedAndroidInterface(String appIdentifier) {
        return new WifiSwitchStubMockImpl(appIdentifier, this);
    }
    
    
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        return new WifiSwitchStubCloakImpl(appIdentifier, this);
    }
    
    
    /**
     * Verifies that the access is allowed.
     * 
     * @return True, iff the access was allowed.
     */
    boolean verifyAccessAllowed(String appPackage, String privacySetting) {
        BooleanPrivacySetting bpl = (BooleanPrivacySetting) getPrivacySetting(privacySetting);
        try {
            return bpl.permits(appPackage, true);
        } catch (PrivacySettingValueException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
