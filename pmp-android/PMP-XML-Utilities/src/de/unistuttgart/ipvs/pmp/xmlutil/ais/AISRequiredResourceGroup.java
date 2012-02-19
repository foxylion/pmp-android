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

import de.unistuttgart.ipvs.pmp.xmlutil.common.IdentifierIS;

/**
 * This is a representation of a resource group, which is required for a
 * specific service feature. It contains a list of required privacy settings.
 * 
 * @author Marcus Vetter
 * 
 */
public class AISRequiredResourceGroup extends IdentifierIS implements IAISRequiredResourceGroup {
    
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
    private List<IAISRequiredPrivacySetting> requiredPrivacySettings = new ArrayList<IAISRequiredPrivacySetting>();
    
    
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
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup#getMinRevision()
     */
    @Override
    public String getMinRevision() {
        return this.minRevision;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup#setMinRevision(java.lang.String)
     */
    @Override
    public void setMinRevision(String minRevision) {
        this.minRevision = minRevision;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup#getRequiredPrivacySettings()
     */
    @Override
    public List<IAISRequiredPrivacySetting> getRequiredPrivacySettings() {
        return this.requiredPrivacySettings;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup#addRequiredPrivacySetting(de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting)
     */
    @Override
    public void addRequiredPrivacySetting(IAISRequiredPrivacySetting privacySetting) {
        this.requiredPrivacySettings.add(privacySetting);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup#removeRequiredPrivacySetting(de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting)
     */
    @Override
    public void removeRequiredPrivacySetting(IAISRequiredPrivacySetting privacySetting) {
        this.requiredPrivacySettings.remove(privacySetting);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup#getRequiredPrivacySettingForIdentifier(java.lang.String)
     */
    @Override
    public IAISRequiredPrivacySetting getRequiredPrivacySettingForIdentifier(String identifier) {
        for (IAISRequiredPrivacySetting rps : this.requiredPrivacySettings) {
            if (rps.getIdentifier().equals(identifier)) {
                return rps;
            }
        }
        return null;
    }
    
}
