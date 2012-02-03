package de.unistuttgart.ipvs.pmp.model.exception;

import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;

/**
 * An exception that is thrown when a condition was supplied to an {@link IContext} or an {@link IContextView} that was
 * not compatible with the context.
 * 
 * @author Tobias Kuhn
 * 
 */
public class InvalidConditionException extends Exception {
    
    private static final long serialVersionUID = 4150641607569620296L;
    
    
    /**
     * @see {@link Exception#Exception()}
     */
    public InvalidConditionException() {
        super();
    }
    
    
    /**
     * @see {@link Exception#Exception(String, Throwable)}
     */
    public InvalidConditionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    
    
    /**
     * @see {@link Exception#Exception(String)}
     */
    public InvalidConditionException(String detailMessage) {
        super(detailMessage);
    }
    
    
    /**
     * @see {@link Exception#Exception(Throwable)}
     */
    public InvalidConditionException(Throwable throwable) {
        super(throwable);
    }
    
}
