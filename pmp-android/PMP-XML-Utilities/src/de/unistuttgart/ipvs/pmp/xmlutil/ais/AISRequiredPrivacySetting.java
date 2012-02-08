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
package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import java.io.Serializable;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.AISIssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class AISRequiredPrivacySetting implements Serializable, AISIssueLocation {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -2494745855919623707L;
    
    /**
     * The identifier of the Privacy Setting
     */
    private String identifier = "";
    
    /**
     * The value of the Privacy Setting
     */
    private String value = "";
    
    
    /**
     * Constructor to set the attributes
     * 
     * @param identifier
     *            identifier of the Privacy Setting
     * @param value
     *            value of the Privacy Setting
     */
    public AISRequiredPrivacySetting(String identifier, String value) {
        setIdentifier(identifier);
        setValue(value);
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
     * Get the value
     * 
     * @return value
     */
    public String getValue() {
        return this.value;
    }
    
    
    /**
     * Set the value
     * 
     * @param value
     *            value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
}
