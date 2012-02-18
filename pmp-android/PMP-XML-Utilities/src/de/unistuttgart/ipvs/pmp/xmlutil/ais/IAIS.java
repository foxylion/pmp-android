package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS;

public interface IAIS extends IBasicIS {
    
    /**
     * Add a service feature to the app
     * 
     * @param sf
     *            service feature
     */
    public abstract void addServiceFeature(IAISServiceFeature sf);
    
    
    /**
     * Remove a service feature of the app
     * 
     * @param sf
     *            service feature
     */
    public abstract void removeServiceFeature(IAISServiceFeature sf);
    
    
    /**
     * Get the list which contains all service features
     * 
     * @return list with service features
     */
    public abstract List<IAISServiceFeature> getServiceFeatures();
    
    
    /**
     * Get a service feature for a given identifier. Null, if no service feature
     * exists for the given identifier.
     * 
     * @param identifier
     *            identifier of the service feature
     * @return service feature with given identifier, null if none exists.
     */
    public abstract IAISServiceFeature getServiceFeatureForIdentifier(String identifier);
    
    
    /**
     * Clear only issues referring to the app information
     */
    public abstract void clearAppInformationIssuesAndPropagate();
    
    
    /**
     * Clear only issues referring to the service features
     */
    public abstract void clearServiceFeaturesIssuesAndPropagate();
    
}
