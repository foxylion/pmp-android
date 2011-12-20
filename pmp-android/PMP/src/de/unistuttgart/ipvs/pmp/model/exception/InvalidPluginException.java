package de.unistuttgart.ipvs.pmp.model.exception;

/**
 * An exception that is thrown whenever a plugin is registered that is somehow corrupt.
 * 
 * @author Tobias Kuhn
 * 
 */
public class InvalidPluginException extends Exception {
    
    private static final long serialVersionUID = -4691956490321050959L;
    
    
    /**
     * @see {@link Exception#Exception()}
     */
    public InvalidPluginException() {
        super();
    }
    
    
    /**
     * @see {@link Exception#Exception(String, Throwable)}
     */
    public InvalidPluginException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    
    
    /**
     * @see {@link Exception#Exception(String)}
     */
    public InvalidPluginException(String detailMessage) {
        super(detailMessage);
    }
    
    
    /**
     * @see {@link Exception#Exception(Throwable)}
     */
    public InvalidPluginException(Throwable throwable) {
        super(throwable);
    }
    
}
