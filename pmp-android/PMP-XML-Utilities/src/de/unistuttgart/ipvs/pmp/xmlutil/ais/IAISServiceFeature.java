package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IIdentifierIS;

public interface IAISServiceFeature extends IBasicIS, IIdentifierIS {
    
    /**
     * Get all required resource groups of the service feature
     * 
     * @return list with required resource groups
     */
    public abstract List<IAISRequiredResourceGroup> getRequiredResourceGroups();
    
    
    /**
     * Add a required resource group to the service feature
     * 
     * @param rrg
     *            the required Resourcegroup to add
     */
    public abstract void addRequiredResourceGroup(IAISRequiredResourceGroup rrg);
    
    
    /**
     * Remove a required resource group from the service feature
     * 
     * @param rrg
     *            required resource group to remove
     */
    public abstract void removeRequiredResourceGroup(IAISRequiredResourceGroup rrg);
    
    
    /**
     * Get a required resource group for a given identifier. Null, if no
     * required resource group exists for the given identifier.
     * 
     * @param identifier
     *            identifier of the required resource group
     * @return required resource group with given identifier, null if none
     *         exists.
     */
    public abstract IAISRequiredResourceGroup getRequiredResourceGroupForIdentifier(String identifier);
    
}
