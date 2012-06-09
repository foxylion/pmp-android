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

import java.io.InvalidClassException;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetSave;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.PresetSetSaveResponse;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;

/**
 * THe {@link PresetSetSaveHandler} react on a {@link RequestPresetSetSave} request.
 * It always returns a {@link PresetSetSaveResponse}.
 * 
 * @author Jakob Jarosch
 * 
 */
public class PresetSetSaveHandler implements IRequestHandler {
    
    @Override
    public AbstractResponse process(AbstractRequest request) {
        /* Check if the request is a correct instance */
        if (!(request instanceof RequestPresetSetSave)) {
            throw new IllegalArgumentException("Should be an instance of " + RequestPresetSetSave.class.getSimpleName());
        }
        
        RequestPresetSetSave saveRequest = (RequestPresetSetSave) request;
        
        /* Try to load the presetSet from the request */
        Object presetSet = null;
        try {
            presetSet = saveRequest.getPresetSet();
        } catch (ClassNotFoundException e) {
            return new PresetSetSaveResponse(false, "The submitted preset set has an unknown class instance"
                    + e.getMessage());
        } catch (InvalidClassException e) {
            return new PresetSetSaveResponse(false, "The submitted preset set has an invalid class version"
                    + e.getMessage());
        }
        
        if (presetSet == null) {
            return new PresetSetSaveResponse(false, "The submitted preset was null");
        }
        
        if (!(presetSet instanceof IPresetSet)) {
            return new PresetSetSaveResponse(false, "The submitted preset is not an instance of PresetSet");
        }
        
        /* Try to save the preset set */
        IPresetSet set = (IPresetSet) presetSet;
        String id = JPMPPS.get().savePresetSet(set);
        return new PresetSetSaveResponse(true, id);
    }
    
}
