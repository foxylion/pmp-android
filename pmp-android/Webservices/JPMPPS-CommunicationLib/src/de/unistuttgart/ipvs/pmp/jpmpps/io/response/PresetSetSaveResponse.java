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

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetSave;

/**
 * A answer for a {@link RequestPresetSetSave}.
 * 
 * @author Jakob Jarosch
 */
public class PresetSetSaveResponse extends AbstractResponse {
    
    private static final long serialVersionUID = 1L;
    
    private boolean success;
    private String messageOrId;
    
    
    /**
     * Creates a new instance of the {@link PresetSetSaveResponse}.
     * 
     * @param success
     *            Whether the save request succeeded or not.
     * @param messageOrId
     *            The message on failure and the id on success.
     */
    public PresetSetSaveResponse(boolean success, String messageOrId) {
        this.success = success;
        this.messageOrId = messageOrId;
    }
    
    
    /**
     * @return Returns the success of the save request.
     */
    public boolean isSuccess() {
        return this.success;
    }
    
    
    /**
     * @return Returns the failure message when the save request failed.
     */
    public String getMessage() {
        if (this.success) {
            return null;
        }
        
        return this.messageOrId;
    }
    
    
    /**
     * @return Returns the id of the saved preset set when saving succeeded.
     */
    public String getId() {
        if (!this.success) {
            return null;
        }
        
        return this.messageOrId;
    }
}
