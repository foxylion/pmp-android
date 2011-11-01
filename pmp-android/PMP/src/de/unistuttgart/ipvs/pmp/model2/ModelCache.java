package de.unistuttgart.ipvs.pmp.model2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model2.element.app.App;
import de.unistuttgart.ipvs.pmp.model2.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.model2.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model2.element.servicelevel.ServiceLevel;

/**
 * Internal cached data storage object for the model and {@link PersistenceProvider} (and its descendants) to access the
 * same cache.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ModelCache {
    
    /**
     * the data stored in the cache
     */
    private Map<String, App> apps;
    private List<Preset> presets;
    private Map<ResourceGroup, Map<String, PrivacyLevel>> privacyLevels;
    private Map<String, ResourceGroup> resourceGroups;
    private Map<App, List<ServiceLevel>> serviceLevels;
    
    
    public ModelCache() {
        this.apps = new HashMap<String, App>();
        this.presets = new ArrayList<Preset>();
        this.privacyLevels = new HashMap<ResourceGroup, Map<String, PrivacyLevel>>();
        this.resourceGroups = new HashMap<String, ResourceGroup>();
        this.serviceLevels = new HashMap<App, List<ServiceLevel>>();
    }
    
    
    public Map<String, App> getApps() {
        return this.apps;
    }
    
    
    public List<Preset> getPresets() {
        return this.presets;
    }
    
    
    public Map<ResourceGroup, Map<String, PrivacyLevel>> getPrivacyLevels() {
        return this.privacyLevels;
    }
    
    
    public Map<String, ResourceGroup> getResourceGroups() {
        return this.resourceGroups;
    }
    
    
    public Map<App, List<ServiceLevel>> getServiceLevels() {
        return this.serviceLevels;
    }
    
}
