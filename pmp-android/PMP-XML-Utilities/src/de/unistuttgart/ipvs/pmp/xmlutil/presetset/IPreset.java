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

import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IPreset extends IIssueLocation, IIdentifierIS {
    
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
     * Get all {@link IPresetAssignedApp}s
     * 
     * @return {@link IPresetAssignedApp}s
     */
    public abstract List<IPresetAssignedApp> getAssignedApps();
    
    
    /**
     * Add an {@link IPresetAssignedApp}
     * 
     * @param assignedApp
     *            {@link IPresetAssignedApp} to add
     */
    public abstract void addAssignedApp(IPresetAssignedApp assignedApp);
    
    
    /**
     * Remove an {@link IPresetAssignedApp}
     * 
     * @param assignedApp
     *            {@link IPresetAssignedApp} to remove
     */
    public abstract void removeAssignedApp(IPresetAssignedApp assignedApp);
    
    
    /**
     * Get all {@link IPresetAssignedPrivacySetting}s
     * 
     * @return assigned {@link IPresetAssignedPrivacySetting}s
     */
    public abstract List<IPresetAssignedPrivacySetting> getAssignedPrivacySettings();
    
    
    /**
     * Add an {@link IPresetAssignedPrivacySetting}
     * 
     * @param assignedPrivacySetting
     *            {@link IPresetAssignedPrivacySetting} to add
     */
    public abstract void addAssignedPrivacySetting(IPresetAssignedPrivacySetting assignedPrivacySetting);
    
    
    /**
     * Remove an {@link IPresetAssignedPrivacySetting}
     * 
     * @param assignedPrivacySetting
     *            {@link IPresetAssignedPrivacySetting} to remove
     */
    public abstract void removeAssignedPrivacySetting(IPresetAssignedPrivacySetting assignedPrivacySetting);
    
}
