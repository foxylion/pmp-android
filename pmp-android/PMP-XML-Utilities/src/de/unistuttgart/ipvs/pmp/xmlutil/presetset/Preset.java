/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-XML-UTILITIES
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class Preset extends IssueLocation implements Serializable, IPreset {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 1219577103419085395L;
    
    /**
     * The identifier
     */
    private String identifier = "";
    
    /**
     * The creator
     */
    private String creator = "";
    
    /**
     * The name
     */
    private String name = "";
    
    /**
     * The description
     */
    private String description = "";
    
    /**
     * The assigned apps
     */
    private List<IPresetAssignedApp> assignedApps = new ArrayList<IPresetAssignedApp>();
    
    /**
     * The assigned privacy settings
     */
    private List<IPresetAssignedPrivacySetting> assignedPrivacySettings = new ArrayList<IPresetAssignedPrivacySetting>();
    
    
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
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#setIdentifier(java.lang.String)
     */
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#getCreator()
     */
    @Override
    public String getCreator() {
        return this.creator;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#setCreator(java.lang.String)
     */
    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#getDescription()
     */
    @Override
    public String getDescription() {
        return this.description;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#getAssignedApps()
     */
    @Override
    public List<IPresetAssignedApp> getAssignedApps() {
        return this.assignedApps;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#addAssignedApp(de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedApp)
     */
    @Override
    public void addAssignedApp(IPresetAssignedApp assignedApp) {
        this.assignedApps.add(assignedApp);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#removeAssignedApp(de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedApp)
     */
    @Override
    public void removeAssignedApp(IPresetAssignedApp assignedApp) {
        this.assignedApps.remove(assignedApp);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#getAssignedPrivacySettings()
     */
    @Override
    public List<IPresetAssignedPrivacySetting> getAssignedPrivacySettings() {
        return this.assignedPrivacySettings;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#addAssignedPrivacySetting(de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedPrivacySetting)
     */
    @Override
    public void addAssignedPrivacySetting(IPresetAssignedPrivacySetting assignedPrivacySetting) {
        this.assignedPrivacySettings.add(assignedPrivacySetting);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset#removeAssignedPrivacySetting(de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedPrivacySetting)
     */
    @Override
    public void removeAssignedPrivacySetting(IPresetAssignedPrivacySetting assignedPrivacySetting) {
        this.assignedPrivacySettings.remove(assignedPrivacySetting);
    }
    
}
