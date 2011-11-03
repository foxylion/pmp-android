package de.unistuttgart.ipvs.pmp.model2.element.preset;

import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model2.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model2.element.app.App;
import de.unistuttgart.ipvs.pmp.model2.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.PrivacyLevel;

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
    protected Map<PrivacyLevel, String> privacyLevelValues;
    protected List<App> assignedApps;
    
    
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
        // TODO
    }
    
    
    @Override
    @Deprecated
    public void assignApp(IApp app, boolean hidden) {
        startUpdate();
        assignApp(app);
        endUpdate();
    }
    
    
    @Override
    public void removeApp(IApp app) {
        // TODO
    }
    
    
    @Override
    @Deprecated
    public void removeApp(IApp app, boolean hidden) {
        startUpdate();
        removeApp(app);
        endUpdate();
    }
    
    
    @Override
    public void addPrivacyLevel(IPrivacyLevel privacyLevel) {
        // TODO
    }
    
    
    @Override
    @Deprecated
    public void addPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        startUpdate();
        addPrivacyLevel(privacyLevel);
        endUpdate();
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel) {
        // TODO
    }
    
    
    @Override
    @Deprecated
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        startUpdate();
        removePrivacyLevel(privacyLevel);
        endUpdate();
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
