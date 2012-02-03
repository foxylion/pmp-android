package de.unistuttgart.ipvs.pmp.model.assertion;

/**
 * If the model finds itself in a state that it cannot recover from, then this {@link ModelIntegrityError} is thrown.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ModelIntegrityError extends AssertError {
    
    public ModelIntegrityError(String text) {
        super(text);
    }
    
    private static final long serialVersionUID = -478608329593768065L;
    
}
