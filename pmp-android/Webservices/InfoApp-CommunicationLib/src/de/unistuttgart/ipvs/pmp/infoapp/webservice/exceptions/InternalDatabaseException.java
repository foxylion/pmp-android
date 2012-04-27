package de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions;

import java.io.IOException;

public class InternalDatabaseException extends IOException {
    
    private static final long serialVersionUID = -2092222093133784651L;
    
    
    public InternalDatabaseException(String s) {
        super(s);
    }
}
