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
package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

import java.io.IOException;
import java.io.ObjectInputStream;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetLoad;

/**
 * Response for an {@link RequestPresetSetLoad} request.
 * 
 * @author Jakob Jarosch
 */
public class PresetSetLoadResponse extends AbstractResponse {
    
    private static final long serialVersionUID = 1L;
    
    private byte[] presetSet;
    
    
    /**
     * Creates a new {@link PresetSetLoadResponse}.
     * 
     * @param presetSet
     *            PresetSet which should be returned.
     */
    public PresetSetLoadResponse(Object presetSet) {
        if (presetSet == null) {
            this.presetSet = null;
        }
        
        this.presetSet = toByteArray(presetSet);
    }
    
    
    /**
     * @return Returns the preset set or null if no preset set with the id was found.
     * @throws ClassNotFoundException
     *             Is thrown when the PMP-XML-UTILITIES are missing, or in an improper version.
     */
    public Object getPresetSet() throws ClassNotFoundException {
        if (this.presetSet == null) {
            return null;
        }
        
        try {
            return new ObjectInputStream(fromByteArray(presetSet)).readObject();
        } catch (IOException e) {
            System.err.println("Failed to deserialize the preset set.");
            e.printStackTrace();
            
            return null;
        }
    }
    
}
