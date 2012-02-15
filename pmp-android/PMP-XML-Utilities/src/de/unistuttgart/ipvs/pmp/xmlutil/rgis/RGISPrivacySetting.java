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

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIdentifierIS;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISPrivacySetting extends BasicIdentifierIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -3533822744161444028L;
    
    /**
     * A description of the valid values for this privacy setting
     */
    private String validValueDescription = "";
    
    /**
     * List of change descriptions
     */
    private List<RGISPSChangeDescription> changeDescriptions = new ArrayList<RGISPSChangeDescription>();
    
    
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
    
    
    /**
     * Add a change descriptions to the privacy setting
     * 
     * @param changeDescription
     *            change description to add
     */
    public void addChangeDescription(RGISPSChangeDescription changeDescription) {
        this.changeDescriptions.add(changeDescription);
    }
    
    
    /**
     * Get the list which contains all change descriptions
     * 
     * @return list with change descriptions
     */
    public List<RGISPSChangeDescription> getChangeDescriptions() {
        return this.changeDescriptions;
    }
    
    
    /**
     * Remove a change description from the privacy setting
     * 
     * @param changeDescription
     *            change description to remove
     */
    public void removeChangeDescription(RGISPSChangeDescription changeDescription) {
        this.changeDescriptions.remove(changeDescription);
    }
    
    
    @Override
    public void clearIssuesAndPropagate() {
        super.getIssues().clear();
        super.clearNameIssues();
        super.clearDescriptionIssues();
    }
    
}
