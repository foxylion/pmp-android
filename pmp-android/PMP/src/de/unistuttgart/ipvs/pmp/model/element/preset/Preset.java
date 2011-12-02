package de.unistuttgart.ipvs.pmp.model.element.preset;

import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
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
    protected ModelElement creator;
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
    
    public Preset(ModelElement creator, String identifier) {
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
    public ModelElement getCreator() {
        return this.creator;
    }
    
    
    protected String getCreatorString() {
        if (getCreator() == null) {
            return PersistenceConstants.PACKAGE_SEPARATOR;
        } else {
            return getCreator().getIdentifier();
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
        this.description = description;
        persist();
    }
    
    
    @Override
    public IPrivacySetting[] getGrantedPrivacyLevels() {
        checkCached();
        return this.privacySettingValues.keySet().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public String getGrantedPrivacyLevelValue(IPrivacySetting privacySetting) {
        checkCached();
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
        return this.assignedApps.contains(app);
    }
    
    
    @Override
    public void assignApp(IApp app) {
        checkCached();
        
        ((PresetPersistenceProvider) this.persistenceProvider).assignApp(app);
        this.assignedApps.add(app);
        
        app.verifyServiceFeatures();
    }
    
    
    @Override
    public void removeApp(IApp app) {
        checkCached();
        
        ((PresetPersistenceProvider) this.persistenceProvider).removeApp(app);
        this.assignedApps.remove(app);
        
        app.verifyServiceFeatures();
    }
    
    
    @Override
    public void assignPrivacyLevel(IPrivacySetting privacySetting, String value) {
        checkCached();
        
        ((PresetPersistenceProvider) this.persistenceProvider).assignPrivacyLevel(privacySetting, value);
        this.privacySettingValues.put(privacySetting, value);
        
        rollout();
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacySetting privacySetting) {
        checkCached();
        
        ((PresetPersistenceProvider) this.persistenceProvider).removePrivacyLevel(privacySetting);
        this.privacySettingValues.remove(privacySetting);
        
        rollout();
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
        this.assignedApps.remove(a);
    }
    
}
