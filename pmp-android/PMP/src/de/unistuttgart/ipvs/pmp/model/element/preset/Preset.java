package de.unistuttgart.ipvs.pmp.model.element.preset;

import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.IPCProvider;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

/**
 * @see IPreset
 * @author Tobias Kuhn
 * 
 */
public class Preset extends ModelElement implements IPreset {
    
    /**
     * identifying attributes
     */
    protected String creator;
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
    
    protected boolean available;
    
    
    /* organizational */
    
    public Preset(String creator, String identifier) {
        super(creator + PersistenceConstants.PACKAGE_SEPARATOR + identifier);
        this.creator = creator;
        this.localIdentifier = identifier;
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        return this.name;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        return this.description;
    }
    
    
    @Override
    public IPrivacySetting[] getGrantedPrivacyLevels() {
        checkCached();
        return this.privacySettingValues.keySet().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public String getGrantedPrivacyLevelValue(IPrivacySetting privacySetting) {
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
        
        ((PresetPersistenceProvider) persistenceProvider).assignApp(app);
        this.assignedApps.add(app);
        
        app.verifyServiceFeatures();
    }
    
    
    @Override
    public void removeApp(IApp app) {
        checkCached();
        
        ((PresetPersistenceProvider) persistenceProvider).removeApp(app);
        this.assignedApps.remove(app);
        
        app.verifyServiceFeatures();
    }
    
    
    @Override
    public void assignPrivacyLevel(IPrivacySetting privacySetting, String value) {
        checkCached();
        
        ((PresetPersistenceProvider) persistenceProvider).assignPrivacyLevel(privacySetting, value);
        this.privacySettingValues.put(privacySetting, value);
        
        for (IApp app : getAssignedApps()) {
            app.verifyServiceFeatures();
        }
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacySetting privacySetting) {
        checkCached();
        
        ((PresetPersistenceProvider) persistenceProvider).removePrivacyLevel(privacySetting);
        this.privacySettingValues.remove(privacySetting);
        
        for (IApp app : getAssignedApps()) {
            app.verifyServiceFeatures();
        }
    }
    
    
    @Override
    public boolean isAvailable() {
        return this.available;
    }
    
    
    @Override
    public boolean isBundled() {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public ModelElement getCreator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public void startUpdate() {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void endUpdate() {
        // TODO Auto-generated method stub
        
    }
    
}
