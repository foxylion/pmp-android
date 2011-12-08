package de.unistuttgart.ipvs.pmp.model.element.preset;

import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.ipc.IPCProvider;

/**
 * @see IPreset
 * @author Tobias Kuhn
 * 
 */
public class Preset extends ModelElement implements IPreset {
    
    /**
     * identifying attributes
     */
    protected IModelElement creator;
    protected String localIdentifier;
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    /**
     * internal data & links
     */
    protected Map<IPrivacySetting, String> privacySettingValues;
    protected List<IApp> assignedApps;
    
    protected boolean containsUnknownElements;
    protected boolean deleted;
    
    
    /* organizational */
    
    public Preset(IModelElement creator, String identifier) {
        super(creator + PersistenceConstants.PACKAGE_SEPARATOR + identifier);
        this.creator = creator;
        this.localIdentifier = identifier;
    }
    
    
    /* interface */
    
    @Override
    public String getLocalIdentifier() {
        return this.localIdentifier;
    }
    
    
    @Override
    public boolean isBundled() {
        return this.creator == null;
    }
    
    
    @Override
    public IModelElement getCreator() {
        return this.creator;
    }
    
    
    protected String getCreatorString() {
        if (this.creator == null) {
            return PersistenceConstants.PACKAGE_SEPARATOR;
        } else {
            return this.creator.getIdentifier();
        }
    }
    
    
    @Override
    public String getName() {
        checkCached();
        return this.name;
    }
    
    
    @Override
    public void setName(String name) {
        checkCached();
        Assert.nonNull(name, new ModelMisuseError(Assert.ILLEGAL_NULL, "name", name));
        this.name = name;
        persist();
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        return this.description;
    }
    
    
    @Override
    public void setDescription(String description) {
        checkCached();
        Assert.nonNull(description, new ModelMisuseError(Assert.ILLEGAL_NULL, "description", description));
        this.description = description;
        persist();
    }
    
    
    @Override
    public IPrivacySetting[] getGrantedPrivacySettings() {
        checkCached();
        return this.privacySettingValues.keySet().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public String getGrantedPrivacySettingValue(IPrivacySetting privacySetting) {
        checkCached();
        Assert.nonNull(privacySetting, new ModelMisuseError(Assert.ILLEGAL_NULL, "privacySetting", privacySetting));
        return this.privacySettingValues.get(privacySetting);
    }
    
    
    @Override
    public IApp[] getAssignedApps() {
        checkCached();
        return this.assignedApps.toArray(new IApp[0]);
    }
    
    
    @Override
    public boolean isAppAssigned(IApp app) {
        checkCached();
        Assert.nonNull(app, new ModelMisuseError(Assert.ILLEGAL_NULL, "app", app));
        return this.assignedApps.contains(app);
    }
    
    
    @Override
    public void assignApp(IApp app) {
        checkCached();
        Assert.nonNull(app, new ModelMisuseError(Assert.ILLEGAL_NULL, "app", app));
        
        ((PresetPersistenceProvider) this.persistenceProvider).assignApp(app);
        this.assignedApps.add(app);
        
        app.verifyServiceFeatures();
    }
    
    
    @Override
    public void removeApp(IApp app) {
        checkCached();
        Assert.nonNull(app, new ModelMisuseError(Assert.ILLEGAL_NULL, "app", app));
        
        ((PresetPersistenceProvider) this.persistenceProvider).removeApp(app);
        this.assignedApps.remove(app);
        
        app.verifyServiceFeatures();
    }
    
    
    @Override
    public void assignPrivacySetting(IPrivacySetting privacySetting, String value) {
        checkCached();
        Assert.nonNull(privacySetting, new ModelMisuseError(Assert.ILLEGAL_NULL, "privacySetting", privacySetting));
        Assert.nonNull(value, new ModelMisuseError(Assert.ILLEGAL_NULL, "value", value));
        
        ((PresetPersistenceProvider) this.persistenceProvider).assignPrivacySetting(privacySetting, value);
        this.privacySettingValues.put(privacySetting, value);
        
        rollout();
    }
    
    
    @Override
    public void removePrivacySetting(IPrivacySetting privacySetting) {
        checkCached();
        Assert.nonNull(privacySetting, new ModelMisuseError(Assert.ILLEGAL_NULL, "privacySetting", privacySetting));
        
        ((PresetPersistenceProvider) this.persistenceProvider).removePrivacySetting(privacySetting);
        this.privacySettingValues.remove(privacySetting);
        
        rollout();
    }
    
    
    @Override
    public void assignServiceFeature(IServiceFeature serviceFeature) {
        checkCached();
        Assert.nonNull(serviceFeature, new ModelMisuseError(Assert.ILLEGAL_NULL, "serviceFeature", serviceFeature));
        
        startUpdate();
        try {
            for (IPrivacySetting ps : serviceFeature.getRequiredPrivacySettings()) {
                assignPrivacySetting(ps, serviceFeature.getRequiredPrivacySettingValue(ps));
            }
            
        } finally {
            endUpdate();
        }
    }
    
    
    @Override
    public boolean isAvailable() {
        checkCached();
        return !this.containsUnknownElements;
    }
    
    
    @Override
    public void startUpdate() {
        IPCProvider.getInstance().startUpdate();
    }
    
    
    @Override
    public void endUpdate() {
        IPCProvider.getInstance().endUpdate();
    }
    
    
    @Override
    public void setDeleted(boolean deleted) {
        checkCached();
        this.deleted = deleted;
        persist();
        forceRecache();
        rollout();
    }
    
    
    @Override
    public boolean isDeleted() {
        checkCached();
        return this.deleted;
    }
    
    
    /* inter-model communication */
    
    /**
     * Forces a rollout to all the affected apps. Useful when this preset changed its active state.
     */
    public void rollout() {
        for (IApp app : getAssignedApps()) {
            app.verifyServiceFeatures();
        }
    }
    
    
    /**
     * Removes the app when it gets deleted.
     * 
     * @param a
     */
    public void removeDeletedApp(App a) {
        Assert.nonNull(a, new ModelIntegrityError(Assert.ILLEGAL_NULL, "a", a));
        this.assignedApps.remove(a);
    }
    
}
