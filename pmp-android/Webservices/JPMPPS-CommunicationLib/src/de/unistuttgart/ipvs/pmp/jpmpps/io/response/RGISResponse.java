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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Answer of the server for a specific rgis request.
 * 
 * @author Jakob Jarosch
 */
public class RGISResponse extends AbstractResponse {
    
    private static final long serialVersionUID = 2L;
    
    private byte[] rgis = null;
    
    
    /**
     * Creates a new {@link RGISResponse}.
     * 
     * @param resourceGroup
     *            {@link InputStream} of the package which should be attached.
     */
    public RGISResponse(Serializable rgis, byte[] cacheHash) {
        if (!rgis.getClass().getSimpleName().equals("RGIS")) {
            throw new IllegalArgumentException();
        }
        
        this.rgis = toByteArray(rgis);
        setCacheHash(cacheHash);
    }
    
    
    /**
     * @return Returns the package as an {@link Serializable}. Must be casted to RGIS.
     */
    public Object getRGIS() {
        try {
            
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(fromByteArray(this.rgis));
                return ois.readObject();
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
            
        } catch (ClassNotFoundException e) {
            throw new IllegalAccessError(
                    "When you use this method you must also add the PMP-XML-UTILITIES to your library");
        } catch (IOException e) {
            System.out.println("[E] While reading the object from bytearray (Error: " + e.getMessage() + ")");
            e.printStackTrace();
        }
        return null;
    }
}
