/*
 * Copyright 2012 pmp-android development team
 * Project: EnergyResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

/**
 * This is the validator for all privacy settings of the resource
 * 
 * @author Marcus Vetter
 * 
 */
public class PSValidator {
    
    private ResourceGroup rg;
    private String appIdentifier;
    
    
    public PSValidator(ResourceGroup rg, String appIdentifier) {
        this.rg = rg;
        this.appIdentifier = appIdentifier;
    }
    
    
    public void validate(String psIdentifier, String requiredValue) {
        AbstractPrivacySetting<?> ps = this.rg.getPrivacySetting(psIdentifier);
        String grantedValue = this.rg.getPMPPrivacySettingValue(psIdentifier, this.appIdentifier);
        
        boolean failed = true;
        try {
            if (ps.permits(grantedValue, requiredValue)) {
                failed = false;
            }
        } catch (PrivacySettingValueException e) {
            logSomethingWentWrong(e);
        }
        
        if (failed) {
            throwException(psIdentifier, requiredValue);
        }
    }
    
    
    private void logSomethingWentWrong(PrivacySettingValueException e) {
        Log.e(this, "Something went wrong while validating the permissions for Privacy Settings.", e);
    }
    
    
    private void throwException(String psName, String psValueRequired) {
        throw new SecurityException("The requested action requires at the PrivacySetting " + psName + " with at least "
                + psValueRequired + " as required value");
    }
    
}
