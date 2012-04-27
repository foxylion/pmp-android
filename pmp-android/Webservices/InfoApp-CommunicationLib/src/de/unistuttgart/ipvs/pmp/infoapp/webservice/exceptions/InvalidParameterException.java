package de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions;

import java.io.IOException;

public class InvalidParameterException extends IOException {
    
    private static final long serialVersionUID = -2092222093133784651L;
    
    
    public InvalidParameterException(String s) {
        super(s);
    }
}
