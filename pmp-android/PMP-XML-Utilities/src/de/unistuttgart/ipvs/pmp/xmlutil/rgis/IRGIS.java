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

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IRGIS extends IBasicIS, IIdentifierIS {
    
    /**
     * Add a privacy setting to the resourcegroup
     * 
     * @param privacySetting
     *            privacy setting to add
     */
    public abstract void addPrivacySetting(IRGISPrivacySetting privacySetting);
    
    
    /**
     * Get the list which contains all privacy settings
     * 
     * @return list with privacy settings
     */
    public abstract List<IRGISPrivacySetting> getPrivacySettings();
    
    
    /**
     * Remove a privacy setting from resource group
     * 
     * @param privacySetting
     *            privacy setting to remove
     */
    public abstract void removePrivacySetting(IRGISPrivacySetting privacySetting);
    
    
    /**
     * Get a privacy setting for a given identifier. Null, if no privacy setting
     * exists for the given identifier.
     * 
     * @param identifier
     *            identifier of the privacy setting
     * @return privacy setting with given identifier, null if none exists.
     */
    public abstract IRGISPrivacySetting getPrivacySettingForIdentifier(String identifier);
    
    
    /**
     * Get the location of the icon of the resource group
     * 
     * @return location of the icon of the resource group
     */
    public abstract String getIconLocation();
    
    
    /**
     * Set the location of the icon of the resource group
     * 
     * @param iconLocation
     *            the location of the icon of the resource group
     */
    public abstract void setIconLocation(String iconLocation);
    
    
    /**
     * Get the class name of the resource group
     * 
     * @return class name of the resource group
     */
    public abstract String getClassName();
    
    
    /**
     * Set the class name of the resource group
     * 
     * @param className
     *            class name of the resource group
     */
    public abstract void setClassName(String className);
    
    
    /**
     * Clear only issues referring to the resource group information
     */
    public abstract void clearRGInformationIssues();
    
    
    /**
     * Clear only issues referring to the privacy settings
     */
    public abstract void clearPSIssues();
    
}
