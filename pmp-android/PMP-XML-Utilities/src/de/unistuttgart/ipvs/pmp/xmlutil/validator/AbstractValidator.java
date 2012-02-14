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
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

public class AbstractValidator {
    
    /**
     * Validate the names of a given {@link IssueLocation}
     * 
     * @param location
     *            the IssueLocation
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public static List<Issue> validateNames(IssueLocation location, boolean attachData) {
        List<Issue> issueList = new ArrayList<Issue>();
        
        // Check, if this location has names and descriptions (extends from BasicIS)
        if (location instanceof BasicIS) {
            boolean englishLocaleExists = false;
            List<String> localesOccurred = new ArrayList<String>();
            
            for (Name name : ((BasicIS) location).getNames()) {
                
                // Instantiate possible issues
                Issue localeMissing = new Issue(IssueType.LOCALE_MISSING, name);
                Issue localeInvalid = new Issue(IssueType.LOCALE_INVALID, name);
                Issue nameEmpty = new Issue(IssueType.EMPTY_VALUE, name);
                
                // Flag, if the locale is missing
                boolean localeAvailable = true;
                
                // Check, if the locale is set
                if (name.getLocale() == null || !checkValueSet(name.getLocale().getLanguage())) {
                    issueList.add(localeMissing);
                    if (attachData)
                        name.addIssue(localeMissing);
                    localeAvailable = false;
                    
                } else if (!checkLocale(name.getLocale())) {
                    // if the locale is invalid
                    issueList.add(localeInvalid);
                    if (attachData)
                        name.addIssue(localeInvalid);
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
                        for (Issue issueExisting : issueList) {
                            if (issueExisting.getType().equals(IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN)
                                    && (issueExisting).getLocation().equals(location)
                                    && issueExisting.getParameters().size() == 1
                                    && issueExisting.getParameters().get(0).equals(locale)) {
                                issueAlreadyExists = true;
                            }
                        }
                        if (!issueAlreadyExists) {
                            Issue localesOccurredTooOften = new Issue(IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN,
                                    location);
                            localesOccurredTooOften.addParameter(locale);
                            issueList.add(localesOccurredTooOften);
                            if (attachData)
                                location.addIssue(localesOccurredTooOften);
                        }
                        
                    }
                }
                
                // Check, if the name is set
                if (!checkValueSet(name.getName())) {
                    issueList.add(nameEmpty);
                    if (attachData)
                        name.addIssue(nameEmpty);
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
                Issue localeEnMissing = new Issue(IssueType.NAME_LOCALE_EN_MISSING, location);
                issueList.add(localeEnMissing);
                if (attachData)
                    location.addIssue(localeEnMissing);
            }
            
        }
        
        return issueList;
        
    }
    
    
    /**
     * Validate the description of a given {@link IssueLocation}
     * 
     * @param location
     *            the IssueLocation
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public static List<Issue> validateDescriptions(IssueLocation location, boolean attachData) {
        List<Issue> issueList = new ArrayList<Issue>();
        
        // Check, if this location has descriptions (extends from BasicIS)
        if (location instanceof BasicIS) {
            for (Description descr : ((BasicIS) location).getDescriptions()) {
                
            }
        }
        
        return issueList;
    }
    
    
    /**
     * Check, if the lang attribute value of a given lang attribute equals "en"
     * 
     * @param locale
     *            the locale to validate
     */
    protected static boolean checkLocaleAttributeEN(Locale locale) {
        return locale.getLanguage().equals("en");
    }
    
    
    /**
     * Check, if the given locale is valid.
     * 
     * @param givenLocale
     *            locale to check
     * @return flag, if the given local is valid or not.
     */
    protected static boolean checkLocale(Locale givenLocale) {
        for (String locale : Locale.getISOLanguages()) {
            if (locale.equals(givenLocale.getLanguage())) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * The method validates, if a given value is set
     * 
     * @param value
     *            value to validate
     * @return flag, if the value is set or not
     */
    protected static boolean checkValueSet(String value) {
        return !(value == null || value.equals(""));
    }
    
    //    /**
    //     * This method validates, if a given locale attribute exists and is valid.
    //     * 
    //     * @param nodeResultList
    //     *            the given node result list array.
    //     */
    //    public void validateLocaleAttribute(List<String[]> nodeResultList) {
    //        // Check all nodes
    //        for (String[] nodeArray : nodeResultList) {
    //            // Check, if the locale is missing
    //            if (nodeArray[1].equals("")) {
    //                throw new ParserException(Type.LOCALE_MISSING, "The locale of " + nodeArray[0] + " is missing!");
    //            }
    //            // Check, if the locale is valid
    //            if (!checkLocale(nodeArray[1])) {
    //                throw new ParserException(Type.LOCALE_INVALID, "The locale " + nodeArray[1] + " of " + nodeArray[0]
    //                        + " is invalid!");
    //            }
    //        }
    //    }
    //    
    //    
    
    //    
    //    
    //    
    //    
    //    /**
    //     * The method validates, if the identifier is set
    //     * 
    //     * @param identifier
    //     *            identifier to validate
    //     */
    //    public void validateIdentifier(String identifier) {
    //        if (identifier == null || identifier.equals("")) {
    //            throw new ParserException(Type.IDENTIFIER_MISSING, "The identifier of the resource group is missing.");
    //        }
    //    }
    //    
    //    
    //    /**
    //     * The method validates, if a given value is set
    //     * 
    //     * @param value
    //     *            value to validate
    //     */
    //    public void validateValueNotEmpty(String value) {
    //        if (value == null || value.equals("")) {
    //            throw new ParserException(Type.VALUE_MISSING, "The value of a node is empty.");
    //        }
    //    }
    //    
    //    
    //    /**
    //     * The method validates, if a given list of string value are set
    //     * 
    //     * @param values
    //     *            values to validate
    //     */
    //    public void validateValueListNotEmpty(List<String[]> values) {
    //        for (String[] stringArray : values) {
    //            for (String element : stringArray) {
    //                validateValueNotEmpty(element);
    //            }
    //        }
    //    }
    
}
