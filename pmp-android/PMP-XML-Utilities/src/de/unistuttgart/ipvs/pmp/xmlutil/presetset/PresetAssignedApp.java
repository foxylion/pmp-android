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
public class PresetAssignedApp extends IssueLocation implements Serializable, IPresetAssignedApp {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6897921426190796025L;
    /**
     * The identifier
     */
    private String identifier = "";
    
    
    /**
     * Constructor to set the identifier
     * 
     * @param identifier
     *            identifier of the assigned app
     */
    public PresetAssignedApp(String identifier) {
        this.identifier = identifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedApp#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedApp#setIdentifier(java.lang.String)
     */
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
