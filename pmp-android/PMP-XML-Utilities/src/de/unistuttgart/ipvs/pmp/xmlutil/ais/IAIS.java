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
package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;

/**
 * This is the interface of the {@link AIS}
 * 
 * @author Marcus Vetter
 * 
 */
public interface IAIS extends IBasicIS {
    
    /**
     * Add a {@link IAISServiceFeature} to the {@link IAIS}
     * 
     * @param sf
     *            {@link IAISServiceFeature} to add
     */
    public abstract void addServiceFeature(IAISServiceFeature sf);
    
    
    /**
     * Remove a {@link IAISServiceFeature} from the {@link IAIS}
     * 
     * @param sf
     *            {@link IAISServiceFeature} to remove
     */
    public abstract void removeServiceFeature(IAISServiceFeature sf);
    
    
    /**
     * Get the list which contains all {@link IAISServiceFeature}s.
     * 
     * @return list with {@link IAISServiceFeature}s
     */
    public abstract List<IAISServiceFeature> getServiceFeatures();
    
    
    /**
     * Get a {@link IAISServiceFeature} for a given identifier. Null, if no {@link IAISServiceFeature} exists for the
     * given identifier.
     * 
     * @param identifier
     *            identifier of the {@link IAISServiceFeature}
     * @return {@link IAISServiceFeature} with given identifier, null if none exists.
     */
    public abstract IAISServiceFeature getServiceFeatureForIdentifier(String identifier);
    
    
    /**
     * Clear only issues referring to the app information
     */
    public abstract void clearAppInformationIssues();
    
    
    /**
     * Clear only issues referring to the service features
     */
    public abstract void clearServiceFeaturesIssues();
    
}
