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

import de.unistuttgart.ipvs.pmp.xmlutil.common.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * 
 * The resource group information set contains all basic information about the
 * resource group. It also contains all provided privacy settings
 * 
 * @author Marcus Vetter
 * 
 */
public class RGIS extends BasicIS implements IRGIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 8978212582601842275L;
    
    /**
     * The identifier
     */
    private String identifier = "";
    
    /**
     * The icon of the resource group
     */
    private String iconLocation = "";
    
    /**
     * The class name of the resource group
     */
    private String className = "";
    
    /**
     * This list contains all privacy settings of the resource group.
     */
    private List<IRGISPrivacySetting> privacySettings = new ArrayList<IRGISPrivacySetting>();
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#addPrivacySetting(de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting)
     */
    @Override
    public void addPrivacySetting(IRGISPrivacySetting privacySetting) {
        this.privacySettings.add(privacySetting);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#getPrivacySettings()
     */
    @Override
    public List<IRGISPrivacySetting> getPrivacySettings() {
        return this.privacySettings;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#removePrivacySetting(de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting)
     */
    @Override
    public void removePrivacySetting(IRGISPrivacySetting privacySetting) {
        this.privacySettings.remove(privacySetting);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#getPrivacySettingForIdentifier(java.lang.String)
     */
    @Override
    public IRGISPrivacySetting getPrivacySettingForIdentifier(String identifier) {
        for (IRGISPrivacySetting ps : this.privacySettings) {
            if (ps.getIdentifier().equals(identifier)) {
                return ps;
            }
        }
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#getIconLocation()
     */
    @Override
    public String getIconLocation() {
        return this.iconLocation;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#setIconLocation(java.lang.String)
     */
    @Override
    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#getClassName()
     */
    @Override
    public String getClassName() {
        return this.className;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#setClassName(java.lang.String)
     */
    @Override
    public void setClassName(String className) {
        this.className = className;
    }
    
    
    @Override
    public void clearIssuesAndPropagate() {
        clearRGInformationIssuesAndPropagate();
        clearPSIssuesAndPropagate();
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#clearRGInformationIssuesAndPropagate()
     */
    @Override
    public void clearRGInformationIssuesAndPropagate() {
        clearNameIssues();
        clearDescriptionIssues();
        List<IIssue> removeList = new ArrayList<IIssue>();
        for (IIssue issue : getIssues()) {
            if (!((issue.getType() == IssueType.PS_IDENTIFIER_OCCURRED_TOO_OFTEN) || (issue.getType() == IssueType.NO_PS_EXISTS))) {
                removeList.add(issue);
            }
        }
        // Remove issues
        for (IIssue issue : removeList) {
            removeIssue(issue);
        }
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS#clearPSIssuesAndPropagate()
     */
    @Override
    public void clearPSIssuesAndPropagate() {
        for (IRGISPrivacySetting ps : this.getPrivacySettings()) {
            ps.clearIssuesAndPropagate();
        }
        List<IIssue> removeList = new ArrayList<IIssue>();
        for (IIssue issue : getIssues()) {
            if ((issue.getType() == IssueType.PS_IDENTIFIER_OCCURRED_TOO_OFTEN)
                    || (issue.getType() == IssueType.NO_PS_EXISTS)) {
                removeList.add(issue);
            }
        }
        // Remove issues
        for (IIssue issue : removeList) {
            removeIssue(issue);
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
