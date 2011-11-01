package de.unistuttgart.ipvs.pmp.model2.element.servicelevel;

import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;
import de.unistuttgart.ipvs.pmp.model2.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model2.element.app.App;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.PrivacyLevel;

/**
 * @see IServiceLevel
 * @author Tobias Kuhn
 * 
 */
public class ServiceLevel extends ModelElement implements IServiceLevel {
    
    /**
     * identifying attributes
     */
    private App app;
    private int level;
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    /**
     * internal data & links
     */
    protected Map<PrivacyLevel, String> privacyLevelValues;
    protected boolean available;
    
    
    /* organizational */
    
    public ServiceLevel(int level, App app) {
        this.level = level;
        this.app = app;
    }
    
    
    @Override
    public boolean equals(Object o) {
        try {
            ServiceLevel sl = (ServiceLevel) o;
            return getUniqueIdentifier().equals(sl.getUniqueIdentifier());
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    
    @Override
    public int hashCode() {
        return getUniqueIdentifier().hashCode();
    }
    
    
    /* interface */
    
    @Override
    public String getUniqueIdentifier() {
        return this.app.getIdentifier() + ":" + getLevel();
    }
    
    
    @Override
    public IApp getApp() {
        return this.app;
    }
    
    
    @Override
    public int getLevel() {
        return this.level;
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
    public IPrivacyLevel[] getPrivacyLevels() {
        checkCached();
        return this.privacyLevelValues.keySet().toArray(new IPrivacyLevel[0]);
    }
    
    
    @Override
    public boolean isAvailable() {
        checkCached();
        return this.available;
    }
    
}
