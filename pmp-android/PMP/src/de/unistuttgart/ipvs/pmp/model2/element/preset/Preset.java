package de.unistuttgart.ipvs.pmp.model2.element.preset;

import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model2.IPCProvider;
import de.unistuttgart.ipvs.pmp.model2.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model2.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.IPrivacyLevel;

/**
 * @see IPreset
 * @author Tobias Kuhn
 * 
 */
public class Preset extends ModelElement implements IPreset {
    
    /**
     * identifying attributes
     */
    private String identifier;
    protected PMPComponentType type;
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    /**
     * internal data & links
     */
    protected Map<IPrivacyLevel, String> privacyLevelValues;
    protected List<IApp> assignedApps;
    
    protected boolean available;
    
    
    /* organizational */
    
    public Preset(String identifier, PMPComponentType type) {
        this.identifier = identifier;
        this.type = type;
    }
    
    
    @Override
    public boolean equals(Object o) {
        try {
            Preset a = (Preset) o;
            return getIdentifier().equals(a.getIdentifier());
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    
    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        return this.name;
    }
    
    
    @Override
    public PMPComponentType getType() {
        return this.type;
    }
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        return this.description;
    }
    
    
    @Override
    public IPrivacyLevel[] getGrantedPrivacyLevels() {
        checkCached();
        return this.privacyLevelValues.keySet().toArray(new IPrivacyLevel[0]);
    }
    
    
    @Override
    public String getGrantedPrivacyLevelValue(IPrivacyLevel privacyLevel) {
        return this.privacyLevelValues.get(privacyLevel);
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
        
        ((PresetPersistenceProvider) persistenceProvider).assignApp(app);
        this.assignedApps.add(app);
        
        app.verifyServiceLevel();
    }
    
    
    @Override
    @Deprecated
    public void assignApp(IApp app, boolean hidden) {
        if (hidden) {
            IPCProvider.getInstance().startUpdate();
        }
        assignApp(app);
        if (!hidden) {
            IPCProvider.getInstance().endUpdate();
        }
    }
    
    
    @Override
    public void removeApp(IApp app) {
        checkCached();
        
        ((PresetPersistenceProvider) persistenceProvider).removeApp(app);
        this.assignedApps.remove(app);
        
        app.verifyServiceLevel();
    }
    
    
    @Override
    @Deprecated
    public void removeApp(IApp app, boolean hidden) {
        if (hidden) {
            IPCProvider.getInstance().startUpdate();
        }
        removeApp(app);
        if (!hidden) {
            IPCProvider.getInstance().endUpdate();
        }
    }
    
    
    @Override
    public void assignPrivacyLevel(IPrivacyLevel privacyLevel, String value) {
        checkCached();
        
        ((PresetPersistenceProvider) persistenceProvider).assignPrivacyLevel(privacyLevel, value);
        this.privacyLevelValues.put(privacyLevel, value);
        
        for (IApp app : getAssignedApps()) {
            app.verifyServiceLevel();
        }
    }
    
    
    @Override
    @Deprecated
    public void assignPrivacyLevel(IPrivacyLevel privacyLevel, String value, boolean hidden) {
        if (hidden) {
            IPCProvider.getInstance().startUpdate();
        }
        assignPrivacyLevel(privacyLevel, value);
        if (!hidden) {
            IPCProvider.getInstance().endUpdate();
        }
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel) {
        checkCached();
        
        ((PresetPersistenceProvider) persistenceProvider).removePrivacyLevel(privacyLevel);
        this.privacyLevelValues.remove(privacyLevel);
        
        for (IApp app : getAssignedApps()) {
            app.verifyServiceLevel();
        }
    }
    
    
    @Override
    @Deprecated
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        if (hidden) {
            IPCProvider.getInstance().startUpdate();
        }
        removePrivacyLevel(privacyLevel);
        if (!hidden) {
            IPCProvider.getInstance().endUpdate();
        }
    }
    
    
    @Override
    public boolean isAvailable() {
        return this.available;
    }
    
}
