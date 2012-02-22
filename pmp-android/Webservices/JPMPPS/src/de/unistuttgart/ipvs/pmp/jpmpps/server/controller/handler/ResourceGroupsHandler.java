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
package de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.CachedRequestResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupsResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.server.ResponseHasher;

/**
 * The {@link ResourceGroupsHandler} reacts on {@link RequestResourceGroups} requests.
 * It normally returns a {@link ResourceGroupsResponse} or a {@link CachedRequestResponse} when the response is cached.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupsHandler implements IRequestHandler {
    
    @Override
    public AbstractResponse process(AbstractRequest request) {
        if (!(request instanceof RequestResourceGroups)) {
            throw new IllegalArgumentException("Should be an instance of "
                    + RequestResourceGroups.class.getSimpleName());
        }
        
        RequestResourceGroups rgsRequest = (RequestResourceGroups) request;
        
        LocalizedResourceGroup[] rgs = JPMPPS.get().findResourceGroups(rgsRequest.getLocale(), rgsRequest.getFilter());
        
        if (ResponseHasher.checkHash(rgsRequest.getLocale(), rgs, rgsRequest.getCacheHash())) {
            return new CachedRequestResponse();
        } else {
            return new ResourceGroupsResponse(rgs, ResponseHasher.hash(rgsRequest.getLocale(), rgs));
        }
    }
}
