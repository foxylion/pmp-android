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
package de.unistuttgart.ipvs.pmp.jpmpps.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * {@link AbstractIO} gives ability to convert given data into byte arrays
 * to save bandwidth
 * 
 * @author Jakob Jarosch
 */
public abstract class AbstractIO implements Serializable {
    
    private static final long serialVersionUID = 2L;
    
    private byte[] cacheHash = new byte[0];
    
    
    /**
     * @return Returns the CacheHash of the response.
     */
    public byte[] getCacheHash() {
        return this.cacheHash;
    }
    
    
    /**
     * Sets a cacheHash for the response.
     */
    public void setCacheHash(byte[] cacheHash) {
        this.cacheHash = cacheHash;
    }
    
    
    /**
     * Creates a byte array from an {@link Object}.
     * 
     * @param object
     *            {@link Object} which should be converted into an byte array.
     * @return Converted {@link Object}.
     */
    protected byte[] toByteArray(Object object) {
        byte[] result = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(object);
            result = baos.toByteArray();
            
            out.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    
    /**
     * Creates a byte array from an {@link InputStream}.
     * 
     * @param in
     *            {@link InputStream} which should be converted into an byte
     *            array.
     * @return Converted {@link InputStream}.
     */
    protected byte[] toByteArray(InputStream in) {
        byte[] result = null;
        final int BUFFER_SIZE = 32 * 1024;
        
        try {
            BufferedInputStream inR = new BufferedInputStream(in);
            ByteArrayOutputStream aios = new ByteArrayOutputStream(BUFFER_SIZE);
            
            byte[] buffer = new byte[BUFFER_SIZE];
            
            int read = -1;
            do {
                read = inR.read(buffer, 0, BUFFER_SIZE);
                if (read > -1) {
                    aios.write(buffer, 0, read);
                }
            } while (read > -1);
            
            result = aios.toByteArray();
            
            inR.close();
            aios.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    
    /**
     * Creates a {@link InputStream} from an byte array.
     * 
     * @param data
     *            byte array which should be converted to an {@link InputStream} .
     * @return Converted byte array.
     */
    protected InputStream fromByteArray(byte[] data) {
        return new ByteArrayInputStream(data);
    }
}
