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
package de.unistuttgart.ipvs.pmp.resource;

import java.io.Serializable;
import java.util.Map;

/**
 * An App representation for the {@link IResourceGroupServicePMP}.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupAccess implements Serializable {
    
    private static final long serialVersionUID = 6045269413834594529L;
    
    private ResourceGroupAccessHeader header;
    private Map<String, String> privacyLevelValues;
    
    
    /**
     * Creates a new {@link ResourceGroupAccess}.
     * 
     * @param header
     *            header of this access set.
     * @param privacyLevelValues
     *            Bundle of privacy levels and their set values
     */
    public ResourceGroupAccess(ResourceGroupAccessHeader header, Map<String, String> privacyLevelValues) {
        this.header = header;
        this.privacyLevelValues = privacyLevelValues;
    }
    
    
    public ResourceGroupAccessHeader getHeader() {
        return this.header;
    }
    
    
    /**
     * Returns the corresponding value to a privacy level.
     * 
     * @param privacyLevel
     *            name of the privacy level
     * @return value of the privacy level or null if it is not set
     */
    public String getPrivacyLevelValue(String privacyLevel) {
        return this.privacyLevelValues.get(privacyLevel);
    }
    
    
    /**
     * @return the bundle containing the privacy level values.
     */
    public Map<String, String> getPrivacyLevelValues() {
        return this.privacyLevelValues;
    }
}
