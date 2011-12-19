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
package de.unistuttgart.ipvs.pmp.util.xml.rg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.util.xml.AbstractInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;

/**
 * 
 * The resourcegroup information set contains all basic information about the resource group.
 * It also contains all provided privacy settings
 * 
 * @author Marcus Vetter
 * 
 */
public class RgInformationSet extends AbstractInformationSet {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -5127998017294446211L;
    
    /**
     * The identifier of the resource group
     */
    private String identifier;
    
    /**
     * The revision of the resource group
     */
    private String revision;
    
    /**
     * This map contains all privacy settings of the resourcegroup. key =
     * identifier
     */
    private Map<String, PrivacySetting> privacySettingsMap;
    
    
    /**
     * Constructor is used to instantiate the data structures.
     */
    protected RgInformationSet() {
        super();
        this.privacySettingsMap = new HashMap<String, PrivacySetting>();
    }
    
    
    /**
     * Add a privacy setting to the resourcegroup
     * 
     * @param identifier
     *            of the privacy setting
     * @param ps
     *            privacy setting
     */
    protected void addPrivacySetting(String identifier, PrivacySetting ps) {
        if (this.privacySettingsMap.containsKey(identifier)) {
            throw new XMLParserException(Type.PRIVACY_SETTING_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
                    "A Privacy Setting with the identifier " + identifier + " already exists.");
        }
        this.privacySettingsMap.put(identifier, ps);
    }
    
    
    /**
     * Get the map which contains all privacy settings
     * 
     * @return map with privacy settings
     */
    public Map<String, PrivacySetting> getPrivacySettingsMap() {
        return this.privacySettingsMap;
    }
    
    
    /**
     * Get the identifier of the resource group
     * 
     * @return the identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /**
     * Set the identifier of the resource group
     * 
     * @param identifier
     *            the identifier of the resource group
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    
    /**
     * Get the revision of the resource group
     * 
     * @return the revision
     */
    public String getRevision() {
        return this.revision;
    }
    
    
    /**
     * Set the revision of the resource group
     * 
     * @param revision
     *            the revision of the resource group
     */
    public void setRevision(String revision) {
        this.revision = revision;
    }
}
