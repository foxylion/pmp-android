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
package de.unistuttgart.ipvs.pmp.xmlutil.rgis;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.common.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISPrivacySetting extends BasicIS implements IRGISPrivacySetting {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -3533822744161444028L;
    
    /**
     * The identifier
     */
    private String identifier = "";
    
    /**
     * A description of the valid values for this privacy setting
     */
    private String validValueDescription = "";
    
    /**
     * List of change descriptions
     */
    private List<ILocalizedString> changeDescriptions = new ArrayList<ILocalizedString>();
    
    
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
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting#getValidValueDescription()
     */
    @Override
    public String getValidValueDescription() {
        return this.validValueDescription;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting#setValidValueDescription(java.lang.String)
     */
    @Override
    public void setValidValueDescription(String validValueDescription) {
        this.validValueDescription = validValueDescription;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting#addChangeDescription(de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.LocalizedString)
     */
    @Override
    public void addChangeDescription(ILocalizedString changeDescription) {
        this.changeDescriptions.add(changeDescription);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting#getChangeDescriptions()
     */
    @Override
    public List<ILocalizedString> getChangeDescriptions() {
        return this.changeDescriptions;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting#removeChangeDescription(de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.ILocalizedString)
     */
    @Override
    public void removeChangeDescription(ILocalizedString changeDescription) {
        this.changeDescriptions.remove(changeDescription);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting#getChangeDescriptionForLocale(java.util.Locale)
     */
    @Override
    public String getChangeDescriptionForLocale(Locale locale) {
        for (ILocalizedString changeDescr : this.changeDescriptions) {
            if (changeDescr.getLocale() == null) {
                continue;
            }
            if (changeDescr.getLocale().getLanguage().equals(locale.getLanguage())) {
                return changeDescr.getString();
            }
        }
        return null;
    }
    
    
    @Override
    public void clearIssues() {
        super.clearIssues();
        super.clearNameIssues();
        super.clearDescriptionIssues();
        for (ILocalizedString changeDescr : getChangeDescriptions()) {
            changeDescr.clearIssues();
        }
    }
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
}
