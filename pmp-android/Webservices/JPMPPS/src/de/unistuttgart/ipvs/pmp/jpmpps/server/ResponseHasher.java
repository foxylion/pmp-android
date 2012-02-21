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
package de.unistuttgart.ipvs.pmp.jpmpps.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.jpmpps.model.ResourceGroup;

public class ResponseHasher {
    
    public static byte[] hash(String locale, LocalizedResourceGroup[] rgs) {
        StringBuilder hashBuilder = new StringBuilder();
        hashBuilder.append(locale);
        for (LocalizedResourceGroup rg : rgs) {
            hashBuilder.append(rg.getIdentifier());
            hashBuilder.append(rg.getRevision());
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hashBuilder.toString().getBytes(), 0, hashBuilder.toString().length());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Do not know the MD5-Hashing alogrithm.");
            
            if (JPMPPS.DEBUG) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    
    public static byte[] hash(long revision) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Long.toString(revision).getBytes(), 0, new Long(revision).toString().length());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Do not know the MD5-Hashing alogrithm.");
            
            if (JPMPPS.DEBUG) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    
    public static boolean checkHash(String locale, LocalizedResourceGroup[] rgs, byte[] cacheHash) {
        byte[] checkHash = hash(locale, rgs);
        
        return new String(checkHash).equals(cacheHash);
    }
    
    
    public static boolean checkHash(ResourceGroup rg, byte[] cacheHash) {
        byte[] checkHash = hash(rg.getRevision());
        
        return Arrays.equals(checkHash, cacheHash);
    }
}
