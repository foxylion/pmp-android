package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

/**
 * Message which is submitted when no valid request was received.
 * 
 * @author Jakob Jarosch
 */
public class InvalidRequestResponse extends AbstractResponse {
    
    private static final long serialVersionUID = 3L;
    private String message;
    
    
    public InvalidRequestResponse(String message) {
        this.message = message;
    }
    
    
    public String getMessage() {
        return this.message;
    }
}
