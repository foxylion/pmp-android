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

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetPSContext extends IssueLocation implements Serializable, IPresetPSContext {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 8503765925521936460L;
    
    /**
     * The type
     */
    private String type = "";
    
    /**
     * The condition
     */
    private String condition = "";
    
    /**
     * The flag, if its an empty condition
     */
    private boolean emptyCondition = false;
    
    /**
     * The override value
     */
    private String overrideValue = "";
    
    /**
     * The flag, if its an empty override value
     */
    private boolean emptyOverrideValue = false;
    
    
    /**
     * Constructor to set the attributes
     * 
     * @param type
     *            type
     * @param condition
     *            condition
     * @param overrideValue
     *            overrideValue
     */
    public PresetPSContext(String type, String condition, String overrideValue) {
        this.type = type;
        this.condition = condition;
        this.overrideValue = overrideValue;
    }
    
    
    @Override
    public String getType() {
        return this.type;
    }
    
    
    @Override
    public void setType(String type) {
        this.type = type;
    }
    
    
    @Override
    public String getCondition() {
        return this.condition;
    }
    
    
    @Override
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    
    @Override
    public String getOverrideValue() {
        return this.overrideValue;
    }
    
    
    @Override
    public void setOverrideValue(String overrideValue) {
        this.overrideValue = overrideValue;
    }
    
    
    @Override
    public boolean isEmptyOverrideValue() {
        return this.emptyOverrideValue;
    }
    
    
    @Override
    public void setEmptyOverrideValue(boolean emptyOverrideValue) {
        this.emptyOverrideValue = emptyOverrideValue;
    }
    
    
    @Override
    public boolean isEmptyCondition() {
        return this.emptyCondition;
    }
    
    
    @Override
    public void setEmptyCondition(boolean emptyCondition) {
        this.emptyCondition = emptyCondition;
    }
    
}
