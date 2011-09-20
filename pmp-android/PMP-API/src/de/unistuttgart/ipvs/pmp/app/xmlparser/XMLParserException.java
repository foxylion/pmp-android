package de.unistuttgart.ipvs.pmp.app.xmlparser;

/**
 * This class is used to create parser exceptions with a type and details.
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLParserException extends RuntimeException {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 5368379350530757117L;
    
    /**
     * Possible types a parser exception can have.
     * 
     * @author Marcus Vetter Patrick: Had to change visibility to "public". Otherwise exception-type would be untestable
     */
    public enum Type {
        /** A required node is missing */
        NODE_MISSING,
        /** A node occurred too often */
        NODE_OCCURRED_TOO_OFTEN,
        /** A locale attribute is missing */
        LOCALE_MISSING,
        /** A given locale attribute is invalid */
        LOCALE_INVALID,
        /** The default service level does not have level value "0" */
        DEFAULT_SERVICE_LEVEL_IS_NOT_ZERO,
        /** The default service must have no required resource groups */
        DEFAULT_SERVICE_LEVEL_MUST_HAVE_NO_REQUIRED_RESOURCE_GROUPS,
        /** A name with the same locale already exists */
        NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,
        /** A description with the same locale already exists */
        DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
        /**
         * A service level with the same level already exists within the same service level
         */
        SERVICE_LEVEL_WITH_SAME_LEVEL_ALREADY_EXISTS,
        /**
         * A privacy level with the same identifier already exists within the same required resource group
         */
        PRIVACY_LEVEL_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
        /** The parser crashed, unknown reason */
        CONFIGURATION_EXCEPTION,
        /** The SAX-parser crashed, unknown reason */
        SAX_EXCEPTION,
        /** Something with the input does not work */
        IO_EXCEPTION;
    }
    
    /**
     * Type of the parser exception
     */
    private Type type;
    
    /**
     * Details of the exception
     */
    private String details;
    
    private Throwable throwable;
    
    
    /**
     * @see XMLParserException#XMLParserException(Type, String, Throwable)
     */
    protected XMLParserException(Type type, String details) {
        this(type, details, null);
    }
    
    
    /**
     * Constructor
     * 
     * @param type
     *            of the exception
     * @param details
     *            of the exception
     */
    protected XMLParserException(Type type, String details, Throwable throwable) {
        this.type = type;
        this.details = details;
        this.throwable = throwable;
    }
    
    
    /**
     * Get the type of the exception
     * 
     * @return type of the exception
     */
    public Type getType() {
        return this.type;
    }
    
    
    /**
     * Get the details of the exception
     * 
     * @return details of the exception
     */
    public String getDetails() {
        return this.details;
    }
    
    
    public Throwable getParent() {
        return this.throwable;
    }
}
