/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-XML-UTILITIES
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
 * This is a required resource group, which is required for a specific {@link IAISServiceFeature}. It contains a list of
 * {@link IAISRequiredPrivacySetting}s.
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
     * This list contains all {@link IAISRequiredPrivacySetting}s of the {@link AISRequiredResourceGroup}
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
     *            the identifier of the {@link AISRequiredResourceGroup}
     * @param minRevision
     *            the minimal revision of the {@link AISRequiredResourceGroup}
     */
    public AISRequiredResourceGroup(String identifier, String minRevision) {
        setIdentifier(identifier);
        setMinRevision(minRevision);
    }
    
    
    @Override
    public String getMinRevision() {
        return this.minRevision;
    }
    
    
    @Override
    public void setMinRevision(String minRevision) {
        this.minRevision = minRevision;
    }
    
    
    @Override
    public List<IAISRequiredPrivacySetting> getRequiredPrivacySettings() {
        return this.requiredPrivacySettings;
    }
    
    
    @Override
    public void addRequiredPrivacySetting(IAISRequiredPrivacySetting privacySetting) {
        this.requiredPrivacySettings.add(privacySetting);
    }
    
    
    @Override
    public void removeRequiredPrivacySetting(IAISRequiredPrivacySetting privacySetting) {
        this.requiredPrivacySettings.remove(privacySetting);
    }
    
    
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
