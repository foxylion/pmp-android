package de.unistuttgart.ipvs.pmp.model.assertion;

/**
 * Basic error class for Asserts in order to have a common constructor.
 * 
 * @author Tobias Kuhn
 * 
 */
public class AssertError extends Error {
    
    private static final long serialVersionUID = -3424948652612899318L;
    
    
    public AssertError(String text) {
        super(text);
    }
}
