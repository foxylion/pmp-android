/*
 * Copyright 2012 pmp-android development team
 * Project: JPMPPS-CommunicationLib
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
package de.unistuttgart.ipvs.pmp.jpmpps.io.request;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;

import de.unistuttgart.ipvs.pmp.jpmpps.io.response.PresetSetSaveResponse;

/**
 * Request for saving a presetSet on the server.
 * Server will answer with a {@link PresetSetSaveResponse}.
 * 
 * @author Jakob Jarosch
 */
public class RequestPresetSetSave extends AbstractRequest {
    
    private static final long serialVersionUID = 1L;
    
    private byte[] presetSet;
    
    
    /**
     * Creates a new {@link RequestPresetSetSave} instance.
     * 
     * @param presetSet
     *            The preset set which should be saved.
     */
    public RequestPresetSetSave(Object presetSet) {
        if (presetSet == null) {
            throw new IllegalArgumentException("presetSet should never be null.");
        }
        
        this.presetSet = toByteArray(presetSet);
    }
    
    
    public Object getPresetSet() throws ClassNotFoundException, InvalidClassException {
        if (this.presetSet == null) {
            return null;
        }
        
        try {
            return new ObjectInputStream(fromByteArray(this.presetSet)).readObject();
        } catch (IOException e) {
            System.err.println("Failed to deserialize the preset set.");
            e.printStackTrace();
            
            return null;
        }
    }
}
