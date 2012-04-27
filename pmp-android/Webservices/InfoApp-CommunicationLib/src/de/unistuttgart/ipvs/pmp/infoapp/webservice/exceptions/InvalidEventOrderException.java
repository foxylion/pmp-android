package de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions;

import java.io.IOException;

public class InvalidEventOrderException extends IOException {
    
    private static final long serialVersionUID = -2092222093133784651L;
    
    
    public InvalidEventOrderException(String s) {
        super(s);
    }
}
