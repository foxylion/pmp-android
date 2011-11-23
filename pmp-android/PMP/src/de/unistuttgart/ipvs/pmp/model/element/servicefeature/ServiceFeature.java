package de.unistuttgart.ipvs.pmp.model.element.servicefeature;

import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;

/**
 * @see IServiceFeature
 * @author Tobias Kuhn
 * 
 */
public class ServiceFeature extends ModelElement implements IServiceFeature {
    
    /**
     * identifying attributes
     */
    protected IApp app;
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    /**
     * internal data & links
     */
    protected Map<PrivacySetting, String> privacyLevelValues;
    protected boolean available;

    
    
    /* organizational */
    
    public ServiceFeature(String identifier) {
        super(identifier);
    }
    
    
    /* interface */
    
    
    @Override
    public IApp getApp() {
        return this.app;
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
    public IPrivacySetting[] getRequiredPrivacyLevels() {
        checkCached();
        return this.privacyLevelValues.keySet().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public String getRequiredPrivacyLevelValue(IPrivacySetting privacySetting) {
        return this.privacyLevelValues.get(privacySetting);
    }
    
    
    @Override
    public boolean isAvailable() {
        checkCached();
        return this.available;
    }


    @Override
    public String getLocalIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
