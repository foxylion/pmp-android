package de.unistuttgart.ipvs.pmp.model;

/**
 * A class that stores all the persistence strings as constants. Turned out to be an interface, so you can easily add it
 * inside a {@link PersistenceProvider} class via inheritance.
 * 
 * Does not yet contain contexts.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface PersistenceConstants {
    
    /**
     * table storing all the apps
     */
    static final String TBL_APP = "App";
    
    /**
     * table storing all the service features
     */
    static final String TBL_SERVICEFEATURE = "ServiceFeature";
    
    /**
     * table storing all the resource groups
     */
    static final String TBL_RESOURCEGROUP = "ResourceGroup";
    
    /**
     * table storing all the privacy settings
     */
    static final String TBL_PRIVACYSETTING = "PrivacySetting";
    
    /**
     * table storing all the presets
     */
    static final String TBL_PRESET = "Preset";
    
    /**
     * table storing the required privacy setting values for a service feature
     */
    static final String TBL_SFReqPSValue = "ServiceFeature_RequiredPrivacySettingValue";
    
    /**
     * table storing the granted privacy setting values for a preset
     */
    static final String TBL_GrantPSValue = "Preset_GrantedPrivacySettingValue";
    
    /**
     * table storing the assigned apps for a preset
     */
    static final String TBL_PresetAssignedApp = "Preset_AssignedApp";
    
    /*
     * identifying columns
     */
    static final String PACKAGE = "Package";
    static final String IDENTIFIER = "Identifier";
    static final String CREATOR = "Creator";
    
    /*
     * referencing, identifying columns
     */
    static final String APP_PACKAGE = TBL_APP + PACKAGE;
    static final String RESOURCEGROUP_PACKAGE = TBL_RESOURCEGROUP + PACKAGE;
    static final String PRIVACYSETTING_RESOURCEGROUP_PACKAGE = TBL_PRIVACYSETTING + TBL_RESOURCEGROUP + PACKAGE;
    static final String PRIVACYSETTING_IDENTIFIER = TBL_PRIVACYSETTING + IDENTIFIER;
    static final String SERVICEFEATURE_APP_PACKAGE = TBL_SERVICEFEATURE + TBL_APP + PACKAGE;
    static final String SERVICEFEATURE_IDENTIFIER = TBL_SERVICEFEATURE + IDENTIFIER;
    static final String PRESET_CREATOR = TBL_PRESET + CREATOR;
    static final String PRESET_IDENTIFIER = TBL_PRESET + IDENTIFIER;
    
    /* 
     * data columns
     */
    static final String GRANTEDVALUE = "GrantedValue";
    static final String REQUIREDVALUE = "RequiredValue";
    static final String NAME = "Name";
    static final String DESCRIPTION = "Description";
    static final String DELETED = "Deleted";
    
    /*
     * meta data constants
     */
    
    /**
     * constant that should never appear in package names.
     */
    static final String PACKAGE_SEPARATOR = "$";
}
