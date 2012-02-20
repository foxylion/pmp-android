package de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler;


import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetLoad;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.PresetSetLoadResponse;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;


public class PresetSetLoadHandler implements IRequestHandler {
    
    @Override
    public AbstractResponse process(AbstractRequest request) {
        if (!(request instanceof RequestPresetSetLoad)) {
            throw new IllegalArgumentException("Should be an instance of "
                    + RequestPresetSetLoad.class.getSimpleName());
        }
        
        RequestPresetSetLoad loadRequest = (RequestPresetSetLoad) request;
        String id = loadRequest.getPresetSetId();
        
        if(id == null) {
            return new PresetSetLoadResponse(null);
        }
        
        IPresetSet presetSet = JPMPPS.get().getPresetSet(id);
        return new PresetSetLoadResponse(presetSet);
    }
    
}
