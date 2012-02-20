package de.unistuttgart.ipvs.pmp.model.xml;

import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;

/**
 * Exception if the {@link PresetSet} contained any element that cannot be resolved because it is not actually
 * installed.
 * 
 * @author Tobias Kuhn
 * 
 */
public class InvalidPresetSetException extends Exception {
    
    private static final long serialVersionUID = 5000448075555827715L;
    
    
    public InvalidPresetSetException() {
        super();
    }
    
    
    public InvalidPresetSetException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    
    
    public InvalidPresetSetException(String detailMessage) {
        super(detailMessage);
    }
    
    
    public InvalidPresetSetException(Throwable throwable) {
        super(throwable);
    }
    
}
