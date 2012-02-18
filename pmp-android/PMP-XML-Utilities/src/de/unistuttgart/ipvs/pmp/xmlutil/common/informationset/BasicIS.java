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
package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

/**
 * This class abstracts common used fields and methods for information sets
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class BasicIS extends IssueLocation {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -7705624993277295194L;
    
    /**
     * This list contains all names.
     */
    protected List<LocalizedString> names = new ArrayList<LocalizedString>();
    
    /**
     * This list contains all descriptions.
     */
    protected List<LocalizedString> descriptions = new ArrayList<LocalizedString>();
    
    
    /**
     * Get all names.
     * 
     * @return list with names
     */
    public List<LocalizedString> getNames() {
        return this.names;
    }
    
    
    /**
     * Get a name-string for a specific locale.
     * 
     * @param locale
     *            locale
     * @return the name-string for the given locale. Null, if no name for the
     *         given locale exists.
     */
    public String getNameForLocale(Locale locale) {
        for (LocalizedString name : this.names) {
            if (name.getLocale().getLanguage().equals(locale.getLanguage())) {
                return name.getString();
            }
        }
        return null;
    }
    
    
    /**
     * Add a name.
     * 
     * @param name
     *            name to add
     */
    public void addName(LocalizedString name) {
        this.names.add(name);
    }
    
    
    /**
     * Remove a name.
     * 
     * @param name
     *            name to remove
     */
    public void removeName(LocalizedString name) {
        this.names.remove(name);
    }
    
    
    /**
     * Get all descriptions.
     * 
     * @return list with descriptions
     */
    public List<LocalizedString> getDescriptions() {
        return this.descriptions;
    }
    
    
    /**
     * Get a description-string for a specific locale.
     * 
     * @param locale
     *            locale
     * @return the description-string for the given locale. Null, if no description for the
     *         given locale exists.
     */
    public String getDescriptionForLocale(Locale locale) {
        for (LocalizedString descr : this.descriptions) {
            if (descr.getLocale().getLanguage().equals(locale.getLanguage())) {
                return descr.getString();
            }
        }
        return null;
    }
    
    
    /**
     * Add a description
     * 
     * @param description
     *            description to add
     */
    public void addDescription(LocalizedString description) {
        this.descriptions.add(description);
    }
    
    
    /**
     * Remove a description
     * 
     * @param description
     *            description to remove
     */
    public void removeDescription(LocalizedString description) {
        this.descriptions.remove(description);
    }
    
    
    /**
     * Clear all issues of the names
     */
    public void clearNameIssues() {
        for (LocalizedString name : this.getNames()) {
            name.clearIssuesAndPropagate();
        }
    }
    
    
    /**
     * Clear all issues of the descriptions
     */
    public void clearDescriptionIssues() {
        for (LocalizedString descr : this.getDescriptions()) {
            descr.clearIssuesAndPropagate();
        }
    }
    
}
