package de.unistuttgart.ipvs.pmp.service.pmp;

/**
 * The Service of PMP provided for an app.
 * 
 * @author Jakob Jarosch
 */
interface IPMPService {
    
    /**
     * A registered app can call this method to gain an arbitrary (but most likely the first initial) update on all
     * the service features. The {@link PMPService} will then call concurrently the app service's
     * {@link IAppServicePMP#setServiceLevel(Integer)}.
     * 
     * @param identifier
     *            the identifier for the app to receive the service feature update
     * @return true, if the service feature will be performed, false, if the app wasn't found
     */
    boolean getServiceFeatureUpdate(String identifier);
    
    
    /**
     * Method for registering a new app at PMP.
     * 
     * @param identifier
     *            the identifier for the app to register
     */
    void registerApp(String identifier);
    
    
    /**
     * 
     * @param identifier
     *            the identifier for the app to check registration for
     * @return true, if and only if the app with identifier is registered with PMP
     */
    boolean isRegistered(String identifier);
    
    
    /**
     * @param identifier
     *            the identifier for the app that requests the resource
     * @param resourceGroup
     *            the name of the RG
     * @param resource
     *            the name of the resource
     * @return the IBinder interface for the resource of the resourceGroup, or null, if an error happened
     *         (e.g. resource not found)
     */
    IBinder getRessource(String identifier, String resourceGroup, String resource);
}
