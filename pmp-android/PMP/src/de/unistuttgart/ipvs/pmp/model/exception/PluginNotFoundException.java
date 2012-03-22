package de.unistuttgart.ipvs.pmp.model.exception;

import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;

public class PluginNotFoundException extends ModelMisuseError {
    
    private static final long serialVersionUID = 1370793487506591588L;
    
    
    public PluginNotFoundException(String text) {
        super(text);
    }
}
