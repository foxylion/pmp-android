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

import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IRGISPrivacySetting extends IBasicIS, IIdentifierIS {
    
    /**
     * Get the description for valid values
     * 
     * @return description for valid values
     */
    public abstract String getValidValueDescription();
    
    
    /**
     * Set the description for valid values
     * 
     * @param validValueDescription
     *            description for valid values
     */
    public abstract void setValidValueDescription(String validValueDescription);
    
    
    /**
     * Add a change descriptions to the privacy setting
     * 
     * @param changeDescription
     *            change description to add
     */
    public abstract void addChangeDescription(ILocalizedString changeDescription);
    
    
    /**
     * Get the list which contains all change descriptions as {@link ILocalizedString}s
     * 
     * @return list with change descriptions as {@link ILocalizedString}s
     */
    public abstract List<ILocalizedString> getChangeDescriptions();
    
    
    /**
     * Remove a change description from the {@link IRGISPrivacySetting} as {@link ILocalizedString}s
     * 
     * @param changeDescription
     *            change description as {@link ILocalizedString} to remove
     */
    public abstract void removeChangeDescription(ILocalizedString changeDescription);
    
    
    /**
     * Get a change description-string for a specific locale.
     * 
     * @param locale
     *            locale
     * @return the change description-string for the given locale. Null, if no change description for the
     *         given locale exists.
     */
    public abstract String getChangeDescriptionForLocale(Locale locale);
    
    
    /**
     * True, if the {@link IRGISPrivacySetting} is requestable
     * 
     * @return flag
     */
    public abstract boolean isRequestable();
    
    
    /**
     * Set if the {@link IRGISPrivacySetting} requestable
     * 
     * @param requestable
     *            flag
     */
    public abstract void setRequestable(boolean requestable);
    
}
