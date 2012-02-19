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
public interface IPresetAssignedPrivacySetting extends IIssueLocation {
    
    /**
     * Get the resource group identifier
     * 
     * @return the rgIdentifier
     */
    public abstract String getRgIdentifier();
    
    
    /**
     * Set the resource group identifier
     * 
     * @param rgIdentifier
     *            the rgIdentifier to set
     */
    public abstract void setRgIdentifier(String rgIdentifier);
    
    
    /**
     * Get the resource group revision
     * 
     * @return the rgRevision
     */
    public abstract String getRgRevision();
    
    
    /**
     * Set the resource group revision
     * 
     * @param rgRevision
     *            the rgRevision to set
     */
    public abstract void setRgRevision(String rgRevision);
    
    
    /**
     * Get the privacy setting identifier
     * 
     * @return the psIdentifier
     */
    public abstract String getPsIdentifier();
    
    
    /**
     * Set the privacy setting identifier
     * 
     * @param psIdentifier
     *            the psIdentifier to set
     */
    public abstract void setPsIdentifier(String psIdentifier);
    
    
    /**
     * Get the value
     * 
     * @return the value
     */
    public abstract String getValue();
    
    
    /**
     * Set the value
     * 
     * @param value
     *            the value to set
     */
    public abstract void setValue(String value);
    
    
    /**
     * Get the list of contexts
     * 
     * @return the contexts
     */
    public abstract List<IPresetPSContext> getContexts();
    
    
    /**
     * Add a context
     * 
     * @param context
     *            Context to add
     */
    public abstract void addContext(IPresetPSContext context);
    
    
    /**
     * Remove a context
     * 
     * @param context
     *            Context to remove
     */
    public abstract void removeContext(IPresetPSContext context);
    
}
