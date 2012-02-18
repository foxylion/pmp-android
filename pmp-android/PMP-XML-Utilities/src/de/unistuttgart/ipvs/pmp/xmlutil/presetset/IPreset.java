package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

public interface IPreset extends IIssueLocation {
    
    /**
     * Get the identifier
     * 
     * @return the identifier
     */
    public abstract String getIdentifier();
    
    
    /**
     * Set the identifier
     * 
     * @param identifier
     *            the identifier to set
     */
    public abstract void setIdentifier(String identifier);
    
    
    /**
     * Get the creator
     * 
     * @return the creator
     */
    public abstract String getCreator();
    
    
    /**
     * Set the creator
     * 
     * @param creator
     *            the creator to set
     */
    public abstract void setCreator(String creator);
    
    
    /**
     * Get the name
     * 
     * @return the name
     */
    public abstract String getName();
    
    
    /**
     * Set the name
     * 
     * @param name
     *            the name to set
     */
    public abstract void setName(String name);
    
    
    /**
     * Get the description
     * 
     * @return the description
     */
    public abstract String getDescription();
    
    
    /**
     * Set the description
     * 
     * @param description
     *            the description to set
     */
    public abstract void setDescription(String description);
    
    
    /**
     * Get all assigned apps
     * 
     * @return assigned apps
     */
    public abstract List<IPresetAssignedApp> getAssignedApps();
    
    
    /**
     * Add an assigned app
     * 
     * @param assignedApp
     *            assigned app to add
     */
    public abstract void addAssignedApp(IPresetAssignedApp assignedApp);
    
    
    /**
     * Remove an assigned app
     * 
     * @param assignedApp
     *            assigned app to remove
     */
    public abstract void removeAssignedApp(IPresetAssignedApp assignedApp);
    
    
    /**
     * Get all assigned privacy settings
     * 
     * @return assigned privacy settings
     */
    public abstract List<IPresetAssignedPrivacySetting> getAssignedPrivacySettings();
    
    
    /**
     * Add an assigned privacy setting
     * 
     * @param assignedPrivacySetting
     *            assigned privacy setting to add
     */
    public abstract void addAssignedPrivacySetting(IPresetAssignedPrivacySetting assignedPrivacySetting);
    
    
    /**
     * Remove an assigned privacy setting
     * 
     * @param assignedPrivacySetting
     *            assigned privacy setting to remove
     */
    public abstract void removeAssignedPrivacySetting(IPresetAssignedPrivacySetting assignedPrivacySetting);
    
}
