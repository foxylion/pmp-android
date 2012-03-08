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

import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface ILocalizedString extends IIssueLocation {
    
    /**
     * Get the name
     * 
     * @return name
     */
    public abstract String getString();
    
    
    /**
     * Set the name
     * 
     * @param name
     *            name to set
     */
    public abstract void setString(String name);
    
    
    /**
     * Get the locale
     * 
     * @return locale
     */
    public abstract Locale getLocale();
    
    
    /**
     * Set the locale
     * 
     * @param locale
     *            locale to set
     */
    public abstract void setLocale(Locale locale);
    
}
