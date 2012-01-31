/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp.xmlutil.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ISException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ISException.Type;

/**
 * This is a representation of a resource group, which is required for a specific service feature. It contains the
 * identifier of a assign privacy setting and its required value.
 * 
 * @author Marcus Vetter
 * 
 */
public class RequiredResourceGroup implements Serializable {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 5951904689151789055L;
    
    /**
     * This map contains all required privacy settings of the required resource group. key = identifier of the privacy
     * setting
     */
    private Map<String, String> privacySettings;
    
    
    /**
     * Constructor is used to instantiate the data structures
     */
    public RequiredResourceGroup() {
        this.privacySettings = new HashMap<String, String>();
    }
    
    
    /**
     * Get all privacy settings of the required resource group
     * 
     * @return map of privacy settings
     */
    public Map<String, String> getPrivacySettingsMap() {
        return this.privacySettings;
    }
    
    
    /**
     * Add a privacy setting to the required resource group
     * 
     * @param identifier
     *            of the privacy setting
     * @param requiredValue
     *            of the privacy setting
     */
    public void addPrivacySetting(String identifier, String requiredValue) {
        if (this.privacySettings.containsKey(identifier)) {
            throw new ISException(Type.PRIVACY_SETTING_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
                    "The privacy setting of a service feature with the identifier " + identifier + " already exists.");
        }
        this.privacySettings.put(identifier, requiredValue);
    }
    
}
