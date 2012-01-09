package de.unistuttgart.ipvs.pmp.model.element.missing;

import de.unistuttgart.ipvs.pmp.model.element.app.App;

/**
 * Object to inform about missing {@link App}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class MissingApp {
    
    private final String app;
    
    
    public MissingApp(String app) {
        this.app = app;
    }
    
    
    public String getApp() {
        return this.app;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s [%s]", super.toString(), this.app);
    }
}
