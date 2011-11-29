package de.unistuttgart.ipvs.pmp.model.element.servicefeature;

import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
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
    protected String localIdentifier;
    
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
    
    public ServiceFeature(App app, String identifier) {
        super(app.getIdentifier() + PersistenceConstants.PACKAGE_SEPARATOR + identifier);
        this.app = app;
        this.localIdentifier = identifier;
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
        checkCached(); // TODO
        return false;
    }
    
    
    @Override
    public String getLocalIdentifier() {
        return this.localIdentifier;
    }
    
    
    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
