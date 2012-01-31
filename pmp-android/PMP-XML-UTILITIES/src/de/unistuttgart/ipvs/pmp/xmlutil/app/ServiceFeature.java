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
package de.unistuttgart.ipvs.pmp.xmlutil.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ISException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ISException.Type;

/**
 * This is a service feature, which is assigned to an app and contains all required resource groups of the app.
 * 
 * @author Marcus Vetter
 * 
 */
public class ServiceFeature implements Serializable {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -3279934293726339125L;
    
    /**
     * This map contains all names of the app. key = locale
     */
    private Map<Locale, String> names;
    
    /**
     * This map contains all description of the app. key = locale
     */
    private Map<Locale, String> descriptions;
    
    /**
     * This list contains all required resource groups of the service feature.
     * key = identifier
     */
    private Map<String, RequiredResourceGroup> requiredResourceGroups;
    
    
    /**
     * Constructor is used to instantiate the data structures.
     */
    public ServiceFeature() {
        this.names = new HashMap<Locale, String>();
        this.descriptions = new HashMap<Locale, String>();
        this.requiredResourceGroups = new HashMap<String, RequiredResourceGroup>();
    }
    
    
    /**
     * Get all names of the service feature
     * 
     * @return map with names, key = locale
     */
    public Map<Locale, String> getNames() {
        return this.names;
    }
    
    
    /**
     * Add a name to the service feature by using the associated locale
     * 
     * @param locale
     *            of the given name
     * @param name
     *            of the service feature
     */
    public void addName(Locale locale, String name) {
        if (this.names.containsKey(locale)) {
            throw new ISException(Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,
                    "The name of a service feature with the locale " + locale.getLanguage() + " already exists.");
        }
        this.names.put(locale, name);
    }
    
    
    /**
     * Get all descriptions of the service feature
     * 
     * @return map with descriptions, key = locale
     */
    public Map<Locale, String> getDescriptions() {
        return this.descriptions;
    }
    
    
    /**
     * Add a description to the service feature by using the associated locale
     * 
     * @param locale
     *            of the given description
     * @param description
     *            of the service feature
     */
    public void addDescription(Locale locale, String description) {
        if (this.descriptions.containsKey(locale)) {
            throw new ISException(Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
                    "The description of a service feature with the locale " + locale.getLanguage() + " already exists.");
        }
        this.descriptions.put(locale, description);
    }
    
    
    /**
     * Get all required resource groups of the service feature
     * 
     * @return map with required resource groups, key = identifier
     */
    public Map<String, RequiredResourceGroup> getRequiredResourceGroups() {
        return this.requiredResourceGroups;
    }
    
    
    /**
     * Add a required resource group to the service feature
     * 
     * @param identifier
     *            identifier of the required Resourcegroup
     * @param rrg
     *            the required Resourcegroup
     */
    public void addRequiredResourceGroup(String identifier, RequiredResourceGroup rrg) {
        if (this.requiredResourceGroups.containsKey(identifier)) {
            throw new ISException(Type.REQUIRED_RESOUCEGROUP_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
                    "The identifier of a required resourcegroup already exists.");
        }
        this.requiredResourceGroups.put(identifier, rrg);
    }
    
}
