/*
 * Copyright 2012 pmp-android development team
 * Project: JPMPPS
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
package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Very simple model for the {@link ResourceGroup}s.
 * 
 * @author Jakob Jarosch
 */
public class Model {
    
    private static Model instance = null;
    
    
    private Model() {
        
    }
    
    
    /**
     * @return Returns the instance of the model.
     */
    public static Model get() {
        if (instance == null) {
            instance = new Model();
        }
        
        return instance;
    }
    
    /**
     * All current visible {@link ResourceGroup}s are in this map.
     */
    private Map<String, ResourceGroup> resourceGroups = new HashMap<String, ResourceGroup>();
    
    
    /**
     * @return Returns all available {@link ResourceGroup}.
     */
    public Map<String, ResourceGroup> getResourceGroups() {
        return this.resourceGroups;
    }
    
    
    /**
     * Replaces the current {@link ResourceGroup}s with a new version.
     */
    public void replaceResourceGroups(Map<String, ResourceGroup> resourceGroups) {
        this.resourceGroups = resourceGroups;
    }
}
