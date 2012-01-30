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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ISException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ISException.Type;

/**
 * This class abstracts common used fields and methods for the information sets of apps and resourcegroups
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractIS {
    
    /**
     * This map contains all names. key = locale
     */
    protected Map<Locale, String> names;
    
    /**
     * This map contains all descriptions. key = locale
     */
    protected Map<Locale, String> descriptions;
    
    
    /**
     * Constructor is used to instantiate the data structures.
     */
    public AbstractIS() {
        this.names = new HashMap<Locale, String>();
        this.descriptions = new HashMap<Locale, String>();
    }
    
    
    /**
     * Get all names.
     * 
     * @return map with names, key = locale
     */
    public Map<Locale, String> getNames() {
        return this.names;
    }
    
    
    /**
     * Add a name.
     * 
     * @param locale
     *            of the name
     * @param name
     *            name
     */
    public void addName(Locale locale, String name) {
        if (this.names.containsKey(locale)) {
            throw new ISException(Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS, "The name with the locale "
                    + locale.getLanguage() + " already exists.");
        }
        this.names.put(locale, name);
    }
    
    
    /**
     * Get all descriptions.
     * 
     * @return map with descriptions, key = locale
     */
    public Map<Locale, String> getDescriptions() {
        return this.descriptions;
    }
    
    
    /**
     * Add a description
     * 
     * @param locale
     *            of the description
     * @param description
     *            description
     */
    public void addDescription(Locale locale, String description) {
        if (this.descriptions.containsKey(locale)) {
            throw new ISException(Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
                    "The description with the locale " + locale.getLanguage() + " already exists.");
        }
        this.descriptions.put(locale, description);
    }
    
}
