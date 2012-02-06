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
package de.unistuttgart.ipvs.pmp.xmlutil.rgis;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;

/**
 * 
 * The resource group information set contains all basic information about the
 * resource group. It also contains all provided privacy settings
 * 
 * @author Marcus Vetter
 * 
 */
public class RGIS extends BasicIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 8978212582601842275L;
    
    /**
     * The identifier of the resource group
     */
    private String identifier;
    
    /**
     * The icon of the resource group
     */
    private String iconLocation;
    
    /**
     * The class name of the resource group
     */
    private String className;
    
    /**
     * This list contains all privacy settings of the resource group.
     */
    private List<RGISPrivacySetting> privacySettings = new ArrayList<RGISPrivacySetting>();
    
    
    /**
     * Add a privacy setting to the resourcegroup
     * 
     * @param privacySetting
     *            privacy setting to add
     */
    public void addPrivacySetting(RGISPrivacySetting privacySetting) {
        this.privacySettings.add(privacySetting);
    }
    
    
    /**
     * Get the list which contains all privacy settings
     * 
     * @return list with privacy settings
     */
    public List<RGISPrivacySetting> getPrivacySettings() {
        return this.privacySettings;
    }
    
    
    /**
     * Remove a privacy setting from resource group
     * 
     * @param privacySetting
     *            privacy setting to remove
     */
    public void removePrivacySetting(RGISPrivacySetting privacySetting) {
        this.privacySettings.remove(privacySetting);
    }
    
    
    /**
     * Get a privacy setting for a given identifier. Null, if no privacy setting
     * exists for the given identifier.
     * 
     * @param identifier
     *            identifier of the privacy setting
     * @return privacy setting with given identifier, null if none exists.
     */
    public RGISPrivacySetting getPrivacySettingForIdentifier(String identifier) {
        for (RGISPrivacySetting ps : this.privacySettings) {
            if (ps.getIdentifier().equals(identifier)) {
                return ps;
            }
        }
        return null;
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
     * Get the location of the icon of the resource group
     * 
     * @return location of the icon of the resource group
     */
    public String getIconLocation() {
        return this.iconLocation;
    }
    
    
    /**
     * Set the location of the icon of the resource group
     * 
     * @param iconLocation
     *            the location of the icon of the resource group
     */
    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }
    
    
    /**
     * Get the class name of the resource group
     * 
     * @return class name of the resource group
     */
    public String getClassName() {
        return this.className;
    }
    
    
    /**
     * Set the class name of the resource group
     * 
     * @param className
     *            class name of the resource group
     */
    public void setClassName(String className) {
        this.className = className;
    }
}
