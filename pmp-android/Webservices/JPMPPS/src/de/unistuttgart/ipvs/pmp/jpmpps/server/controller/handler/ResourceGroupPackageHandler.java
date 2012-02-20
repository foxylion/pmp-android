package de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroupPackage;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.CachedRequestResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.NoSuchPackageResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupPackageResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.model.Model;
import de.unistuttgart.ipvs.pmp.jpmpps.model.ResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.server.ResponseHasher;

public class ResourceGroupPackageHandler implements IRequestHandler {
    
    @Override
    public AbstractResponse process(AbstractRequest request) {
        if (!(request instanceof RequestResourceGroupPackage)) {
            throw new IllegalArgumentException("Should be an instance of "
                    + RequestResourceGroupPackage.class.getSimpleName());
        }
        
        RequestResourceGroupPackage rgRequest = (RequestResourceGroupPackage) request;
        
        ResourceGroup rg = Model.get().getResourceGroups().get(rgRequest.getPackageName());
        
        if (rg == null) {
            return new NoSuchPackageResponse();
        } else if (ResponseHasher.checkHash(rg, rgRequest.getCacheHash())) {
            return new CachedRequestResponse();
        } else {
            return new ResourceGroupPackageResponse(rg.getInputStream(), ResponseHasher.hash(rg.getRevision()));
        }
    }
}
