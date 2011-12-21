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
    public static final String ILLEGAL_NULL = "'%s' was expected to be an Object, found null instead. (%s)";
    public static final String ILLEGAL_NOT_NULL = "'%s' was expected to be null, found Object instead. (%s)";
    public static final String ILLEGAL_CREATOR = "'%s' was expected to be a Creator parameter, found something else instead. (%s)";
    public static final String ILLEGAL_METHOD = "'%s' was expecting this call to never happen, found call anyhow. (%s)";
    public static final String ILLEGAL_DB = "'%s' was expecting a database query to return results, found none instead. (%s)";
    public static final String ILLEGAL_CLASS = "'%s' was expected to be a model class, found a different one instead. (%s)";
    public static final String ILLEGAL_UNCACHED = "'%s' was expected to be cached, found no cache however. (%s)";
    public static final String ILLEGAL_SIMPLE_MODE = "'%s' was expected to be prepared for simple model, found expert mode configuration instead. (%s)";
    public static final String ILLEGAL_TYPE = "'%s' was expected to be of a type known to the model, found an unknown instead. (%s)";
    public static final String ILLEGAL_UNINSTALLED_ACCESS = "'%s' was expected to be installed, found nothing instead. (%s)";
    public static final String ILLEGAL_ALREADY_INSTALLED = "'%s' was expected to be not installed, found it in the model however. (%s)";
    public static final String ILLEGAL_MISSING_FILE = "'%s' was expected to be an existing file, found it missing however. (%s)";
    
    
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
    
    
    /**
     * Checks whether check is instanceof clazz. Throws reaction, if check is not.
     * 
     * @param check
     * @param clazz
     * @param reaction
     */
    public static void instanceOf(Object check, Class<?> clazz, Error reaction) {
        if (!clazz.isAssignableFrom(check.getClass())) {
            Log.e("Assertion", reaction);
            throw reaction;
        }
    }
    
}
