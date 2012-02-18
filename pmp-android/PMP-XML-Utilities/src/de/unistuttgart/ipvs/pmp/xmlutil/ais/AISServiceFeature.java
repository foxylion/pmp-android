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

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;

/**
 * This is a service feature, which is assigned to an app and contains all
 * required resource groups of the app.
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
     * This list contains all required resource groups of the service feature.
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
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature#getRequiredResourceGroups()
     */
    @Override
    public List<IAISRequiredResourceGroup> getRequiredResourceGroups() {
        return this.requiredResourceGroups;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature#addRequiredResourceGroup(de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup)
     */
    @Override
    public void addRequiredResourceGroup(IAISRequiredResourceGroup rrg) {
        this.requiredResourceGroups.add(rrg);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature#removeRequiredResourceGroup(de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup)
     */
    @Override
    public void removeRequiredResourceGroup(IAISRequiredResourceGroup rrg) {
        this.requiredResourceGroups.remove(rrg);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature#getRequiredResourceGroupForIdentifier(java.lang.String)
     */
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
    public void clearIssuesAndPropagate() {
        super.getIssues().clear();
        super.clearNameIssues();
        super.clearDescriptionIssues();
        for (IAISRequiredResourceGroup rrg : this.getRequiredResourceGroups()) {
            rrg.clearIssuesAndPropagate();
        }
    }
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
