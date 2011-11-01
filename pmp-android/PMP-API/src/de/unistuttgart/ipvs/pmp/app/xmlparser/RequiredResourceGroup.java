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
package de.unistuttgart.ipvs.pmp.app.xmlparser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParserException.Type;

/**
 * This is a representation of a resource group, which is required for a specific service level. It contains the
 * identifier of a assign privacy level and its required value.
 * 
 * @author Marcus Vetter
 * 
 */
public class RequiredResourceGroup implements Serializable {
    
    private static final long serialVersionUID = 5951904689151789055L;
    
    /**
     * Identifier of the required resource group
     */
    private String rgIdentifier;
    
    /**
     * This map contains all required privacy levels of the required resource group. key = identifier of the privacy
     * level
     */
    private Map<String, String> privacyLevels;
    
    
    /**
     * Constructor is used to instantiate the data structures and set the required resource group identifier.
     * 
     * @param rgIdentifier
     */
    protected RequiredResourceGroup(String rgIdentifier) {
        this.privacyLevels = new HashMap<String, String>();
        this.rgIdentifier = rgIdentifier;
    }
    
    
    /**
     * Get the identifier of the required resource group
     * 
     * @return identifier of the required resource group
     */
    public String getRgIdentifier() {
        return this.rgIdentifier;
    }
    
    
    /**
     * Set the identifier of the required resource group
     * 
     * @param rgIdentifier
     *            of the required resource group
     */
    protected void setRgIdentifier(String rgIdentifier) {
        this.rgIdentifier = rgIdentifier;
    }
    
    
    /**
     * Get all privacy levels of the required resource group
     * 
     * @return map of privacy levels
     */
    public Map<String, String> getPrivacyLevelMap() {
        return this.privacyLevels;
    }
    
    
    /**
     * Add a privacy level to the required resource group
     * 
     * @param identifier
     *            of the privacy level
     * @param requiredValue
     *            of the privacy level
     */
    protected void addPrivacyLevel(String identifier, String requiredValue) {
        if (this.privacyLevels.containsKey(identifier)) {
            throw new XMLParserException(Type.PRIVACY_LEVEL_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
                    "The privacy level of a service level with the identifier " + identifier + " already exists.");
        }
        this.privacyLevels.put(identifier, requiredValue);
    }
    
}
