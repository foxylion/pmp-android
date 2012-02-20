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
package de.unistuttgart.ipvs.pmp.xmlutil.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Validator for AIS
 * 
 * @author Marcus Vetter
 * 
 */
public class AISValidator extends AbstractValidator {
    
    /**
     * Validate the whole AIS
     * 
     * @param ais
     *            the ais
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public List<IIssue> validateAIS(IAIS ais, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData) {
            ais.clearIssues();
        }
        
        /*
         * Validate the app information and the service features 
         */
        issueList.addAll(validateAppInformation(ais, attachData));
        issueList.addAll(validateServiceFeatures(ais, attachData));
        
        return issueList;
    }
    
    
    /**
     * Validate the app information
     * 
     * @param ais
     *            the ais
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public List<IIssue> validateAppInformation(IAIS ais, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData) {
            ais.clearAppInformationIssues();
        }
        
        /*
         * Validate names and descriptions
         */
        issueList.addAll(validateNames(ais));
        issueList.addAll(validateDescriptions(ais));
        
        // Attach data
        attachData(issueList, attachData);
        
        return issueList;
    }
    
    
    /**
     * Validate all service features of the given AIS
     * 
     * @param ais
     *            the ais
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public List<IIssue> validateServiceFeatures(IAIS ais, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData) {
            ais.clearServiceFeaturesIssues();
        }
        
        /*
         * Validate the occurrences of identifier of service features
         */
        // Convert
        List<IIdentifierIS> superSFList = new ArrayList<IIdentifierIS>();
        for (IAISServiceFeature sf : ais.getServiceFeatures()) {
            superSFList.add(sf);
        }
        // Validate
        for (String identifierFail : validateOccurrenceOfIdentifier(superSFList)) {
            Issue issue = new Issue(IssueType.SF_IDENTIFIER_OCCURRED_TOO_OFTEN, ais);
            issue.addParameter(identifierFail);
            issueList.add(issue);
        }
        
        /*
         * Validate, if any service features exists
         */
        if (ais.getServiceFeatures().size() == 0) {
            Issue issue = new Issue(IssueType.NO_SF_EXISTS, ais);
            issueList.add(issue);
        }
        
        /*
         * Validate, if all service features contain different values of privacy settings, 
         * if they have the same required resource groups and required privacy settings
         */
        issueList.addAll(validateAISDiffPSValuesForDiffSFs(ais));
        
        // Attach data
        attachData(issueList, attachData);
        
        /*
         * Validate all service features
         */
        for (IAISServiceFeature sf : ais.getServiceFeatures()) {
            issueList.addAll(validateServiceFeature(sf, attachData));
        }
        
        return issueList;
    }
    
    
    /**
     * Validate the given service feature
     * 
     * @param sf
     *            the service feature
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public List<IIssue> validateServiceFeature(IAISServiceFeature sf, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData) {
            sf.clearIssues();
        }
        
        /*
         * Validate names and descriptions
         */
        issueList.addAll(validateNames(sf));
        issueList.addAll(validateDescriptions(sf));
        
        /*
         * Validate, if the identifier is set
         */
        if (!checkValueSet(sf.getIdentifier())) {
            issueList.add(new Issue(IssueType.IDENTIFIER_MISSING, sf));
        }
        
        /*
         * Validate, if any required resource group is available 
         */
        if (sf.getRequiredResourceGroups().size() == 0) {
            issueList.add(new Issue(IssueType.NO_RRG_EXISTS, sf));
        }
        
        /*
         * Validate the occurrences of identifier of required resource groups
         */
        // Convert
        List<IIdentifierIS> superRRGList = new ArrayList<IIdentifierIS>();
        for (IAISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
            superRRGList.add(rrg);
        }
        // Validate
        for (String identifierFail : validateOccurrenceOfIdentifier(superRRGList)) {
            Issue issue = new Issue(IssueType.RRG_IDENTIFIER_OCCURRED_TOO_OFTEN, sf);
            issue.addParameter(identifierFail);
            issueList.add(issue);
        }
        
        // Attach data
        attachData(issueList, attachData);
        
        /*
         * Validate all required resource groups
         */
        for (IAISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
            issueList.addAll(validateRequiredResourceGroup(rrg, attachData));
        }
        
        return issueList;
    }
    
    
    /**
     * Validate a given required resource grouop
     * 
     * @param rrg
     *            the required resource group to validate
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public List<IIssue> validateRequiredResourceGroup(IAISRequiredResourceGroup rrg, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData) {
            rrg.clearIssues();
        }
        
        /*
         * Validate, if the identifier is set
         */
        if (!checkValueSet(rrg.getIdentifier())) {
            issueList.add(new Issue(IssueType.IDENTIFIER_MISSING, rrg));
        }
        
        /*
         * Validate, if any required privacy setting is available
         */
        if (rrg.getRequiredPrivacySettings().size() == 0) {
            issueList.add(new Issue(IssueType.NO_RPS_EXISTS, rrg));
        }
        
        /*
         * Validate the occurrences of identifier of required privacy settings
         */
        // Convert
        List<IIdentifierIS> superRPSList = new ArrayList<IIdentifierIS>();
        for (IAISRequiredPrivacySetting rps : rrg.getRequiredPrivacySettings()) {
            superRPSList.add(rps);
        }
        // Validate
        for (String identifierFail : validateOccurrenceOfIdentifier(superRPSList)) {
            Issue issue = new Issue(IssueType.RPS_IDENTIFIER_OCCURRED_TOO_OFTEN, rrg);
            issue.addParameter(identifierFail);
            issueList.add(issue);
        }
        
        /*
         * Validate, if the minrevision is set
         */
        if (!checkValueSet(rrg.getMinRevision())) {
            issueList.add(new Issue(IssueType.MINREVISION_MISSING, rrg));
        } else {
            /*
             * Validate, if the minrevision is valid
             */
            try {
                Integer.valueOf(rrg.getMinRevision());
            } catch (NumberFormatException nfe) {
                Issue issue = new Issue(IssueType.MINREVISION_INVALID, rrg);
                issue.addParameter(rrg.getMinRevision());
                issueList.add(issue);
            }
        }
        
        // Attach data
        attachData(issueList, attachData);
        
        /*
         * Validate all required privacy settings
         */
        for (IAISRequiredPrivacySetting rps : rrg.getRequiredPrivacySettings()) {
            issueList.addAll(validateRequiredPrivacySetting(rps, attachData));
        }
        
        return issueList;
    }
    
    
    /**
     * Validate a given required privacy setting
     * 
     * @param rps
     *            the required privacy setting to validate
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public List<IIssue> validateRequiredPrivacySetting(IAISRequiredPrivacySetting rps, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData) {
            rps.clearIssues();
        }
        
        /*
         * Validate, if the identifier is set
         */
        if (!checkValueSet(rps.getIdentifier())) {
            issueList.add(new Issue(IssueType.IDENTIFIER_MISSING, rps));
        }
        
        /*
         * Validate, if the value is set
         */
        if (!checkValueSet(rps.getValue())) {
            issueList.add(new Issue(IssueType.EMPTY_VALUE, rps));
        }
        
        // Attach data
        attachData(issueList, attachData);
        
        return issueList;
    }
    
    
    /**
     * Validate the given AppInformationSet (ais) Check, that there are no
     * different Service Features which address the same Privacy Settings and
     * the same required values of those Privacy Settings.
     * 
     * @param ais
     *            the AppInformationSet
     * @return List with issues as result of the validation
     */
    private List<IIssue> validateAISDiffPSValuesForDiffSFs(IAIS ais) {
        // Temporary list if invalid sf sets
        List<Set<IAISServiceFeature>> invalidSFSets = new ArrayList<Set<IAISServiceFeature>>();
        
        // Build the maps with set of strings
        Map<IAISServiceFeature, Set<String>> sfSetMap = new HashMap<IAISServiceFeature, Set<String>>();
        for (IAISServiceFeature sf : ais.getServiceFeatures()) {
            Set<String> stringSet = new TreeSet<String>();
            for (IAISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
                for (IAISRequiredPrivacySetting rps : rrg.getRequiredPrivacySettings()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(rrg.getIdentifier());
                    sb.append(rrg.getMinRevision());
                    sb.append(rps.getIdentifier());
                    sb.append(rps.getValue());
                    stringSet.add(sb.toString());
                }
                
            }
            sfSetMap.put(sf, stringSet);
        }
        
        // Compare the maps
        for (Entry<IAISServiceFeature, Set<String>> entry : sfSetMap.entrySet()) {
            for (Entry<IAISServiceFeature, Set<String>> entryCompare : sfSetMap.entrySet()) {
                if (entry.equals(entryCompare)) {
                    continue;
                }
                
                if (entry.getValue().equals(entryCompare.getValue())) {
                    Set<IAISServiceFeature> set = new HashSet<IAISServiceFeature>();
                    set.add(entry.getKey());
                    set.add(entryCompare.getKey());
                    invalidSFSets.add(set);
                }
            }
        }
        
        // Build union sets, if sets contain the same service feature
        for (Set<IAISServiceFeature> set : invalidSFSets) {
            for (Set<IAISServiceFeature> setCompare : invalidSFSets) {
                if (set.equals(setCompare)) {
                    continue;
                }
                
                for (IAISServiceFeature sf : setCompare) {
                    if (set.contains(sf)) {
                        set.addAll(setCompare);
                    }
                }
            }
        }
        
        // Adjust the sets of invalid sfs
        List<Set<IAISServiceFeature>> adjustedInvalidSFSets = new ArrayList<Set<IAISServiceFeature>>();
        for (Set<IAISServiceFeature> set : invalidSFSets) {
            if (!adjustedInvalidSFSets.contains(set)) {
                adjustedInvalidSFSets.add(set);
            }
        }
        
        // Instantiate the issues
        List<IIssue> issues = new ArrayList<IIssue>();
        for (Set<IAISServiceFeature> set : adjustedInvalidSFSets) {
            Issue issue = new Issue(IssueType.SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE, ais);
            for (IAISServiceFeature sf : set) {
                issue.addParameter(sf.getIdentifier());
            }
            issues.add(issue);
        }
        
        return issues;
    }
    
    
    /**
     * Clear all issues, begin at the given IAIS and propagate
     * 
     * @param ais
     *            the IAIS
     */
    public void clearIssuesAndPropagate(IAIS ais) {
        ais.clearIssues();
        for (IAISServiceFeature sf : ais.getServiceFeatures()) {
            sf.clearIssues();
            for (IAISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
                rrg.clearIssues();
                for (IAISRequiredPrivacySetting ps : rrg.getRequiredPrivacySettings()) {
                    ps.clearIssues();
                }
            }
        }
    }
}
