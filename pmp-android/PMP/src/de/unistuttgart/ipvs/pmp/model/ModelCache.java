package de.unistuttgart.ipvs.pmp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;

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
    private Map<ResourceGroup, Map<String, PrivacySetting>> privacySettings;
    private Map<String, ResourceGroup> resourceGroups;
    private Map<App, List<ServiceFeature>> serviceFeatures;
    
    
    public ModelCache() {
        this.apps = new HashMap<String, App>();
        this.presets = new ArrayList<Preset>();
        this.privacySettings = new HashMap<ResourceGroup, Map<String, PrivacySetting>>();
        this.resourceGroups = new HashMap<String, ResourceGroup>();
        this.serviceFeatures = new HashMap<App, List<ServiceFeature>>();
    }
    
    
    public Map<String, App> getApps() {
        return this.apps;
    }
    
    
    public List<Preset> getPresets() {
        return this.presets;
    }
    
    
    public Map<ResourceGroup, Map<String, PrivacySetting>> getPrivacyLevels() {
        return this.privacySettings;
    }
    
    
    public Map<String, ResourceGroup> getResourceGroups() {
        return this.resourceGroups;
    }
    
    
    public Map<App, List<ServiceFeature>> getServiceLevels() {
        return this.serviceFeatures;
    }
    
}