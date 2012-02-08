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
package de.unistuttgart.ipvs.pmp.xmlutil.rgis;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.RGISIssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISPrivacySetting extends BasicIS implements RGISIssueLocation {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -3533822744161444028L;
    
    /**
     * Identifier of the privacy setting
     */
    private String identifier = "";
    
    /**
     * A description of the valid values for this privacy setting
     */
    private String validValueDescription = "";
    
    
    /**
     * Constructor without attributes
     */
    public RGISPrivacySetting() {
    }
    
    
    /**
     * Constructor to set the identifier and the valid value description
     * 
     * @param identifier
     *            identifier to set
     * @param validValueDescription
     *            validValueDescription to set
     */
    public RGISPrivacySetting(String identifier, String validValueDescription) {
        setIdentifier(identifier);
        setValidValueDescription(validValueDescription);
    }
    
    
    /**
     * Get the identifier
     * 
     * @return identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    /**
     * Set the identifier
     * 
     * @param identifier
     *            identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    
    /**
     * Get the description for valid values
     * 
     * @return description for valid values
     */
    public String getValidValueDescription() {
        return this.validValueDescription;
    }
    
    
    /**
     * Set the description for valid values
     * 
     * @param validValueDescription
     *            description for valid values
     */
    public void setValidValueDescription(String validValueDescription) {
        this.validValueDescription = validValueDescription;
    }
    
}
