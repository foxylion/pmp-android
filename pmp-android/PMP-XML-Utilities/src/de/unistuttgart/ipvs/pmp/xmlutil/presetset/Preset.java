package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class Preset implements Serializable {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 1219577103419085395L;
    
    /**
     * The identifier
     */
    private String identifier;
    
    /**
     * The creator
     */
    private String creator;
    
    /**
     * The name
     */
    private String name;
    
    /**
     * The description
     */
    private String description;
    
    /**
     * The assigned apps
     */
    private List<PresetAssignedApp> assignedApps = new ArrayList<PresetAssignedApp>();
    
    /**
     * The assigned privacy settings
     */
    private List<PresetAssignedPrivacySetting> assignedPrivacySettings = new ArrayList<PresetAssignedPrivacySetting>();
    
    
    /**
     * Constructor to set the attributes
     * 
     * @param identifier
     *            Identifier
     * @param creator
     *            Creator
     * @param name
     *            Name
     * @param description
     *            Description
     */
    public Preset(String identifier, String creator, String name, String description) {
        this.identifier = identifier;
        this.creator = creator;
        this.name = name;
        this.description = description;
    }
    
    
    /**
     * Get the identifier
     * 
     * @return the identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /**
     * Set the identifier
     * 
     * @param identifier
     *            the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    
    /**
     * Get the creator
     * 
     * @return the creator
     */
    public String getCreator() {
        return this.creator;
    }
    
    
    /**
     * Set the creator
     * 
     * @param creator
     *            the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    
    /**
     * Get the name
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
    
    /**
     * Set the name
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
    /**
     * Get the description
     * 
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }
    
    
    /**
     * Set the description
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    /**
     * Get all assigned apps
     * 
     * @return assigned apps
     */
    public List<PresetAssignedApp> getAssignedApps() {
        return assignedApps;
    }
    
    
    /**
     * Add an assigned app
     * 
     * @param assignedApp
     *            assigned app to add
     */
    public void addAssignedApp(PresetAssignedApp assignedApp) {
        assignedApps.add(assignedApp);
    }
    
    
    /**
     * Remove an assigned app
     * 
     * @param assignedApp
     *            assigned app to remove
     */
    public void removeAssignedApp(PresetAssignedApp assignedApp) {
        assignedApps.remove(assignedApp);
    }
    
    
    /**
     * Get all assigned privacy settings
     * 
     * @return assigned privacy settings
     */
    public List<PresetAssignedPrivacySetting> getAssignedPrivacySettings() {
        return assignedPrivacySettings;
    }
    
    
    /**
     * Add an assigned privacy setting
     * 
     * @param assignedPrivacySetting
     *            assigned privacy setting to add
     */
    public void addAssignedPrivacySetting(PresetAssignedPrivacySetting assignedPrivacySetting) {
        assignedPrivacySettings.add(assignedPrivacySetting);
    }
    
    
    /**
     * Remove an assigned privacy setting
     * 
     * @param assignedPrivacySetting
     *            assigned privacy setting to remove
     */
    public void removeAssignedPrivacySetting(PresetAssignedPrivacySetting assignedPrivacySetting) {
        assignedPrivacySettings.remove(assignedPrivacySetting);
    }
    
}
