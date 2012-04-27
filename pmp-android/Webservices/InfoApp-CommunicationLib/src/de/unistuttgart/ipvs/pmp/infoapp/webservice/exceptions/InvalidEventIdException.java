package de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions;

import java.io.IOException;

public class InvalidEventIdException extends IOException {
    
    private static final long serialVersionUID = -2092222093133784651L;
    
    
    public InvalidEventIdException(String s) {
        super(s);
    }
}
