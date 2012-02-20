package de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler;

import java.io.InvalidClassException;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetSave;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.PresetSetSaveResponse;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;


public class PresetSetSaveHandler implements IRequestHandler {
    
    @Override
    public AbstractResponse process(AbstractRequest request) {
        if (!(request instanceof RequestPresetSetSave)) {
            throw new IllegalArgumentException("Should be an instance of "
                    + RequestPresetSetSave.class.getSimpleName());
        }
        
        RequestPresetSetSave saveRequest = (RequestPresetSetSave) request;

        Object presetSet = null;
        try {
            presetSet = saveRequest.getPresetSet();
        } catch (ClassNotFoundException e) {
            return new PresetSetSaveResponse(false, "The submitted preset set has an unknown class instance" + e.getMessage());
        } catch (InvalidClassException e) {
            return new PresetSetSaveResponse(false, "The submitted preset set has an invalid class version" + e.getMessage());
        }
        
        if(presetSet == null) {
            return new PresetSetSaveResponse(false, "The submitted preset was null");
        }
        
        if(!(presetSet instanceof IPresetSet)) {
           return new PresetSetSaveResponse(false, "The submitted preset is not an instance of PresetSet");
        }

        IPresetSet set = (IPresetSet) presetSet;
        String id = JPMPPS.get().savePresetSet(set);
        return new PresetSetSaveResponse(true, id);
    }
    
}
