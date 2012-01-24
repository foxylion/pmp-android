package de.unistuttgart.ipvs.pmp.util;

/**
 * Provides some necessary functions for {@link String}s.
 * 
 * @author Jakob Jarosch
 */
public class StringUtil {
    
    /**
     * Joins a {@link String} with the given delimiter as boundary.
     * 
     * @param delimiter
     *            Separates the different {@link String}.
     * @param stringArray
     *            Given Strings which should be joined.
     * @return Joined String.
     */
    public static String join(String delimiter, String... stringArray) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringArray.length - 1; i++) {
            sb.append(stringArray[i]);
            sb.append(delimiter);
        }
        sb.append(stringArray[stringArray.length - 1]);
        
        return sb.toString();
    }
}
