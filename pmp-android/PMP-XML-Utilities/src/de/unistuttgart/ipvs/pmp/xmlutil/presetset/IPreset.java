/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
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
