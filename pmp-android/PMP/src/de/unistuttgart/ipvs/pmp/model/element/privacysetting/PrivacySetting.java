package de.unistuttgart.ipvs.pmp.model.element.privacysetting;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;

/**
 * @see IPrivacySetting
 * @author Tobias Kuhn
 * 
 */
public class PrivacySetting extends ModelElement implements IPrivacySetting {
    
    /**
     * identifying attributes
     */
    private ResourceGroup resourceGroup;
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    
    /**
     * internal data & links
     */
    
    /* organizational */
    
    public PrivacySetting(String identifier) {
        super(identifier);
    }
    
    
    /* interface */
    
    @Override
    public IResourceGroup getResourceGroup() {
        return this.resourceGroup;
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
    public String getLocalIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean isValueValid(String value) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean permits(String reference, String value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }
    
}
