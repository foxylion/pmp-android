package de.unistuttgart.ipvs.pmp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
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
    private Map<ModelElement, Map<String, Preset>> presets;
    private Map<ResourceGroup, Map<String, PrivacySetting>> privacySettings;
    private Map<String, ResourceGroup> resourceGroups;
    private Map<App, Map<String, ServiceFeature>> serviceFeatures;
    
    
    public ModelCache() {
        this.apps = new HashMap<String, App>();
        this.presets = new HashMap<ModelElement, Map<String,Preset>>();
        this.privacySettings = new HashMap<ResourceGroup, Map<String, PrivacySetting>>();
        this.resourceGroups = new HashMap<String, ResourceGroup>();
        this.serviceFeatures = new HashMap<App, Map<String, ServiceFeature>>();
    }
    
    
    public Map<String, App> getApps() {
        return this.apps;
    }
    
    
    public Map<ModelElement, Map<String, Preset>> getPresets() {
        return this.presets;
    }
    
    
    public Map<ResourceGroup, Map<String, PrivacySetting>> getPrivacyLevels() {
        return this.privacySettings;
    }
    
    
    public Map<String, ResourceGroup> getResourceGroups() {
        return this.resourceGroups;
    }
    
    
    public Map<App, Map<String, ServiceFeature>> getServiceLevels() {
        return this.serviceFeatures;
    }


    public List<Preset> getAllPresets() {
        List<Preset> result = new ArrayList<Preset>();
        for (ModelElement me : this.presets.keySet()) {
            result.addAll(this.presets.get(me).values());
        }
        return result;
    }
    
}
