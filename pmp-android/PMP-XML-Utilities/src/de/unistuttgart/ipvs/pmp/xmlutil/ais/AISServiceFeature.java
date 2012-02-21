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

import de.unistuttgart.ipvs.pmp.xmlutil.common.BasicIS;

/**
 * This is a service feature, which is assigned to an {@link IAIS} and contains all {@link IAISRequiredResourceGroup}s.
 * 
 * @author Marcus Vetter
 * 
 */
public class AISServiceFeature extends BasicIS implements IAISServiceFeature {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -3279934293726339125L;
    
    /**
     * The identifier
     */
    private String identifier = "";
    
    /**
     * This list contains all {@link IAISRequiredResourceGroup} of the {@link AISServiceFeature}
     */
    private List<IAISRequiredResourceGroup> requiredResourceGroups = new ArrayList<IAISRequiredResourceGroup>();
    
    
    /**
     * Constructor without attributes
     */
    public AISServiceFeature() {
    }
    
    
    /**
     * Constructor to set the identifier
     * 
     * @param identifier
     *            identifier to set
     */
    public AISServiceFeature(String identifier) {
        setIdentifier(identifier);
    }
    
    
    @Override
    public List<IAISRequiredResourceGroup> getRequiredResourceGroups() {
        return this.requiredResourceGroups;
    }
    
    
    @Override
    public void addRequiredResourceGroup(IAISRequiredResourceGroup rrg) {
        this.requiredResourceGroups.add(rrg);
    }
    
    
    @Override
    public void removeRequiredResourceGroup(IAISRequiredResourceGroup rrg) {
        this.requiredResourceGroups.remove(rrg);
    }
    
    
    @Override
    public IAISRequiredResourceGroup getRequiredResourceGroupForIdentifier(String identifier) {
        for (IAISRequiredResourceGroup rrg : this.requiredResourceGroups) {
            if (rrg.getIdentifier().equals(identifier)) {
                return rrg;
            }
        }
        return null;
    }
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    
    @Override
    public void clearIssues() {
        super.clearIssues();
        clearNameIssues();
        clearDescriptionIssues();
    }
    
}
