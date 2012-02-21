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

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IPresetPSContext extends IIssueLocation {
    
    /**
     * Get the type
     * 
     * @return the type
     */
    public abstract String getType();
    
    
    /**
     * Set the type
     * 
     * @param type
     *            the type to set
     */
    public abstract void setType(String type);
    
    
    /**
     * Get the condition
     * 
     * @return the condition
     */
    public abstract String getCondition();
    
    
    /**
     * Set the condition
     * 
     * @param condition
     *            the condition to set
     */
    public abstract void setCondition(String condition);
    
    
    /**
     * Get the override value
     * 
     * @return the overrideValue
     */
    public abstract String getOverrideValue();
    
    
    /**
     * Set the override value
     * 
     * @param overrideValue
     *            the overrideValue to set
     */
    public abstract void setOverrideValue(String overrideValue);
    
}
