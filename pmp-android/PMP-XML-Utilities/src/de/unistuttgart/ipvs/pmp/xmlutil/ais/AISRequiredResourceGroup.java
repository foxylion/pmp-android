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
package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IdentifierIS;

/**
 * This is a representation of a resource group, which is required for a
 * specific service feature. It contains a list of required privacy settings.
 * 
 * @author Marcus Vetter
 * 
 */
public class AISRequiredResourceGroup extends IdentifierIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 5951904689151789055L;
    
    /**
     * Min revision
     */
    private String minRevision = "";
    
    /**
     * This list contains all required privacy settings of the required resource
     * group.
     */
    private List<AISRequiredPrivacySetting> requiredPrivacySettings = new ArrayList<AISRequiredPrivacySetting>();
    
    
    /**
     * Constructor without attributes
     */
    public AISRequiredResourceGroup() {
    }
    
    
    /**
     * Constructor to set the identifier and minRevision
     * 
     * @param identifier
     *            identifier to set
     * @param minRevision
     *            the min revision of the rrg
     */
    public AISRequiredResourceGroup(String identifier, String minRevision) {
        setIdentifier(identifier);
        setMinRevision(minRevision);
    }
    
    
    /**
     * Get the min revision
     * 
     * @return the min revision
     */
    public String getMinRevision() {
        return this.minRevision;
    }
    
    
    /**
     * Set the min revision
     * 
     * @param minRevision
     *            min revision to set
     */
    public void setMinRevision(String minRevision) {
        this.minRevision = minRevision;
    }
    
    
    /**
     * Get all privacy settings of the required resource group
     * 
     * @return list of privacy settings
     */
    public List<AISRequiredPrivacySetting> getRequiredPrivacySettings() {
        return this.requiredPrivacySettings;
    }
    
    
    /**
     * Add a privacy setting to the required resource group
     * 
     * @param privacySetting
     *            privacySetting to add
     */
    public void addRequiredPrivacySetting(AISRequiredPrivacySetting privacySetting) {
        this.requiredPrivacySettings.add(privacySetting);
    }
    
    
    /**
     * Remove a privacy setting from the required resource group
     * 
     * @param privacySetting
     *            privacySetting to remove
     */
    public void removeRequiredPrivacySetting(AISRequiredPrivacySetting privacySetting) {
        this.requiredPrivacySettings.remove(privacySetting);
    }
    
    
    /**
     * Get a required privacy setting for a given identifier. Null, if no
     * required privacy setting exists for the given identifier.
     * 
     * @param identifier
     *            identifier of the required privacy setting
     * @return required privacy setting with given identifier, null if none
     *         exists.
     */
    public AISRequiredPrivacySetting getRequiredPrivacySettingForIdentifier(String identifier) {
        for (AISRequiredPrivacySetting rps : this.requiredPrivacySettings) {
            if (rps.getIdentifier().equals(identifier)) {
                return rps;
            }
        }
        return null;
    }
    
    
    @Override
    public void clearIssuesAndPropagate() {
        super.getIssues().clear();
        for (AISRequiredPrivacySetting rps : this.getRequiredPrivacySettings()) {
            rps.clearIssuesAndPropagate();
        }
    }
    
}
