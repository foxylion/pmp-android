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
package de.unistuttgart.ipvs.pmp.xmlutil.common;

import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IBasicIS extends IIssueLocation {
    
    /**
     * Get all names as {@link ILocalizedString}s
     * 
     * @return list with names as {@link ILocalizedString}s
     */
    public abstract List<ILocalizedString> getNames();
    
    
    /**
     * Get a name-string for a specific locale.
     * 
     * @param locale
     *            locale
     * @return the name-string for the given locale. Null, if no name for the
     *         given locale exists.
     */
    public abstract String getNameForLocale(Locale locale);
    
    
    /**
     * Add a name as {@link ILocalizedString}
     * 
     * @param name
     *            name to add
     */
    public abstract void addName(ILocalizedString name);
    
    
    /**
     * Remove a name as {@link ILocalizedString}
     * 
     * @param name
     *            name to remove
     */
    public abstract void removeName(ILocalizedString name);
    
    
    /**
     * Get all descriptions as {@link ILocalizedString}s
     * 
     * @return list with descriptions
     */
    public abstract List<ILocalizedString> getDescriptions();
    
    
    /**
     * Get a description-string for a specific locale.
     * 
     * @param locale
     *            locale
     * @return the description-string for the given locale. Null, if no description for the
     *         given locale exists.
     */
    public abstract String getDescriptionForLocale(Locale locale);
    
    
    /**
     * Add a description as {@link ILocalizedString}
     * 
     * @param description
     *            description to add
     */
    public abstract void addDescription(ILocalizedString description);
    
    
    /**
     * Remove a description as {@link ILocalizedString}
     * 
     * @param description
     *            description to remove
     */
    public abstract void removeDescription(ILocalizedString description);
    
    
    /**
     * Clear all issues of the names
     */
    public abstract void clearNameIssues();
    
    
    /**
     * Clear all issues of the descriptions
     */
    public abstract void clearDescriptionIssues();
    
}
