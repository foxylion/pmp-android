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
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException.Type;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.AISIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.AISIssueLocation;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.AISIssueType;

/**
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
     * @return List with issues as result of the validation
     */
    public static List<AISIssue> validateAIS(AIS ais) {
        List<AISIssue> issueList = new ArrayList<AISIssue>();
        
        issueList.addAll(validateAppInformation(ais));
        issueList.addAll(validateServiceFeatures(ais));
        
        return issueList;
    }
    
    
    /**
     * Validate the app information
     * 
     * @param ais
     *            the ais
     * @return List with issues as result of the validation
     */
    public static List<AISIssue> validateAppInformation(AIS ais) {
        List<AISIssue> issueList = new ArrayList<AISIssue>();
        
        issueList.addAll(validateNames(ais));
        issueList.addAll(validateDescriptions(ais));
        
        return issueList;
    }
    
    
    /**
     * Validate the names of a given {@link AISIssueLocation}
     * 
     * @param location
     *            the AISIssueLocation
     * @return List with issues as result of the validation
     */
    public static List<AISIssue> validateNames(AISIssueLocation location) {
        List<AISIssue> issueList = new ArrayList<AISIssue>();
        
        // Check, if this location has names and descriptions (extends from BasicIS)
        if (location instanceof BasicIS) {
            boolean englishLocaleExists = false;
            List<String> localesOccurred = new ArrayList<String>();
            
            for (Name name : ((BasicIS) location).getNames()) {
                
                // Instantiate possible issues
                AISIssue localeMissing = new AISIssue(AISIssueType.NAME_LOCALE_MISSING, location);
                AISIssue localeInvalid = new AISIssue(AISIssueType.NAME_LOCALE_INVALID, location);
                AISIssue nameEmpty = new AISIssue(AISIssueType.NAME_EMPTY, location);
                
                // Flag, if the locale is missing
                boolean localeAvailable = true;
                
                // Check, if the locale is set
                if (name.getLocale() == null || !checkValueSet(name.getLocale().getLanguage())) {
                    issueList.add(localeMissing);
                    localeAvailable = false;
                    
                } else if (!checkLocale(name.getLocale())) {
                    // if the locale is invalid
                    issueList.add(localeInvalid);
                } else {
                    // Check, if its the english attribute
                    if (checkLocaleAttributeEN(name.getLocale())) {
                        englishLocaleExists = true;
                    }
                    
                    String locale = name.getLocale().getLanguage();
                    if (!localesOccurred.contains(locale)) {
                        localesOccurred.add(locale);
                    } else {
                        // Check, if this issue is already added to the issuelist
                        boolean issueAlreadyExists = false;
                        for (AISIssue issueExisting : issueList) {
                            if (issueExisting.getType().equals(AISIssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN)
                                    && issueExisting.getLocation().equals(location)
                                    && issueExisting.getParameters().size() == 1
                                    && issueExisting.getParameters().get(0).equals(locale)) {
                                issueAlreadyExists = true;
                            }
                        }
                        if (!issueAlreadyExists) {
                            AISIssue localesOccurredTooOften = new AISIssue(
                                    AISIssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN, location);
                            localesOccurredTooOften.addParameter(locale);
                            issueList.add(localesOccurredTooOften);
                        }
                        
                    }
                }
                
                // Check, if the name is set
                if (!checkValueSet(name.getName())) {
                    issueList.add(nameEmpty);
                    // Add the information of the locale to the name issue
                    if (localeAvailable) {
                        nameEmpty.addParameter(name.getLocale().getLanguage());
                    }
                } else {
                    // Add the information of the name to the locale issues
                    localeMissing.addParameter(name.getName());
                    localeInvalid.addParameter(name.getName());
                }
            }
            
            // Add an issue, that the english locale is missing
            if (!englishLocaleExists) {
                issueList.add(new AISIssue(AISIssueType.NAME_LOCALE_EN_MISSING, location));
            }
            
        }
        
        return issueList;
        
    }
    
    
    /**
     * Validate the description of a given {@link AISIssueLocation}
     * 
     * @param location
     *            the AISIssueLocation
     * @return List with issues as result of the validation
     */
    public static List<AISIssue> validateDescriptions(AISIssueLocation location) {
        List<AISIssue> issueList = new ArrayList<AISIssue>();
        
        // Check, if this location has descriptions (extends from BasicIS)
        if (location instanceof BasicIS) {
            for (Description descr : ((BasicIS) location).getDescriptions()) {
                
            }
        }
        
        return issueList;
    }
    
    
    /**
     * Validate all service features of the given AIS
     * 
     * @param ais
     *            the ais
     * @return List with issues as result of the validation
     */
    public static List<AISIssue> validateServiceFeatures(AIS ais) {
        List<AISIssue> issueList = new ArrayList<AISIssue>();
        
        for (AISServiceFeature sf : ais.getServiceFeatures()) {
            issueList.addAll(validateServiceFeature(sf));
        }
        
        return issueList;
    }
    
    
    /**
     * Validate the given service feature
     * 
     * @param sf
     *            the service feature
     * @return List with issues as result of the validation
     */
    public static List<AISIssue> validateServiceFeature(AISServiceFeature sf) {
        List<AISIssue> issueList = new ArrayList<AISIssue>();
        
        issueList.addAll(validateNames(sf));
        issueList.addAll(validateDescriptions(sf));
        
        return issueList;
    }
    
    
    /**
     * Validate the given AppInformationSet (ais) Check, that there are no
     * different Service Features which address the same Privacy Settings and
     * the same required values of those Privacy Settings.
     * 
     * @param ais
     *            the AppInformationSet
     */
    public static void validateAISDiffPSValuesForDiffSFs(AIS ais) {
        
        for (AISServiceFeature sf : ais.getServiceFeatures()) {
            
            // Iterate through all other Service Features
            COMPARE_SF: for (AISServiceFeature sfCompare : ais.getServiceFeatures()) {
                
                // If it's the same sf identifier, continue
                if (sf.getIdentifier().equals(sfCompare.getIdentifier())) {
                    continue COMPARE_SF;
                }
                
                // Continue, if they have a different number of RRGs
                if (sf.getRequiredResourceGroups().size() != sfCompare.getRequiredResourceGroups().size()) {
                    continue COMPARE_SF;
                }
                for (AISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
                    
                    // Iterate through all RRGs of the sfCompare
                    for (AISRequiredResourceGroup rrgCompare : sfCompare.getRequiredResourceGroups()) {
                        
                        // Continue, if the RRGs do not have the same identifier
                        if (!rrg.getIdentifier().equals(rrgCompare.getIdentifier())) {
                            continue COMPARE_SF;
                        }
                        
                        // Continue, if they have a different number of PSs
                        // within one RRG
                        if (rrg.getRequiredPrivacySettings().size() != rrgCompare.getRequiredPrivacySettings().size()) {
                            continue COMPARE_SF;
                        }
                        
                        // Iterate through all PSs of the rrg
                        for (AISRequiredPrivacySetting ps : rrg.getRequiredPrivacySettings()) {
                            
                            // Iterate through all PSs of the rrgCompare
                            for (AISRequiredPrivacySetting psCompare : rrgCompare.getRequiredPrivacySettings()) {
                                
                                // Continue, if they have different PSs
                                // identifier
                                if (!ps.getIdentifier().equals(psCompare.getIdentifier())) {
                                    continue COMPARE_SF;
                                }
                                
                                // Continue, of they have different PSs values
                                if (!ps.getValue().equals(psCompare.getValue())) {
                                    continue COMPARE_SF;
                                }
                            }
                            
                        }
                        
                    }
                    
                }
                /*
                 * If we reach this line, the both Service Feature have the same
                 * RRGs, the same PSs within the RGGs and the same values of the
                 * PSs
                 */
                throw new ParserException(
                        Type.AT_LEAST_TWO_SFS_ADDRESS_SAME_RRGS_AND_PSS,
                        "At least two Service Features address the same required Resourcegroups and the same Privacy Settings with keys and values. This is not allowed.");
                
            }
            
        }
    }
    
}
