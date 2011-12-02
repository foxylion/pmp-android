package de.unistuttgart.ipvs.pmp.resource;

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
    
}
