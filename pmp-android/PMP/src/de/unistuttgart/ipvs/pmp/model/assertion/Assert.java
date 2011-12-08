package de.unistuttgart.ipvs.pmp.model.assertion;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

/**
 * A static assertion class much like in the old model. Performs all kinds of checks and reacts to errors.
 * 
 * @author Tobias Kuhn
 * 
 */
public class Assert {
    
    /*
     * all kinds of strings used in error messages
     */
    public static String ILLEGAL_NULL = "'%s' (%s) was expected to be an Object, found null instead.";
    public static String ILLEGAL_NOT_NULL = "'%s' (%s) was expected to be null, found Object instead.";
    public static final String ILLEGAL_CREATOR = "'%s' (%s) was expected to be a Creator parameter, found something else instead.";
    
    
    /**
     * Formats a predefined error message for object reference.
     * 
     * @param errString
     * @param refName
     * @param reference
     * @return an error message mentioning correct references.
     */
    public static String format(String errString, String refName, Object reference) {
        if (reference != null) {
            return String.format(errString, refName, reference.toString());
        } else {
            return String.format(errString, refName, "null");
        }
    }
    
    
    /**
     * Checks whether check is not null. Throws reaction, if check == null.
     * 
     * @param check
     * @param reaction
     */
    public static void nonNull(Object check, Error reaction) {
        if (check == null) {
            Log.e("Assertion", reaction);
            throw reaction;
        }
    }
    
    
    /**
     * Checks whether check is null. Throws reaction, if check != null.
     * 
     * @param check
     * @param reaction
     */
    public static void isNull(Object check, Error reaction) {
        if (check != null) {
            Log.e("Assertion", reaction);
            throw reaction;
        }
    }
    
    
    /**
     * Checks whether check is a valid creator. Throws reaction, if check is not.
     * 
     * @param check
     * @param reaction
     */
    public static void isValidCreator(Object check, Error reaction) {
        if ((check != null) && !(check instanceof IApp) && !(check instanceof IResourceGroup)) {
            Log.e("Assertion", reaction);
            throw reaction;
        }
    }
    
}
