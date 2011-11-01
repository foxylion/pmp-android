/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
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
package de.unistuttgart.ipvs.pmp.app.xmlparser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParserException.Type;

/**
 * This is an information set of the app. It contains all basic informations (names and descriptions in different
 * locals) and all provided service levels.
 * 
 * @author Marcus Vetter
 */
public class AppInformationSet implements Serializable {
    
    private static final long serialVersionUID = 2629559699588037711L;
    
    /**
     * This map contains all names of the app. key = locale
     */
    private Map<Locale, String> names;
    
    /**
     * This map contains all description of the app. key = locale
     */
    private Map<Locale, String> descriptions;
    
    /**
     * This map contains all service levels of the app. key = level
     */
    private Map<Integer, ServiceLevel> serviceLevels;
    
    
    /**
     * Constructor is used to instantiate the data structures.
     */
    protected AppInformationSet() {
        this.names = new HashMap<Locale, String>();
        this.descriptions = new HashMap<Locale, String>();
        this.serviceLevels = new HashMap<Integer, ServiceLevel>();
    }
    
    
    /**
     * Get all names of the app
     * 
     * @return map with names, key = locale
     */
    public Map<Locale, String> getNames() {
        return this.names;
    }
    
    
    /**
     * Get all descriptions of the app
     * 
     * @return map with descriptions, key = locale
     */
    public Map<Locale, String> getDescriptions() {
        return this.descriptions;
    }
    
    
    /**
     * Get all service levels of the app
     * 
     * @return map with service levels, key = level
     */
    public Map<Integer, ServiceLevel> getServiceLevels() {
        return this.serviceLevels;
    }
    
    
    /**
     * Add a name to the app
     * 
     * @param locale
     *            of the name
     * @param name
     */
    protected void addName(Locale locale, String name) {
        if (this.names.containsKey(locale)) {
            throw new XMLParserException(Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,
                    "The name of the app with the locale " + locale.getLanguage() + " already exists.");
        }
        this.names.put(locale, name);
    }
    
    
    /**
     * Add a description to the app
     * 
     * @param locale
     *            of the description
     * @param description
     */
    protected void addDescription(Locale locale, String description) {
        if (this.descriptions.containsKey(locale)) {
            throw new XMLParserException(Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
                    "The description of the app with the locale " + locale.getLanguage() + " already exists.");
        }
        this.descriptions.put(locale, description);
    }
    
    
    /**
     * Add a service level to the app
     * 
     * @param level
     *            of the service level
     * @param sl
     *            service level
     */
    protected void addServiceLevel(int level, ServiceLevel sl) {
        if (this.serviceLevels.containsKey(level)) {
            throw new XMLParserException(Type.SERVICE_LEVEL_WITH_SAME_LEVEL_ALREADY_EXISTS, "The level " + level
                    + " of a service level already exists.");
        }
        this.serviceLevels.put(level, sl);
    }
    
}
