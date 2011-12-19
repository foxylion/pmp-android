package de.unistuttgart.ipvs.pmp.gui.mockup;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.IModel;
import de.unistuttgart.ipvs.pmp.model.ModelCache;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model.ipc.IPCProvider;

/**
 * Mockup model guaranteed to not persist anything or communicate with anyone while trying to maintain the main
 * infrastructure.
 * 
 * @author Tobias Kuhn
 * 
 */
public class MockupModel implements IModel {
    
    public static final MockupModel instance = new MockupModel();
    
    private ModelCache mc;
    
    
    private MockupModel() {
        this.mc = new ModelCache();
        IPCProvider.getInstance().startUpdate();
    }
    
    
    @Override
    public IApp[] getApps() {
        Collection<App> result = this.mc.getApps().values();
        return result.toArray(new IApp[result.size()]);
    }
    
    
    @Override
    public IApp getApp(String identifier) {
        return this.mc.getApps().get(identifier);
    }
    
    
    @Override
    @Deprecated
    public void registerApp(String identifier) {
    }
    
    
    public void registerApp(String identifier, MockupApp app) {
        this.mc.getApps().put(identifier, app);
    }
    
    
    @Override
    public boolean unregisterApp(String identifier) {
        
        App app = this.mc.getApps().get(identifier);
        
        for (IPreset preset : app.getAssignedPresets()) {
            // this time, there's no way but to cast (or run manually through all apps)                     
            Assert.instanceOf(preset, Preset.class, new ModelIntegrityError(Assert.ILLEGAL_CLASS, "preset", preset));
            Preset castPreset = (Preset) preset;
            
            // since these presets were assigned to the app they now are guaranteed not to be available.
            if (!castPreset.isDeleted()) {
                castPreset.forceRecache();
                castPreset.rollout();
            }
        }
        
        return this.mc.getApps().remove(identifier) != null;
    }
    
    
    @Override
    public IResourceGroup[] getResourceGroups() {
        Collection<ResourceGroup> result = this.mc.getResourceGroups().values();
        return result.toArray(new IResourceGroup[result.size()]);
    }
    
    
    @Override
    public IResourceGroup getResourceGroup(String identifier) {
        return this.mc.getResourceGroups().get(identifier);
    }
    
    
    @Override
    @Deprecated
    public boolean installResourceGroup(String identifier) {
        return false;
    }
    
    
    public boolean installResourceGroup(String identifier, MockupRG rg) {
        this.mc.getResourceGroups().put(identifier, rg);
        return true;
    }
    
    
    @Override
    public boolean uninstallResourceGroup(String identifier) {
        return this.mc.getResourceGroups().remove(identifier) != null;
    }
    
    
    @Override
    public IPreset[] getPresets() {
        Collection<Preset> result = this.mc.getAllPresets();
        return result.toArray(new IPreset[result.size()]);
    }
    
    
    @Override
    public IPreset[] getPresets(ModelElement creator) {
        Map<String, Preset> creatorPresets = this.mc.getPresets().get(creator);
        if (creatorPresets == null) {
            return new IPreset[0];
        } else {
            Collection<Preset> result = creatorPresets.values();
            return result.toArray(new IPreset[result.size()]);
        }
    }
    
    
    @Override
    public IPreset getPreset(IModelElement creator, String identifier) {
        Map<String, Preset> creatorPresets = this.mc.getPresets().get(creator);
        if (creatorPresets == null) {
            return null;
        } else {
            return creatorPresets.get(identifier);
        }
    }
    
    
    @Override
    public IPreset addPreset(IModelElement creator, String identifier, String name, String description) {
        Preset newPreset = new MockupPreset(creator, identifier, name, description);
        Map<String, Preset> creatorMap = this.mc.getPresets().get(creator);
        if (creatorMap == null) {
            creatorMap = new HashMap<String, Preset>();
            this.mc.getPresets().put(creator, creatorMap);
        }
        creatorMap.put(identifier, newPreset);
        return newPreset;
    }
    
    
    @Override
    public IPreset addUserPreset(String name, String description) {
        // prepare standard
        Map<String, Preset> creatorMap = this.mc.getPresets().get(null);
        int suffix = 1;
        String identifier = name;
        
        // find free identifier
        while (creatorMap.get(identifier) != null) {
            suffix++;
            identifier = name + suffix;
        }
        
        // create
        return addPreset(null, identifier, name, description);
    }
    
    
    @Override
    public boolean removePreset(IModelElement creator, String identifier) {
        // does the creator map exist?
        Map<String, Preset> creatorMap = this.mc.getPresets().get(creator);
        
        if (creatorMap == null) {
            return false;
        } else {
            Preset p = creatorMap.get(identifier);
            
            for (IApp app : p.getAssignedApps()) {
                // this time, there's no way but to cast (or run manually through all apps)
                Assert.instanceOf(app, App.class, new ModelIntegrityError(Assert.ILLEGAL_CLASS, "app", app));
                App castApp = (App) app;
                castApp.removePreset(p);
            }
            
            return creatorMap.remove(identifier) != null;
        }
    }
    
    
    @Override
    public void clearAll() {
        this.mc.getApps().clear();
        this.mc.getServiceFeatures().clear();
        this.mc.getResourceGroups().clear();
        this.mc.getPrivacySettings().clear();
        this.mc.getPresets().clear();
    }
    
}
