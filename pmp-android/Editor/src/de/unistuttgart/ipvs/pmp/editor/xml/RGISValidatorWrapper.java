/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
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
package de.unistuttgart.ipvs.pmp.editor.xml;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.RGISValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Wrapper of the RGISValidator. The following issues are also attached to the
 * subobjects: - PS_IDENTIFIER_OCCURRED_TOO_OFTEN -
 * CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISValidatorWrapper {
    
    /**
     * ValidatorWrapperHelper
     */
    ValidatorWrapperHelper helper = ValidatorWrapperHelper.getInstance();
    
    /**
     * Singleton stuff
     */
    private static RGISValidatorWrapper instance = null;
    
    
    private RGISValidatorWrapper() {
        
    }
    
    
    public static RGISValidatorWrapper getInstance() {
        if (instance == null) {
            instance = new RGISValidatorWrapper();
        }
        return instance;
    }
    
    
    public List<IIssue> validateRGIS(IRGIS rgis, boolean attachData) {
        List<IIssue> issues = new RGISValidator().validateRGIS(rgis, attachData);
        extendAttachments(issues, attachData);
        addSummaryIssues(rgis);
        return issues;
    }
    
    
    /**
     * INTERNAL USE ONLY! Extend the attachments of issues
     * 
     * @param issues
     *            the list of issues
     * @param attachData
     *            only extend the attachments, if its true
     */
    private void extendAttachments(List<IIssue> issues, boolean attachData) {
        if (attachData) {
            try {
                for (IIssue issue : issues) {
                    
                    // The issue
                    IssueType issueType = issue.getType();
                    List<String> issueParameters = issue.getParameters();
                    IIssueLocation issueLocation = issue.getLocation();
                    
                    /*
                     * RGIS
                     */
                    if (issueLocation instanceof RGIS) {
                        // Cast the issue location
                        RGIS rgis = (RGIS) issueLocation;
                        
                        // Switch the issue types
                        switch (issueType) {
                        
                            case NAME_LOCALE_OCCURRED_TOO_OFTEN:
                                
                                // Attach names with issue
                                this.helper.attachIBasicIS(rgis.getNames(), IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN,
                                        issueParameters);
                                
                                // Remove issue of the rgis
                                rgis.removeIssue(issue);
                                
                                break;
                            
                            case DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:
                                
                                // Attach descriptions with issue
                                this.helper.attachIBasicIS(rgis.getDescriptions(),
                                        IssueType.DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN, issueParameters);
                                
                                // Remove issue of the rgis
                                rgis.removeIssue(issue);
                                
                                break;
                            
                            case PS_IDENTIFIER_OCCURRED_TOO_OFTEN:
                                
                                // Attach privacy settings with issue
                                List<IIdentifierIS> identifierISs = new ArrayList<IIdentifierIS>();
                                for (IRGISPrivacySetting ps : rgis.getPrivacySettings()) {
                                    identifierISs.add(ps);
                                }
                                this.helper.attachIIdentifierIS(identifierISs,
                                        IssueType.PS_IDENTIFIER_OCCURRED_TOO_OFTEN, issueParameters);
                                
                                // Remove issue of the rgis
                                rgis.removeIssue(issue);
                        }
                        
                    }
                    
                    /*
                     * Privacy Setting
                     */
                    if (issueLocation instanceof RGISPrivacySetting) {
                        // Cast the issue location
                        RGISPrivacySetting ps = (RGISPrivacySetting) issueLocation;
                        
                        // Switch the issue types
                        switch (issueType) {
                        
                            case NAME_LOCALE_OCCURRED_TOO_OFTEN:
                                
                                // Attach names with issue
                                this.helper.attachIBasicIS(ps.getNames(), IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN,
                                        issueParameters);
                                
                                break;
                            
                            case DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:
                                
                                // Attach descriptions with issue
                                this.helper.attachIBasicIS(ps.getDescriptions(),
                                        IssueType.DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN, issueParameters);
                                
                                break;
                            
                            case CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN:
                                
                                // Attach change descriptions with issues
                                this.helper.attachIBasicIS(ps.getChangeDescriptions(),
                                        IssueType.CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN, issueParameters);
                                
                                break;
                        
                        }
                    }
                    
                }
            } catch (ClassCastException cce) {
                System.out
                        .println("Oooops, shit happens! The RGISValidatorWrapper failed or the XML Library has changed."); //$NON-NLS-1$
                cce.printStackTrace();
            }
            
        }
    }
    
    
    /**
     * Add all summary issues
     * 
     * @param rgis
     *            the RGIS
     */
    private void addSummaryIssues(IRGIS rgis) {
        for (IRGISPrivacySetting ps : rgis.getPrivacySettings()) {
            boolean nameIssue = false;
            boolean descriptionIssue = false;
            boolean changeDescriptionIssue = false;
            for (ILocalizedString name : ps.getNames()) {
                if (!name.getIssues().isEmpty()) {
                    nameIssue = true;
                }
            }
            for (ILocalizedString description : ps.getDescriptions()) {
                if (!description.getIssues().isEmpty()) {
                    descriptionIssue = true;
                }
            }
            for (ILocalizedString changeDescription : ps.getChangeDescriptions()) {
                if (!changeDescription.getIssues().isEmpty()) {
                    changeDescriptionIssue = true;
                }
            }
            
            if (nameIssue && !ps.hasIssueType(IssueType.RGIS_PS_NAME_ISSUES)) {
                ps.addIssue(new Issue(IssueType.RGIS_PS_NAME_ISSUES, ps));
            }
            if (descriptionIssue && !ps.hasIssueType(IssueType.RGIS_PS_DESCRIPTION_ISSUES)) {
                ps.addIssue(new Issue(IssueType.RGIS_PS_DESCRIPTION_ISSUES, ps));
            }
            if (changeDescriptionIssue && !ps.hasIssueType(IssueType.RGIS_PS_CHANGE_DESCRIPTION_ISSUES)) {
                ps.addIssue(new Issue(IssueType.RGIS_PS_CHANGE_DESCRIPTION_ISSUES, ps));
            }
        }
    }
    
}
