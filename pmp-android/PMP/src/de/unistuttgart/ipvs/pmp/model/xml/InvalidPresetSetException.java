package de.unistuttgart.ipvs.pmp.model.xml;

import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;

/**
 * Exception if the {@link PresetSet} contained Apps or RGs that are not actually installed.
 * 
 * @author Tobias Kuhn
 * 
 */
public class InvalidPresetSetException extends Exception {
    
    private static final long serialVersionUID = 5000448075555827715L;
    
}
