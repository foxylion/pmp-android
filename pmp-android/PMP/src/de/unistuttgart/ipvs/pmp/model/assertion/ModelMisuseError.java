package de.unistuttgart.ipvs.pmp.model.assertion;

/**
 * If the model is called with invalid options or in an invalid state, then this {@link ModelMisuseError} is thrown.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ModelMisuseError extends Error {
    
    private static final long serialVersionUID = 6785852992592842128L;
    
    
    public ModelMisuseError(String errString, String refName, Object reference) {
        super(Assert.format(errString, refName, reference));
    }
    
}
