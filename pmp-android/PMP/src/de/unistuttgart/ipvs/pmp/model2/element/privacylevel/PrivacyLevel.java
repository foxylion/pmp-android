package de.unistuttgart.ipvs.pmp.model2.element.privacylevel;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model2.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model2.element.resourcegroup.ResourceGroup;

/**
 * @see IPrivacyLevel
 * @author Tobias Kuhn
 * 
 */
public class PrivacyLevel extends ModelElement implements IPrivacyLevel {
    
    /**
     * identifying attributes
     */
    private String identifier;
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
    
    public PrivacyLevel(String identifier, ResourceGroup resourceGroup) {
        this.identifier = identifier;
        this.resourceGroup = resourceGroup;
    }
    
    
    @Override
    public boolean equals(Object o) {
        try {
            PrivacyLevel pl = (PrivacyLevel) o;
            return getUniqueIdentifier().equals(pl.getUniqueIdentifier());
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
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public IResourceGroup getResourceGroup() {
        return this.resourceGroup;
    }
    
    
    @Override
    public String getUniqueIdentifier() {
        return this.resourceGroup.getIdentifier() + ":" + getIdentifier();
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
    @Deprecated
    public String getValue() {
        /*
        checkCached();
        return this.value;*/
        // i don't think this belongs here.
        return null;
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
        // TODO IPC here
        return null;
    }
    
    
    @Override
    public boolean permits(String reference, String value) throws RemoteException {
        // TODO IPC here
        return false;
    }
    
}
