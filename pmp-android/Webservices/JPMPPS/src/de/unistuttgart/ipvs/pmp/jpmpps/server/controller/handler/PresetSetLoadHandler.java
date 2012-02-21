package de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetLoad;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.PresetSetLoadResponse;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;

/**
 * The {@link PresetSetLoadHandler} processes a {@link RequestPresetSetLoad} request.
 * And returns a {@link PresetSetLoadResponse}.
 * 
 * @author Jakob Jarosch
 */
public class PresetSetLoadHandler implements IRequestHandler {
    
    @Override
    public AbstractResponse process(AbstractRequest request) {
        /* Check if the request has a correct instance. */
        if (!(request instanceof RequestPresetSetLoad)) {
            throw new IllegalArgumentException("Should be an instance of " + RequestPresetSetLoad.class.getSimpleName());
        }
        
        /* Cast the request and load the id */
        RequestPresetSetLoad loadRequest = (RequestPresetSetLoad) request;
        String id = loadRequest.getPresetSetId();
        
        if (id == null) {
            return new PresetSetLoadResponse(null);
        }
        
        /* Return the PresetSet */
        IPresetSet presetSet = JPMPPS.get().getPresetSet(id);
        return new PresetSetLoadResponse(presetSet);
    }
    
}
