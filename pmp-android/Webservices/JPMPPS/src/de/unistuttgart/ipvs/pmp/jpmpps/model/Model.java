package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Very simple model for the {@link ResourceGroup}s.
 * 
 * @author Jakob Jarosch
 */
public class Model {
    
    private static Model instance = null;
    
    
    private Model() {
        
    }
    
    
    /**
     * @return Returns the instance of the model.
     */
    public static Model get() {
        if (instance == null) {
            instance = new Model();
        }
        
        return instance;
    }
    
    /**
     * All current visible {@link ResourceGroup}s are in this map.
     */
    private Map<String, ResourceGroup> resourceGroups = new HashMap<String, ResourceGroup>();
    
    
    
    
    /**
     * @return Returns all available {@link ResourceGroup}.
     */
    public Map<String, ResourceGroup> getResourceGroups() {
        return resourceGroups;
    }
    
    /**
     * Replaces the current {@link ResourceGroup}s with a new version.
     */
    public void replaceResourceGroups(Map<String, ResourceGroup> resourceGroups) {
        this.resourceGroups = resourceGroups;
    }
}
