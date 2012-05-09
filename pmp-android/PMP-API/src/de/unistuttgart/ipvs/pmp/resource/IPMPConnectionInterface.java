package de.unistuttgart.ipvs.pmp.resource;

import android.content.Context;
import android.test.mock.MockContext;

/**
 * Interface for communication from the {@link ResourceGroup} plugin to the PMP model.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IPMPConnectionInterface {
    
    /**
     * Must first ask the PMP model for the privacy setting identified by rgPackage and psIdentifier, then find out what
     * this privacy setting's value is for appPackage.
     * 
     * @param rgPackage
     * @param psIdentifier
     * @param appPackage
     * @return the value of the privacy setting identified for appPackage, or null, if it is not set or was not found
     */
    public String getPrivacySettingValue(String rgPackage, String psIdentifier, String appPackage);
    
    
    /**
     * Ability to get a context for a resource group.
     * 
     * @param rgPackage
     * @return an Android context
     * @deprecated Results are undefined. Use {@link #getContext(String, String)} instead.
     */
    @Deprecated
    public Context getContext(String rgPackage);
    
    
    /**
     * Ability to get a context for a resource group for a specific app.
     * 
     * @param rgPackage
     * @param appPackage
     * @return an Android {@link Context}, if the operation was allowed, a {@link MockContext} otherwise
     */
    public Context getContext(String rgPackage, String appPackage);
    
}
