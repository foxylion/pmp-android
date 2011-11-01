package de.unistuttgart.ipvs.pmp.model2.element.preset;

import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model2.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model2.element.app.App;
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
    public IPrivacyLevel[] getUsedPrivacyLevels() {
        checkCached();
        // TODO Auto-generated method stub
        
        // performance test implementation        
        return this.privacyLevelValues.keySet().toArray(new IPrivacyLevel[0]);
    }
    
    
    @Override
    public IApp[] getAssignedApps() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean isAppAssigned(IApp app) {
        checkCached();
        // TODO Auto-generated method stub
        
        // performance test implementation
        return this.assignedApps.contains(app);
    }
    
    
    @Override
    public void assignApp(IApp app) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void assignApp(IApp app, boolean hidden) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void removeApp(IApp app) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void removeApp(IApp app, boolean hidden) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void addPrivacyLevel(IPrivacyLevel privacyLevel) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void addPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        // TODO Auto-generated method stub
        
    }
    
}
