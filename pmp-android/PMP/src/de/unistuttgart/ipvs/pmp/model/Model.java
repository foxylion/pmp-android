package de.unistuttgart.ipvs.pmp.model;

import java.util.Observable;
import java.util.Observer;

import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;

/**
 * <p>
 * The business logic a.k.a. the domain object model that stores all the {@link App}s, {@link Preset}s,
 * {@link PrivacySetting}s, {@link ResourceGroup}s and {@link ServiceFeature}s in use.
 * </p>
 * 
 * <p>
 * Internally, it automatically creates a {@link ModelCache}, the raw structure without data filled in yet, the
 * {@link ModelElement}s then load the required data on-demand by calling their associated
 * {@link ElementPersistenceProvider} and act as a cache data structure. They also save the data back when necessary.
 * </p>
 * 
 * <p>
 * More internally, the {@link ModelCache} object is maintained by the {@link PersistenceProvider} and its descendants.
 * The model gets this cache by observing the main singleton {@link PersistenceProvider}.
 * </p>
 * 
 * @author Tobias Kuhn
 * 
 */
public class Model implements IModel, Observer {
    
    /**
     * Actual model content. May be null if no cache is present.
     */
    private ModelCache cache;
    
    /**
     * Singleton stuff
     */
    private static final Model instance = new Model();
    
    
    private Model() {
        this.cache = null;
        PersistenceProvider.getInstance().addObserver(this);
    }
    
    
    public static IModel getInstance() {
        return instance;
    }
    
    
    /**
     * Checks whether the model is already cached.If not, loads the data from the persistence and thus creates the
     * whole model data structures.
     */
    private void checkCached() {
        if (this.cache == null) {
            PersistenceProvider.getInstance().reloadDatabaseConnection();
        }
    }
    
    @Override
    public void update(Observable observable, Object data) {
        // new ModelCache from the PersistenceProvider
        this.cache = (ModelCache) data;
    }
    
    /*
     * Actual overridden content
     */
    
    @Override
    public IApp[] getApps() {
        checkCached();
        return this.cache.getApps().values().toArray(new IApp[0]);
    }
    
    
    @Override
    public IApp getApp(String identifier) {
        checkCached();
        return this.cache.getApps().get(identifier);
    }
    
    
    @Override
    public void registerApp(String identifier) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public boolean unregisterApp(String identifier) {
        return false;
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public IResourceGroup[] getResourceGroups() {
        checkCached();
        return this.cache.getResourceGroups().values().toArray(new IResourceGroup[0]);
    }
    
    
    @Override
    public IResourceGroup getResourceGroup(String identifier) {
        checkCached();
        return this.cache.getResourceGroups().get(identifier);
    }
    
    
    @Override
    public String[] findResourceGroup(String searchString) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean installResourceGroup(String identifier) {
        // TODO Auto-generated method stub
        // TODO remember that illegal service features have to be reenabled once their missing PS get installed
        // TODO Presets as well
        return false;
    }
    
    
    @Override
    public boolean uninstallResourceGroup(String identifier) {
        // TODO Auto-generated method stub
        // TODO remember that  service features have to be disabled once their required PS get uninstalled
        // TODO Presets as well
        return false;
    }
    
    
    @Override
    public IPreset[] getPresets() {
        checkCached();
        return this.cache.getAllPresets().toArray(new IPreset[0]);
    }
    
    @Override
    public IPreset[] getPresets(ModelElement creator) {
        checkCached();
        return this.cache.getPresets().get(creator).values().toArray(new IPreset[0]);
    }
    
    @Override
    public IPreset getPreset(ModelElement creator, String identifier) {
        checkCached();
        return this.cache.getPresets().get(creator).get(identifier);
    }
    
    
    @Override
    public IPreset addPreset(ModelElement creator, String identifier, String name, String description) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean removePreset(ModelElement creator, String identifier) {
        return false;
        // TODO Auto-generated method stub
        
    }
    
       
    
}
