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

import java.util.Locale;

public class AbstractValidator {
    
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
