package de.unistuttgart.ipvs.pmp.resource;

import android.content.Context;
import android.os.Bundle;

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
     */
    public Context getContext(String rgPackage);
    
    
    /**
     * Sends a transmission request to call {@link Resource#transmit(Bundle)} and publish the return value to all
     * apps listening on this resource.
     * 
     * @param rgPackage
     *            the package of the resource group
     * @param resource
     *            the resource to request a transmission for
     */
    public void requestTransmission(String rgPackage, String resource);
    
}
