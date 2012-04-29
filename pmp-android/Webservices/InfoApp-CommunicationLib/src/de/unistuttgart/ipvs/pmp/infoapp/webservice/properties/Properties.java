package de.unistuttgart.ipvs.pmp.infoapp.webservice.properties;

import java.io.IOException;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;

public abstract class Properties {
    
    protected Service service;
    
    
    public Properties(Service service) {
        this.service = service;
    }
    
    
    public abstract void commit() throws IOException;
    
}
