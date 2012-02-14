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
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class AISValidator extends AbstractValidator {
    
    public static void clearAllIssues(AIS ais) {
        
    }
    
    
    /**
     * Validate the whole AIS
     * 
     * @param ais
     *            the ais
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public static List<Issue> validateAIS(AIS ais, boolean attachData) {
        List<Issue> issueList = new ArrayList<Issue>();
        
        // TODO: Clear all issues of all objects in the AIS!
        
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
    public static List<Issue> validateAppInformation(AIS ais, boolean attachData) {
        List<Issue> issueList = new ArrayList<Issue>();
        
        issueList.addAll(validateNames(ais, attachData));
        issueList.addAll(validateDescriptions(ais, attachData));
        
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
    public static List<Issue> validateServiceFeatures(AIS ais, boolean attachData) {
        List<Issue> issueList = new ArrayList<Issue>();
        
        for (AISServiceFeature sf : ais.getServiceFeatures()) {
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
    public static List<Issue> validateServiceFeature(AISServiceFeature sf, boolean attachData) {
        List<Issue> issueList = new ArrayList<Issue>();
        
        issueList.addAll(validateNames(sf, attachData));
        issueList.addAll(validateDescriptions(sf, attachData));
        
        return issueList;
    }
    
    
    /**
     * Validate the given AppInformationSet (ais) Check, that there are no
     * different Service Features which address the same Privacy Settings and
     * the same required values of those Privacy Settings.
     * 
     * @param ais
     *            the AppInformationSet
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     */
    public static void validateAISDiffPSValuesForDiffSFs(AIS ais, boolean attachData) {
        
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
