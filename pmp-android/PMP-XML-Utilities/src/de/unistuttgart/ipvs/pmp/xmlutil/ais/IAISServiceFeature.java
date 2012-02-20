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
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;

/**
 * This is the interface of the {@link AISServiceFeature}
 * 
 * @author Marcus Vetter
 * 
 */
public interface IAISServiceFeature extends IBasicIS, IIdentifierIS {
    
    /**
     * Get all {@link IAISRequiredResourceGroup}s of the {@link IAISServiceFeature}
     * 
     * @return list with {@link IAISRequiredResourceGroup}s
     */
    public abstract List<IAISRequiredResourceGroup> getRequiredResourceGroups();
    
    
    /**
     * Add a {@link IAISRequiredResourceGroup} to the {@link IAISServiceFeature}
     * 
     * @param rrg
     *            the {@link IAISRequiredResourceGroup} to add
     */
    public abstract void addRequiredResourceGroup(IAISRequiredResourceGroup rrg);
    
    
    /**
     * Remove a {@link IAISRequiredResourceGroup} from the {@link IAISServiceFeature}
     * 
     * @param rrg
     *            required resource group to remove
     */
    public abstract void removeRequiredResourceGroup(IAISRequiredResourceGroup rrg);
    
    
    /**
     * Get a {@link IAISRequiredResourceGroup} for a given identifier. Null, if no {@link IAISRequiredResourceGroup}
     * exists for the given identifier.
     * 
     * @param identifier
     *            identifier of the {@link IAISRequiredResourceGroup}
     * @return {@link IAISRequiredResourceGroup} with given identifier, null if none
     *         exists.
     */
    public abstract IAISRequiredResourceGroup getRequiredResourceGroupForIdentifier(String identifier);
    
}
