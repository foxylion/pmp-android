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
package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IAIS extends IBasicIS {
    
    /**
     * Add a service feature to the app
     * 
     * @param sf
     *            service feature
     */
    public abstract void addServiceFeature(IAISServiceFeature sf);
    
    
    /**
     * Remove a service feature of the app
     * 
     * @param sf
     *            service feature
     */
    public abstract void removeServiceFeature(IAISServiceFeature sf);
    
    
    /**
     * Get the list which contains all service features
     * 
     * @return list with service features
     */
    public abstract List<IAISServiceFeature> getServiceFeatures();
    
    
    /**
     * Get a service feature for a given identifier. Null, if no service feature
     * exists for the given identifier.
     * 
     * @param identifier
     *            identifier of the service feature
     * @return service feature with given identifier, null if none exists.
     */
    public abstract IAISServiceFeature getServiceFeatureForIdentifier(String identifier);
    
    
    /**
     * Clear only issues referring to the app information
     */
    public abstract void clearAppInformationIssuesAndPropagate();
    
    
    /**
     * Clear only issues referring to the service features
     */
    public abstract void clearServiceFeaturesIssuesAndPropagate();
    
}
