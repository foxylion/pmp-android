package de.unistuttgart.ipvs.pmp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.ContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
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
    private Map<IModelElement, Map<String, Preset>> presets;
    private Map<ResourceGroup, Map<String, PrivacySetting>> privacySettings;
    private Map<String, ResourceGroup> resourceGroups;
    private Map<App, Map<String, ServiceFeature>> serviceFeatures;
    private List<IContext> contexts;
    private Map<Preset, Map<IPrivacySetting, List<ContextAnnotation>>> contextAnnotations;
    
    
    public ModelCache() {
        this.apps = new HashMap<String, App>();
        this.presets = new HashMap<IModelElement, Map<String, Preset>>();
        this.privacySettings = new HashMap<ResourceGroup, Map<String, PrivacySetting>>();
        this.resourceGroups = new HashMap<String, ResourceGroup>();
        this.serviceFeatures = new HashMap<App, Map<String, ServiceFeature>>();
        this.contexts = new ArrayList<IContext>();
        this.contextAnnotations = new HashMap<Preset, Map<IPrivacySetting, List<ContextAnnotation>>>();
    }
    
    
    public Map<String, App> getApps() {
        return this.apps;
    }
    
    
    public Map<IModelElement, Map<String, Preset>> getPresets() {
        return this.presets;
    }
    
    
    public Map<ResourceGroup, Map<String, PrivacySetting>> getPrivacySettings() {
        return this.privacySettings;
    }
    
    
    public Map<String, ResourceGroup> getResourceGroups() {
        return this.resourceGroups;
    }
    
    
    public Map<App, Map<String, ServiceFeature>> getServiceFeatures() {
        return this.serviceFeatures;
    }
    
    
    public List<Preset> getAllPresets() {
        List<Preset> result = new ArrayList<Preset>();
        for (Map<String, Preset> creatorMap : this.presets.values()) {
            result.addAll(creatorMap.values());
        }
        return result;
    }
    
    
    public List<IContext> getContexts() {
        return this.contexts;
    }
    
    
    public Map<Preset, Map<IPrivacySetting, List<ContextAnnotation>>> getContextAnnotations() {
        return this.contextAnnotations;
    }
    
    
    public List<IContextAnnotation> getAllContextAnnotations() {
        List<IContextAnnotation> result = new ArrayList<IContextAnnotation>();
        
        for (Map<IPrivacySetting, List<ContextAnnotation>> psMap : this.contextAnnotations.values()) {
            for (List<ContextAnnotation> list : psMap.values()) {
                result.addAll(list);
            }
        }
        return result;
    }
}
