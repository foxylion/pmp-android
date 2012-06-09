/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeWSRessourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.resource.vHikeWebserviceResource;

public class vHikeWSResourceGroup extends ResourceGroup {
    
    public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS";
    
    public static final String R_vHIKE_WEBSERVICE = "vHikeWebserviceResource";
    
    public static final String PS_USE_vHIKE_WEBSERVICE = "usevHikeWS";
    public static final String PS_ANONYMOUS = "anonymous";
    
    
    public vHikeWSResourceGroup(IPMPConnectionInterface pmpci) {
        super(PACKAGE_NAME, pmpci);
        
        registerResource(R_vHIKE_WEBSERVICE, new vHikeWebserviceResource(this));
        Log.i(this, "registerResource");
        registerPrivacySetting(PS_USE_vHIKE_WEBSERVICE, new BooleanPrivacySetting());
        registerPrivacySetting(PS_ANONYMOUS, new BooleanPrivacySetting());
    }
    
}
