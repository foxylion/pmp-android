package de.unistuttgart.ipvs.pmp.model;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.PMPConditions;

/**
 * Some assertions to check validity of method parameter inputs.
 * 
 * @author Jakob Jarosch
 */
public class ModelConditions extends PMPConditions {

    /**
     * Checks if the given {@link Object} is null.
     * 
     * @param variableName variable name of the checked object
     * @param object to be checked {@link Object}
     * 
     * @throws IllegalArgumentException when assertion fails
     */
    public static void assertNotNull(String variableName, Object object) {
	if (object == null) {
	    throw_IllegalArgumentException("The " + variableName
		    + " must not be null.");
	}
    }

    /**
     * Checks if the given {@link String} is not null or empty.
     * 
     * @param variableName variable name of the checked {@link String}
     * @param string to be checked {@link String}
     * 
     * @throws IllegalArgumentException when assertion fails
     */
    public static void assertStringNotNullOrEmpty(String variableName,
	    String string) {
	if (string == null || string.length() == 0) {
	    throw_IllegalArgumentException("The " + variableName
		    + " must be not NULL and also not empty.");
	}
    }

    /**
     * Checks if the given int is non negative}.
     * 
     * @param variableName variable name of the checked {@link String}
     * @param string to be checked {@link String}
     * 
     * @throws IllegalArgumentException when assertion fails
     */
    public static void assertServiceLevelIdNotNegative(int level) {
	if (level < 0) {
	    throw_IllegalArgumentException("The level must be a non negative int.");
	}
    }

    /**
     * Checks if the given publicKey is not null or empty.
     * 
     * @param publicKey key to be checked
     * 
     * @throws IllegalArgumentException when assertion fails
     */
    public static void assertPublicKeyNotNullOrEmpty(byte[] publicKey) {
	if (publicKey == null || publicKey.length == 0) {
	    throw_IllegalArgumentException("The publicKey must be not NULL and also not empty.");
	}
    }

    /**
     * Checks if the given {@link PMPComponentType} is not null.
     * 
     * @param type {@link PMPComponentType} to be checked
     * 
     * @throws IllegalArgumentException when assertion fails
     */
    public static void assertPMPComponentTypeNotNull(PMPComponentType type) {
	if (type == null) {
	    throw_IllegalArgumentException("The preset type must not be null, use PMPComponentType.NONE instead");
	}
    }

    /**
     * Checks if the given {@link PMPComponentType} and identifier-{@link String} matches.
     * 
     * @param type {@link PMPComponentType} to be checked
     * @param identifier Identifier to be checked
     * 
     * @throws IllegalArgumentException when assertion fails
     */
    public static void assertPMPComponentTypeAndIdentiferMatch(
	    PMPComponentType type, String identifier) {
	if (type != PMPComponentType.NONE && (identifier == null || identifier.length() == 0)) {
	    throw_IllegalArgumentException("The preset type is not NONE, so the identifier must not be empty or null");
	}
    }
}
