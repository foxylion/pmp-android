package de.unistuttgart.ipvs.pmp.model.assertion;

/**
 * If the model is called with invalid options or in an invalid state, then this {@link ModelMisuseError} is thrown.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ModelMisuseError extends AssertError {
    
    public ModelMisuseError(String text) {
        super(text);
    }
    
    private static final long serialVersionUID = 6785852992592842128L;
    
}
