package de.unistuttgart.ipvs.pmp.model2.element.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model2.Model;
import de.unistuttgart.ipvs.pmp.model2.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model2.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model2.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model2.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model2.element.servicelevel.IServiceLevel;
import de.unistuttgart.ipvs.pmp.model2.element.servicelevel.ServiceLevel;

/**
 * @see IApp
 * @author Tobias Kuhn
 * 
 */
public class App extends ModelElement implements IApp {
    
    /**
     * identifying attributes
     */
    private String identifier;
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    /**
     * internal data & links
     */
    protected List<ServiceLevel> serviceLevels;
    protected int activeServiceLevel;
    protected List<Preset> assignedPresets;
    
    
    /* organizational */
    
    public App(String identifier) {
        this.identifier = identifier;
    }
    
    
    @Override
    public boolean equals(Object o) {
        try {
            App a = (App) o;
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
    public String getIdentifier() {
        return this.identifier;
    }
    
    
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
    public IServiceLevel[] getServiceLevels() {
        checkCached();
        return this.serviceLevels.toArray(new IServiceLevel[0]);
    }
    
    
    @Override
    public IServiceLevel getServiceLevel(int level) {
        checkCached();
        return this.serviceLevels.get(level);
    }
    
    
    @Override
    public IServiceLevel getActiveServiceLevel() {
        checkCached();
        return this.serviceLevels.get(this.activeServiceLevel);
    }
    
    
    @Override
    @Deprecated
    public boolean setActiveServiceLevelAsPreset(int level) {
        checkCached();
        
        if (!getActiveServiceLevel().isAvailable()) {
            return false;
        }
        
        for (IPreset preset : getAssignedPresets()) {
            preset.removeApp(this, true);
        }
        
        IPreset preset = Model.getInstance().addPreset("AutoServiceLevelPreset", "", PMPComponentType.APP,
                getIdentifier());
        preset.assignApp(this, true);
        
        IServiceLevel sl = getServiceLevel(level);
        for (IPrivacyLevel pl : sl.getPrivacyLevels()) {
            preset.assignPrivacyLevel(pl, getActiveServiceLevel().getRequiredPrivacyLevelValue(pl), true);
        }
        
        verifyServiceLevel();
        return true;
    }
    
    
    @Override
    public void verifyServiceLevel() {
        // TODO also here
        
    }
    
    
    @Override
    public IPreset[] getAssignedPresets() {
        return this.assignedPresets.toArray(new IPreset[0]);
    }
    
    
    @Override
    @Deprecated
    public IResourceGroup[] getAllResourceGroupsUsedByServiceLevels() {
        
        Set<IResourceGroup> result = new HashSet<IResourceGroup>();
        for (ServiceLevel sl : this.serviceLevels) {
            for (IPrivacyLevel pl : sl.getPrivacyLevels()) {
                result.add(pl.getResourceGroup());
            }
        }
        return result.toArray(new IResourceGroup[0]);
    }
    
    
    @Override
    @Deprecated
    public IPrivacyLevel[] getAllPrivacyLevelsUsedByActiveServiceLevel(IResourceGroup resourceGroup) {
        checkCached();
        
        List<IPrivacyLevel> result = new ArrayList<IPrivacyLevel>();
        for (IPrivacyLevel pl : getActiveServiceLevel().getPrivacyLevels()) {
            if (pl.getResourceGroup().equals(resourceGroup)) {
                result.add(pl);
            }
        }
        return result.toArray(new IPrivacyLevel[0]);
    }
    
}
