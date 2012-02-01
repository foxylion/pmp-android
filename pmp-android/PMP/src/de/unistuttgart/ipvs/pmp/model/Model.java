package de.unistuttgart.ipvs.pmp.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import android.content.pm.PackageManager.NameNotFoundException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.api.ipc.IPCConnection;
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
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidXMLException;
import de.unistuttgart.ipvs.pmp.model.ipc.IPCProvider;
import de.unistuttgart.ipvs.pmp.model.plugin.PluginProvider;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;
import de.unistuttgart.ipvs.pmp.service.pmp.RegistrationResult;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSetParser;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

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
     * Security-mechanism against using the same dex class loader twice for the same dexs, producing a SIGBUS.
     * Do not persist this, a restart should clear the list since it fixes the issue.
     */
    private Set<String> unallowedInstall;
    
    /**
     * Singleton stuff
     */
    private static final Model instance = new Model();
    
    
    private Model() {
        this.cache = null;
        this.unallowedInstall = new HashSet<String>();
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
        Collection<App> result = this.cache.getApps().values();
        return result.toArray(new IApp[result.size()]);
    }
    
    
    @Override
    public IApp getApp(String appPackage) {
        checkCached();
        Assert.nonNull(appPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "appPackage", appPackage));
        
        return this.cache.getApps().get(appPackage);
    }
    
    
    @Override
    public RegistrationResult registerApp(final String appPackage) {
        checkCached();
        Assert.nonNull(appPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "appPackage", appPackage));
        Assert.isNull(getApp(appPackage), new ModelMisuseError(Assert.ILLEGAL_ALREADY_INSTALLED, "appPackage",
                appPackage));
        
        // check XML
        try {
            InputStream xmlStream = PMPApplication.getContext().getPackageManager()
                    .getResourcesForApplication(appPackage).getAssets().open(PersistenceConstants.APP_XML_NAME);
            
            AppInformationSet ais = AppInformationSetParser.createAppInformationSet(xmlStream);
            
            // check service availability
            IPCConnection ipcc = new IPCConnection(PMPApplication.getContext());
            try {
                ipcc.setDestinationService(appPackage);
                if (ipcc.getBinder() == null) {
                    /* error during connecting to service */
                    Log.w(appPackage + " has failed registration with PMP.");
                    return new RegistrationResult(false, "Service not available.");
                }
            } finally {
                ipcc.disconnect();
            }
            
            // apply new app to DB, then model
            App newApp = new AppPersistenceProvider(null).createElementData(appPackage);
            Assert.nonNull(newApp, new ModelIntegrityError(Assert.ILLEGAL_NULL, "newApp", newApp));
            this.cache.getApps().put(appPackage, newApp);
            this.cache.getServiceFeatures().put(newApp, new HashMap<String, ServiceFeature>());
            
            // apply new SF to DB, then model
            for (String sfIdentifier : ais.getServiceFeaturesMap().keySet()) {
                ServiceFeature newSF = new ServiceFeaturePersistenceProvider(null).createElementData(newApp,
                        sfIdentifier, ais.getServiceFeaturesMap().get(sfIdentifier).getRequiredResourceGroups());
                Assert.nonNull(newSF, new ModelIntegrityError(Assert.ILLEGAL_NULL, "newSF", newSF));
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
            Log.d(appPackage + " has successfully registered.");
            return new RegistrationResult(true);
            
        } catch (final IOException ioe) {
            /* error during finding files */
            Log.w(appPackage + " has failed registration with PMP.", ioe);
            return new RegistrationResult(false, ioe.getMessage());
            
        } catch (final NameNotFoundException nnfe) {
            /* error during finding files */
            Log.w(appPackage + " has failed registration with PMP.", nnfe);
            return new RegistrationResult(false, nnfe.getMessage());
            
        } catch (final XMLParserException xmlpe) {
            /* error during XML validation */
            Log.w(appPackage + " has failed registration with PMP.", xmlpe);
            return new RegistrationResult(false, xmlpe.getDetails());
        }
    }
    
    
    @Override
    public boolean unregisterApp(String appPackage) {
        checkCached();
        Assert.nonNull(appPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "appPackage", appPackage));
        
        App app = this.cache.getApps().get(appPackage);
        if (app == null) {
            return false;
        } else {
            
            app.delete();
            this.cache.getApps().remove(appPackage);
            
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
        Collection<ResourceGroup> result = this.cache.getResourceGroups().values();
        return result.toArray(new IResourceGroup[result.size()]);
    }
    
    
    @Override
    public IResourceGroup getResourceGroup(String rgPackage) {
        checkCached();
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        return this.cache.getResourceGroups().get(rgPackage);
    }
    
    
    @Override
    public boolean installResourceGroup(String rgPackage, boolean dontDownload) throws InvalidXMLException,
            InvalidPluginException {
        checkCached();
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        Assert.isNull(getResourceGroup(rgPackage), new ModelMisuseError(Assert.ILLEGAL_ALREADY_INSTALLED, "rgPackage",
                rgPackage));
        if (this.unallowedInstall.contains(rgPackage)) {
            throw new ModelMisuseError(Assert.ILLEGAL_SIGBUS_INSTALL, "rgPackage", rgPackage);
        }
        
        try {
            
            if (!dontDownload) {
                // download the plugin
                File temp = ServerProvider.getInstance().downloadResourceGroup(rgPackage);
                Assert.nonNull(temp, new ModelMisuseError(Assert.ILLEGAL_PACKAGE, "rgPackage", rgPackage));
                
                // add it
                FileInputStream fis;
                try {
                    fis = new FileInputStream(temp);
                    try {
                        PluginProvider.getInstance().injectFile(rgPackage, fis);
                    } finally {
                        try {
                            fis.close();
                        } catch (IOException ioe) {
                            Log.e("IO exception during install RG", ioe);
                        }
                    }
                } catch (FileNotFoundException fnfe) {
                    throw new ModelIntegrityError(Assert.ILLEGAL_MISSING_FILE, rgPackage, rgPackage);
                }
                
                // remove temporary file
                if (!temp.delete()) {
                    Log.e("Could not delete temporary file: " + temp.getAbsolutePath());
                }
            }
            
            // install the plugin
            PluginProvider.getInstance().install(rgPackage);
            
            // get the RGIS
            RgInformationSet rgis = PluginProvider.getInstance().getRGIS(rgPackage);
            
            // check it is valid
            de.unistuttgart.ipvs.pmp.resource.ResourceGroup rg = PluginProvider.getInstance().getResourceGroupObject(
                    rgPackage);
            if (!rgPackage.equals(rgis.getIdentifier())) {
                throw new InvalidXMLException("ResourceGroup package (parameter, XML)", rgPackage, rgis.getIdentifier());
            }
            if (!rgis.getIdentifier().equals(rg.getRgPackage())) {
                throw new InvalidXMLException("ResourceGroup package (XML, object)", rgis.getIdentifier(),
                        rg.getRgPackage());
            }
            for (String psIdentifier : rgis.getPrivacySettingsMap().keySet()) {
                AbstractPrivacySetting<?> aps = rg.getPrivacySetting(psIdentifier);
                if (aps == null) {
                    throw new InvalidXMLException("PrivacySetting (XML, objects)", psIdentifier, psIdentifier);
                }
            }
            
            // apply new RG to DB, then model
            ResourceGroup newRG = new ResourceGroupPersistenceProvider(null).createElementData(rgPackage);
            Assert.nonNull(newRG, new ModelIntegrityError(Assert.ILLEGAL_NULL, "newRG", newRG));
            this.cache.getResourceGroups().put(rgPackage, newRG);
            this.cache.getPrivacySettings().put(newRG, new HashMap<String, PrivacySetting>());
            
            // apply new PS to DB, then model
            for (String psIdentifier : rgis.getPrivacySettingsMap().keySet()) {
                PrivacySetting newPS = new PrivacySettingPersistenceProvider(null).createElementData(newRG,
                        psIdentifier);
                Assert.nonNull(newPS, new ModelIntegrityError(Assert.ILLEGAL_NULL, "newPS", newPS));
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
            Log.w(rgPackage + " has failed installation with PMP.", xmlpe);
            return false;
        }
    }
    
    
    @Override
    public boolean uninstallResourceGroup(String rgPackage) {
        checkCached();
        Assert.nonNull(rgPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "rgPackage", rgPackage));
        
        ResourceGroup rg = this.cache.getResourceGroups().get(rgPackage);
        if (rg == null) {
            return false;
        } else {
            
            rg.delete();
            // delete the class files / apk / etc
            PluginProvider.getInstance().uninstall(rgPackage);
            this.unallowedInstall.add(rgPackage);
            this.cache.getResourceGroups().remove(rgPackage);
            
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
        Collection<Preset> result = this.cache.getAllPresets();
        return result.toArray(new IPreset[result.size()]);
    }
    
    
    @Override
    public IPreset[] getPresets(ModelElement creator) {
        checkCached();
        Assert.isValidCreator(creator, new ModelMisuseError(Assert.ILLEGAL_CREATOR, "creator", creator));
        
        Map<String, Preset> creatorPresets = this.cache.getPresets().get(creator);
        if (creatorPresets == null) {
            return new IPreset[0];
        } else {
            return creatorPresets.values().toArray(new IPreset[creatorPresets.values().size()]);
        }
    }
    
    
    @Override
    public IPreset getPreset(IModelElement creator, String presetIdentifier) {
        checkCached();
        Assert.isValidCreator(creator, new ModelMisuseError(Assert.ILLEGAL_CREATOR, "creator", creator));
        Assert.nonNull(presetIdentifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", presetIdentifier));
        
        Map<String, Preset> creatorPresets = this.cache.getPresets().get(creator);
        if (creatorPresets == null) {
            return null;
        } else {
            return creatorPresets.get(presetIdentifier);
        }
    }
    
    
    @Override
    public IPreset addPreset(IModelElement creator, String presetIdentifier, String name, String description) {
        checkCached();
        Assert.isValidCreator(creator, new ModelMisuseError(Assert.ILLEGAL_CREATOR, "creator", creator));
        Assert.nonNull(presetIdentifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", presetIdentifier));
        Assert.nonNull(name, new ModelMisuseError(Assert.ILLEGAL_NULL, "name", name));
        Assert.nonNull(description, new ModelMisuseError(Assert.ILLEGAL_NULL, "description", description));
        Assert.isNull(getPreset(creator, presetIdentifier), new ModelMisuseError(Assert.ILLEGAL_ALREADY_INSTALLED,
                "preset", (creator == null ? "null" : creator.toString()) + ", " + presetIdentifier));
        
        Preset newPreset = new PresetPersistenceProvider(null).createElementData(creator, presetIdentifier, name,
                description);
        Assert.nonNull(newPreset, new ModelIntegrityError(Assert.ILLEGAL_NULL, "newPreset", newPreset));
        
        Map<String, Preset> creatorMap = this.cache.getPresets().get(creator);
        if (creatorMap == null) {
            creatorMap = new HashMap<String, Preset>();
            this.cache.getPresets().put(creator, creatorMap);
        }
        creatorMap.put(presetIdentifier, newPreset);
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
        if (creatorMap != null) {
            while (creatorMap.get(identifier) != null) {
                suffix++;
                identifier = name + suffix;
            }
        }
        
        // create
        return addPreset(null, identifier, name, description);
    }
    
    
    @Override
    public boolean removePreset(IModelElement creator, String presetIdentifier) {
        checkCached();
        Assert.nonNull(presetIdentifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "identifier", presetIdentifier));
        Assert.isValidCreator(creator, new ModelMisuseError(Assert.ILLEGAL_CREATOR, "creator", creator));
        
        // does the creator map exist?
        Map<String, Preset> creatorMap = this.cache.getPresets().get(creator);
        if (creatorMap == null) {
            return false;
        } else {
            // does the preset exist?
            Preset p = creatorMap.get(presetIdentifier);
            
            if (p == null) {
                return false;
            } else {
                p.delete();
                
                // update model
                creatorMap.remove(presetIdentifier);
                
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
