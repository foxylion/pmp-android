package de.unistuttgart.ipvs.pmp.resource.privacylevel;

import java.util.Map;

/**
 * {@link LocalizedDefaultPrivacyLevel} for {@link Boolean}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class BooleanPrivacyLevel extends LocalizedDefaultPrivacyLevel<Boolean> {
    
    /**
     * @see {@link LocalizedDefaultPrivacyLevel#LocalizedDefaultPrivacyLevel(Map, Map)}
     */
    public BooleanPrivacyLevel(Map<String, String> names, Map<String, String> descriptions) {
        super(names, descriptions);
    }
    
    
    /**
     * @see {@link LocalizedDefaultPrivacyLevel#LocalizedDefaultPrivacyLevel(String, String)}
     */
    public BooleanPrivacyLevel(String defaultName, String defaultDescription) {
        super(defaultName, defaultDescription);
    }
    
    
    @Override
    public Boolean parseValue(String value) {
        return Boolean.valueOf(value);
    }
    
}
