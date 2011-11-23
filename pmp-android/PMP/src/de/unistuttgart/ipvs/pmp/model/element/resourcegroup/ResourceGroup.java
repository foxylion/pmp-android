package de.unistuttgart.ipvs.pmp.model.element.resourcegroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;

/**
 * @see IResourceGroup
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroup extends ModelElement implements IResourceGroup {
    
    /**
     * identifying attributes
     */
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    /**
     * internal data & links
     */
    protected Map<String, PrivacySetting> privacySettings;
    
    
    /* organizational */
    
    public ResourceGroup(String identifier) {
        super(identifier);
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
    public IPrivacySetting[] getPrivacySettings() {
        checkCached();
        return this.privacySettings.values().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public IPrivacySetting getPrivacySetting(String privacyLevelIdentifier) {
        return this.privacySettings.get(privacyLevelIdentifier);
    }
    
}
