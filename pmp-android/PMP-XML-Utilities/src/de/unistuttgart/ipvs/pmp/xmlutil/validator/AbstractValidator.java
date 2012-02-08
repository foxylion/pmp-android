package de.unistuttgart.ipvs.pmp.xmlutil.validator;

import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException.Type;

public class AbstractValidator {
    
    /**
     * This method validates, if a given locale attribute exists and is valid.
     * 
     * @param nodeResultList
     *            the given node result list array.
     */
    public void validateLocaleAttribute(List<String[]> nodeResultList) {
        // Check all nodes
        for (String[] nodeArray : nodeResultList) {
            // Check, if the locale is missing
            if (nodeArray[1].equals("")) {
                throw new ParserException(Type.LOCALE_MISSING, "The locale of " + nodeArray[0] + " is missing!");
            }
            // Check, if the locale is valid
            if (!checkLocale(nodeArray[1])) {
                throw new ParserException(Type.LOCALE_INVALID, "The locale " + nodeArray[1] + " of " + nodeArray[0]
                        + " is invalid!");
            }
        }
    }
    
    
    /**
     * Check, if the given locale (as string) is valid.
     * 
     * @param givenLocale
     *            locale to check (as string)
     * @return flag, if the given local is valid or not.
     */
    public boolean checkLocale(String givenLocale) {
        for (String locale : Locale.getISOLanguages()) {
            if (locale.equals(givenLocale)) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Check, if the lang attribute value of a given lang attribute equals "en"
     * 
     * @param langAttributeValue
     *            the lang attribute value
     */
    public void validateLocaleAttributeEN(String langAttributeValue) {
        if (!langAttributeValue.equals("en")) {
            throw new ParserException(Type.LOCALE_INVALID,
                    "The lang attribute value of the default name/description has to be \"en\"");
        }
    }
    
    
    /**
     * The method validates, if the identifier is set
     * 
     * @param identifier
     *            identifier to validate
     */
    public void validateIdentifier(String identifier) {
        if (identifier == null || identifier.equals("")) {
            throw new ParserException(Type.IDENTIFIER_MISSING, "The identifier of the resource group is missing.");
        }
    }
    
    
    /**
     * The method validates, if a given value is set
     * 
     * @param value
     *            value to validate
     */
    public void validateValueNotEmpty(String value) {
        if (value == null || value.equals("")) {
            throw new ParserException(Type.VALUE_MISSING, "The value of a node is empty.");
        }
    }
    
    
    /**
     * The method validates, if a given list of string value are set
     * 
     * @param values
     *            values to validate
     */
    public void validateValueListNotEmpty(List<String[]> values) {
        for (String[] stringArray : values) {
            for (String element : stringArray) {
                validateValueNotEmpty(element);
            }
        }
    }
    
}
