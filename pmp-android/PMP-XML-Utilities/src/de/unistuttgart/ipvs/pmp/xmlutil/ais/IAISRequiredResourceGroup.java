package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IIdentifierIS;

public interface IAISRequiredResourceGroup extends IIdentifierIS {
    
    /**
     * Get the min revision
     * 
     * @return the min revision
     */
    public abstract String getMinRevision();
    
    
    /**
     * Set the min revision
     * 
     * @param minRevision
     *            min revision to set
     */
    public abstract void setMinRevision(String minRevision);
    
    
    /**
     * Get all privacy settings of the required resource group
     * 
     * @return list of privacy settings
     */
    public abstract List<IAISRequiredPrivacySetting> getRequiredPrivacySettings();
    
    
    /**
     * Add a privacy setting to the required resource group
     * 
     * @param privacySetting
     *            privacySetting to add
     */
    public abstract void addRequiredPrivacySetting(IAISRequiredPrivacySetting privacySetting);
    
    
    /**
     * Remove a privacy setting from the required resource group
     * 
     * @param privacySetting
     *            privacySetting to remove
     */
    public abstract void removeRequiredPrivacySetting(IAISRequiredPrivacySetting privacySetting);
    
    
    /**
     * Get a required privacy setting for a given identifier. Null, if no
     * required privacy setting exists for the given identifier.
     * 
     * @param identifier
     *            identifier of the required privacy setting
     * @return required privacy setting with given identifier, null if none
     *         exists.
     */
    public abstract IAISRequiredPrivacySetting getRequiredPrivacySettingForIdentifier(String identifier);
    
}
