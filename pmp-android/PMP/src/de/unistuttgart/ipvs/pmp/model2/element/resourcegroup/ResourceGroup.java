package de.unistuttgart.ipvs.pmp.model2.element.resourcegroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model2.Model;
import de.unistuttgart.ipvs.pmp.model2.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model2.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.PrivacyLevel;

/**
 * @see IResourceGroup
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroup extends ModelElement implements IResourceGroup {
    
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
    protected Map<String, PrivacyLevel> privacyLevels;
    
    
    /* organizational */
    
    public ResourceGroup(String identifier) {
        this.identifier = identifier;
    }
    
    
    @Override
    public boolean equals(Object o) {
        try {
            ResourceGroup rg = (ResourceGroup) o;
            return getIdentifier().equals(rg.getIdentifier());
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
    public IPrivacyLevel[] getPrivacyLevels() {
        checkCached();
        return this.privacyLevels.values().toArray(new IPrivacyLevel[0]);
    }
    
    
    @Override
    public IPrivacyLevel getPrivacyLevel(String privacyLevelIdentifier) {
        return this.privacyLevels.get(privacyLevelIdentifier);
    }
    
    
    @Override
    @Deprecated
    /* do not ask a RG this. instead ask the model. */
    public IApp[] getAllAppsUsingThisResourceGroup() {
        // TODO Auto-generated method stub
        
        // implementation for performance test
        List<IApp> result = new ArrayList<IApp>();
        
        for (IApp app : Model.getInstance().getApps()) {
            if (app.getAllPrivacyLevelsUsedByActiveServiceLevel(this).length > 0) {
                result.add(app);
            }
        }
        
        return result.toArray(new IApp[0]);
    }
    
}
