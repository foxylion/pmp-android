package de.unistuttgart.ipvs.pmp.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.AppPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.preset.PresetPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySettingPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroupPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeaturePersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.ipc.IPCProvider;
import de.unistuttgart.ipvs.pmp.service.app.RegistrationResult;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.AppServiceConnector;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSetParser;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSetParser;

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
        Assert.nonNull(identifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        
        return this.cache.getApps().get(identifier);
    }
    
    
    @Override
    public void registerApp(final String identifier) {
        checkCached();
        Assert.nonNull(identifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        
        final AppServiceConnector asc = new AppServiceConnector(PMPApplication.getContext(), identifier);
        
        // check XML
        try {
            InputStream xmlStream = PMPApplication.getContext().getPackageManager()
                    .getResourcesForApplication(identifier).getAssets().open(PersistenceConstants.APP_XML_NAME);
            
            AppInformationSet ais = AppInformationSetParser.createAppInformationSet(xmlStream);
            
            // apply new app to DB, then model
            App newApp = new AppPersistenceProvider(null).createElementData(identifier);
            this.cache.getApps().put(identifier, newApp);
            this.cache.getServiceFeatures().put(newApp, new HashMap<String, ServiceFeature>());
            
            // apply new SF to DB, then model
            for (String sfIdentifier : ais.getServiceFeaturesMap().keySet()) {
                ServiceFeature newSF = new ServiceFeaturePersistenceProvider(null).createElementData(newApp,
                        sfIdentifier, ais.getServiceFeaturesMap().get(sfIdentifier).getRequiredResourceGroups());
                this.cache.getServiceFeatures().get(newApp).put(sfIdentifier, newSF);
            }
            
            // remember that illegal presets have to be enabled once their missing apps get installed
            IPCProvider.getInstance().startUpdate();
            try {
                for (Preset p : this.cache.getAllPresets()) {
                    if (!p.isAvailable() && !p.isDeleted()) {
                        p.forceRecache();
                        
                        // if the preset was only missing this app, rollout the changes
                        if (p.isAvailable() && !p.isDeleted()) {
                            p.rollout();
                        }
                    }
                }
            } finally {
                IPCProvider.getInstance().endUpdate();
            }
            
            // "Hello thar, App!"
            asc.addCallbackHandler(new AbstractConnectorCallback() {
                
                @Override
                public void onConnect(AbstractConnector connector) throws RemoteException {
                    asc.getAppService().replyRegistrationResult(new RegistrationResult(true));
                    Log.d(identifier + " has successfully registered with PMP.");
                }
                
                
                @Override
                public void onBindingFailed(AbstractConnector connector) {
                    Log.d(identifier
                            + " would have been successfully registered with PMP, but could not connect to its service.");
                }
            });
            
        } catch (final IOException ioe) {
            /* error during finding files */
            asc.addCallbackHandler(new AbstractConnectorCallback() {
                
                @Override
                public void onConnect(AbstractConnector connector) throws RemoteException {
                    asc.getAppService().replyRegistrationResult(new RegistrationResult(false, ioe.getMessage()));
                }
            });
            Log.w(identifier + " has failed registration with PMP.", ioe);
            
        } catch (final NameNotFoundException nnfe) {
            /* error during finding files */
            asc.addCallbackHandler(new AbstractConnectorCallback() {
                
                @Override
                public void onConnect(AbstractConnector connector) throws RemoteException {
                    asc.getAppService().replyRegistrationResult(new RegistrationResult(false, nnfe.getMessage()));
                }
            });
            Log.w(identifier + " has failed registration with PMP.", nnfe);
            
        } catch (final XMLParserException xmlpe) {
            /* error during XML validation */
            asc.addCallbackHandler(new AbstractConnectorCallback() {
                
                @Override
                public void onConnect(AbstractConnector connector) throws RemoteException {
                    asc.getAppService().replyRegistrationResult(new RegistrationResult(false, xmlpe.getDetails()));
                }
            });
            Log.w(identifier + " has failed registration with PMP.", xmlpe);
        }
        
        // and off you go
        asc.bind();
    }
    
    
    @Override
    public boolean unregisterApp(String identifier) {
        checkCached();
        Assert.nonNull(identifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        
        App app = this.cache.getApps().get(identifier);
        if (app == null) {
            return false;
        } else {
            
            app.delete();
            this.cache.getApps().remove(identifier);
            
            IPCProvider.getInstance().startUpdate();
            try {
                // remember that presets have to be disabled once their required apps get uninstalled
                for (IPreset preset : app.getAssignedPresets()) {
                    // this time, there's no way but to cast (or run manually through all apps)                     
                    Assert.instanceOf(preset, Preset.class, new ModelIntegrityError(Assert.ILLEGAL_CLASS, "preset",
                            preset));
                    Preset castPreset = (Preset) preset;
                    
                    // since these presets were assigned to the app they now are guaranteed not to be available.
                    if (!castPreset.isDeleted()) {
                        castPreset.forceRecache();
                        castPreset.rollout();
                    }
                    
                }
            } finally {
                IPCProvider.getInstance().endUpdate();
            }
            
            return true;
        }
    }
    
    
    @Override
    public IResourceGroup[] getResourceGroups() {
        checkCached();
        return this.cache.getResourceGroups().values().toArray(new IResourceGroup[0]);
    }
    
    
    @Override
    public IResourceGroup[] getInstalledResourceGroups() {
        // FIXME: Warum gibt es diese Funktion? Alle im Model registrierten Komponenten müssen installiert sein.
        checkCached();
        List<IResourceGroup> resourceGroups = new ArrayList<IResourceGroup>(this.cache.getResourceGroups().values());
        // TODO we should not cast so many times. better just use list and hashmap?
        Iterator<IResourceGroup> it = resourceGroups.iterator();
        while (it.hasNext()) {
            
            IResourceGroup resourceGroup = it.next();
            if (resourceGroup.isInstalled() == false) {
                it.remove();
            }
        }
        return resourceGroups.toArray(new IResourceGroup[0]);
    }
    
    
    @Override
    public IResourceGroup getResourceGroup(String identifier) {
        checkCached();
        Assert.nonNull(identifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        return this.cache.getResourceGroups().get(identifier);
    }
    
    
    @Override
    public String[] findResourceGroup(String searchString) {
        Assert.nonNull(searchString, new ModelMisuseError(Assert.ILLEGAL_NULL, "searchString", searchString));
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean installResourceGroup(String identifier, InputStream input) {
        checkCached();
        Assert.nonNull(input, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        Assert.nonNull(input, new ModelMisuseError(Assert.ILLEGAL_NULL, "input", input));
        
        try {
            // TODO unpack(?), install RG
            
            // TODO give correct xml stream here
            InputStream xmlStream = null;
            
            RgInformationSet rgis = RgInformationSetParser.createRgInformationSet(xmlStream);
            
            // apply new RG to DB, then model
            ResourceGroup newRG = new ResourceGroupPersistenceProvider(null).createElementData(identifier);
            this.cache.getResourceGroups().put(identifier, newRG);
            this.cache.getPrivacySettings().put(newRG, new HashMap<String, PrivacySetting>());
            
            // apply new PS to DB, then model
            for (String psIdentifier : rgis.getPrivacySettingsMap().keySet()) {
                PrivacySetting newPS = new PrivacySettingPersistenceProvider(null).createElementData(newRG,
                        psIdentifier);
                this.cache.getPrivacySettings().get(newRG).put(psIdentifier, newPS);
            }
            
            IPCProvider.getInstance().startUpdate();
            try {
                // remember that illegal service features have to be enabled once their missing PS get installed
                for (App app : this.cache.getApps().values()) {
                    boolean appChanged = false;
                    
                    for (ServiceFeature sf : this.cache.getServiceFeatures().get(app).values()) {
                        if (!sf.isAvailable()) {
                            sf.forceRecache();
                            
                            // if the service feature was only missing this RG, rollout the changes
                            if (sf.isAvailable()) {
                                appChanged = true;
                            }
                        }
                    }
                    
                    if (appChanged) {
                        app.verifyServiceFeatures();
                    }
                }
                
                // remember that illegal presets have to be enabled once their missing PS get installed
                for (Preset p : this.cache.getAllPresets()) {
                    if (!p.isAvailable()) {
                        p.forceRecache();
                        
                        // if the preset was only missing this RG, rollout the changes
                        if (p.isAvailable()) {
                            p.rollout();
                        }
                    }
                }
                
            } finally {
                IPCProvider.getInstance().endUpdate();
            }
            return true;
        } catch (XMLParserException xmlpe) {
            /* error during XML validation */
            Log.w(identifier + " has failed registration with PMP.", xmlpe);
            return false;
        }
    }
    
    
    @Override
    public boolean uninstallResourceGroup(String identifier) {
        checkCached();
        Assert.nonNull(identifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        
        ResourceGroup rg = this.cache.getResourceGroups().get(identifier);
        if (rg == null) {
            return false;
        } else {
            
            // TODO delete the class files / jar / etc
            
            rg.delete();
            this.cache.getResourceGroups().remove(identifier);
            
            IPCProvider.getInstance().startUpdate();
            try {
                // remember that service features have to be disabled once their required PS get uninstalled
                for (App app : this.cache.getApps().values()) {
                    boolean appChanged = false;
                    
                    for (ServiceFeature sf : this.cache.getServiceFeatures().get(app).values()) {
                        if (sf.isAvailable()) {
                            sf.forceRecache();
                            
                            // if the service feature will be missing this RG, rollout the changes
                            if (!sf.isAvailable()) {
                                appChanged = true;
                            }
                        }
                    }
                    
                    if (appChanged) {
                        app.verifyServiceFeatures();
                    }
                }
                
                // remember that presets have to be disabled once their required PS get uninstalled
                for (Preset preset : this.cache.getAllPresets()) {
                    if (preset.isAvailable() && !preset.isDeleted()) {
                        preset.forceRecache();
                        
                        // if the preset will be missing this RG, rollout the changes
                        if (!preset.isAvailable()) {
                            preset.rollout();
                        }
                    }
                }
            } finally {
                IPCProvider.getInstance().endUpdate();
            }
            
            return true;
        }
    }
    
    
    @Override
    public IPreset[] getPresets() {
        checkCached();
        return this.cache.getAllPresets().toArray(new IPreset[0]);
    }
    
    
    @Override
    public IPreset[] getPresets(ModelElement creator) {
        checkCached();
        Assert.isValidCreator(creator, new ModelMisuseError(Assert.ILLEGAL_CREATOR, "creator", creator));
        
        Map<String, Preset> creatorPresets = this.cache.getPresets().get(creator);
        if (creatorPresets == null) {
            return null;
        } else {
            return creatorPresets.values().toArray(new IPreset[0]);
        }
    }
    
    
    @Override
    public IPreset getPreset(IModelElement creator, String identifier) {
        checkCached();
        Assert.isValidCreator(creator, new ModelMisuseError(Assert.ILLEGAL_CREATOR, "creator", creator));
        Assert.nonNull(identifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        
        Map<String, Preset> creatorPresets = this.cache.getPresets().get(creator);
        if (creatorPresets == null) {
            return null;
        } else {
            return creatorPresets.get(identifier);
        }
    }
    
    
    @Override
    public IPreset addPreset(IModelElement creator, String identifier, String name, String description) {
        checkCached();
        Assert.isValidCreator(creator, new ModelMisuseError(Assert.ILLEGAL_CREATOR, "creator", creator));
        Assert.nonNull(identifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        Assert.nonNull(name, new ModelMisuseError(Assert.ILLEGAL_NULL, "name", name));
        Assert.nonNull(description, new ModelMisuseError(Assert.ILLEGAL_NULL, "description", description));
        
        Preset newPreset = new PresetPersistenceProvider(null)
                .createElementData(creator, identifier, name, description);
        Map<String, Preset> creatorMap = this.cache.getPresets().get(creator);
        if (creatorMap == null) {
            creatorMap = new HashMap<String, Preset>();
            this.cache.getPresets().put(creator, creatorMap);
        }
        creatorMap.put(identifier, newPreset);
        return newPreset;
    }
    
    
    @Override
    public IPreset addUserPreset(String name, String description) {
        checkCached();
        Assert.nonNull(name, new ModelMisuseError(Assert.ILLEGAL_NULL, "name", name));
        Assert.nonNull(description, new ModelMisuseError(Assert.ILLEGAL_NULL, "description", description));
        
        // prepare standard
        Map<String, Preset> creatorMap = this.cache.getPresets().get(null);
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
        checkCached();
        Assert.nonNull(identifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", identifier));
        Assert.isValidCreator(creator, new ModelMisuseError(Assert.ILLEGAL_CREATOR, "creator", creator));
        
        // does the creator map exist?
        Map<String, Preset> creatorMap = this.cache.getPresets().get(creator);
        if (creatorMap == null) {
            return false;
        } else {
            // does the preset exist?
            Preset p = creatorMap.get(identifier);
            
            if (p == null) {
                return false;
            } else {
                p.delete();
                
                // update model
                creatorMap.remove(identifier);
                
                IPCProvider.getInstance().startUpdate();
                try {
                    for (IApp app : p.getAssignedApps()) {
                        // this time, there's no way but to cast (or run manually through all apps)
                        Assert.instanceOf(app, App.class, new ModelIntegrityError(Assert.ILLEGAL_CLASS, "app", app));
                        App castApp = (App) app;
                        castApp.removePreset(p);
                    }
                } finally {
                    IPCProvider.getInstance().endUpdate();
                }
                
                return true;
            }
        }
    }
    
    
    @Override
    public void clearAll() {
        PersistenceProvider.getInstance().getDoh().cleanTables();
        PersistenceProvider.getInstance().releaseCache();
    }
    
}
