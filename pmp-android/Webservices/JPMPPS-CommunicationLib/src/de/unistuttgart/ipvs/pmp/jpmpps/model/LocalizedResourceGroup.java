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
package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.io.Serializable;

/**
 * A localized version of the {@link ResourceGroup}, saves memory and bandwidth
 * during transmission.
 * 
 * @author Jakob Jarosch
 * 
 */
public class LocalizedResourceGroup implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private String identifier;
    private long revision;
    
    
    public String getName() {
        return this.name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getDescription() {
        return this.description;
    }
    
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    
    public long getRevision() {
        return this.revision;
    }
    
    
    public void setRevision(long revision) {
        this.revision = revision;
    }
}
