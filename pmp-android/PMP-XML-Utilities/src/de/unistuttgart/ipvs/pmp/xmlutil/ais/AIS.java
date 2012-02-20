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

import de.unistuttgart.ipvs.pmp.xmlutil.common.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * This is an information set of the app. It contains all basic informations
 * (names and descriptions in different locals) and all provided {@link IAISServiceFeature}s.
 * 
 * @author Marcus Vetter
 */
public class AIS extends BasicIS implements IAIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -6652355069031983384L;
    
    /**
     * This list contains all {@link IAISServiceFeature}s of the app.
     */
    private List<IAISServiceFeature> serviceFeatures = new ArrayList<IAISServiceFeature>();
    
    
    @Override
    public void addServiceFeature(IAISServiceFeature sf) {
        this.serviceFeatures.add(sf);
    }
    
    
    @Override
    public void removeServiceFeature(IAISServiceFeature sf) {
        this.serviceFeatures.remove(sf);
    }
    
    
    @Override
    public List<IAISServiceFeature> getServiceFeatures() {
        return this.serviceFeatures;
    }
    
    
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
    public void clearIssues() {
        clearAppInformationIssues();
        clearServiceFeaturesIssues();
    }
    
    
    @Override
    public void clearAppInformationIssues() {
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
    
    
    @Override
    public void clearServiceFeaturesIssues() {
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
