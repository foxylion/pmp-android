/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
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
package de.unistuttgart.ipvs.pmp.resource;

import java.io.Serializable;

public class ResourceGroupAccessHeader implements Serializable {
    
    private static final long serialVersionUID = -7482979934059030704L;
    
    private String identifier;
    private byte[] publicKey;
    
    
    /**
     * Creates a new {@link ResourceGroupAccessHeader}.
     * 
     * @param identifier
     *            identifier of the app
     * @param publicKey
     *            public key of the app
     */
    public ResourceGroupAccessHeader(String identifier, byte[] publicKey) {
        this.identifier = identifier;
        this.publicKey = publicKey.clone();
    }
    
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    public byte[] getPublicKey() {
        return this.publicKey.clone();
    }
}
