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
