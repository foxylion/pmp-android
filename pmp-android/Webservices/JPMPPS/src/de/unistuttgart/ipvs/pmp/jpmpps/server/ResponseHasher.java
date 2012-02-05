package de.unistuttgart.ipvs.pmp.jpmpps.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;

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
            md.update(new Long(revision).toString().getBytes(), 0, new Long(revision).toString().length());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Do not know the MD5-Hashing alogrithm.");
            
            if (JPMPPS.DEBUG) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    
    public static boolean checkHash(String locale, LocalizedResourceGroup[] rgs, byte[] hash) {
        byte[] checkHash = hash(locale, rgs);
        
        return new String(checkHash).equals(hash);
    }
}
