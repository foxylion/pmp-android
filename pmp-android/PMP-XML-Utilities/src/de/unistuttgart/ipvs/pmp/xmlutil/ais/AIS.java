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
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * This is an information set of the app. It contains all basic informations
 * (names and descriptions in different locals) and all provided service
 * features.
 * 
 * @author Marcus Vetter
 */
public class AIS extends BasicIS implements IAIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -6652355069031983384L;
    
    /**
     * This list contains all service features of the app.
     */
    private List<IAISServiceFeature> serviceFeatures;
    
    
    /**
     * Constructor is used to instantiate the data structures.
     */
    public AIS() {
        super();
        this.serviceFeatures = new ArrayList<IAISServiceFeature>();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS#addServiceFeature(de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature)
     */
    @Override
    public void addServiceFeature(IAISServiceFeature sf) {
        this.serviceFeatures.add(sf);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS#removeServiceFeature(de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature)
     */
    @Override
    public void removeServiceFeature(IAISServiceFeature sf) {
        this.serviceFeatures.remove(sf);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS#getServiceFeatures()
     */
    @Override
    public List<IAISServiceFeature> getServiceFeatures() {
        return this.serviceFeatures;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS#getServiceFeatureForIdentifier(java.lang.String)
     */
    @Override
    public IAISServiceFeature getServiceFeatureForIdentifier(String identifier) {
        for (IAISServiceFeature sf : this.serviceFeatures) {
            if (sf.getIdentifier().equals(identifier)) {
                return sf;
            }
        }
        return null;
    }
    
    
    @Override
    public void clearIssuesAndPropagate() {
        clearAppInformationIssuesAndPropagate();
        clearServiceFeaturesIssuesAndPropagate();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS#clearAppInformationIssuesAndPropagate()
     */
    @Override
    public void clearAppInformationIssuesAndPropagate() {
        clearNameIssues();
        clearDescriptionIssues();
        List<IIssue> removeList = new ArrayList<IIssue>();
        for (IIssue issue : getIssues()) {
            if (!((issue.getType() == IssueType.SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE)
                    || (issue.getType() == IssueType.NO_SF_EXISTS) || (issue.getType() == IssueType.SF_IDENTIFIER_OCCURRED_TOO_OFTEN))) {
                removeList.add(issue);
            }
        }
        // Remove issues
        for (IIssue issue : removeList) {
            removeIssue(issue);
        }
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS#clearServiceFeaturesIssuesAndPropagate()
     */
    @Override
    public void clearServiceFeaturesIssuesAndPropagate() {
        for (IAISServiceFeature sf : this.getServiceFeatures()) {
            sf.clearIssuesAndPropagate();
        }
        List<IIssue> removeList = new ArrayList<IIssue>();
        for (IIssue issue : getIssues()) {
            if ((issue.getType() == IssueType.SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE)
                    || (issue.getType() == IssueType.NO_SF_EXISTS)
                    || (issue.getType() == IssueType.SF_IDENTIFIER_OCCURRED_TOO_OFTEN)) {
                removeList.add(issue);
            }
        }
        // Remove issues
        for (IIssue issue : removeList) {
            removeIssue(issue);
        }
    }
    
}
