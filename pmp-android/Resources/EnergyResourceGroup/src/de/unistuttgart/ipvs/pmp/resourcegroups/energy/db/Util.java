package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

/**
 * An utility class for converting milliseconds into a string representation of d, h, m, s
 * 
 * @author Marcus Vetter
 * 
 */
public class Util {
    
    /**
     * Convert a time in milliseconds to a string in days, hours, minutes, seconds
     * 
     * @param ms
     *            the time in milliseconds
     * @return a pretty string
     */
    public static String convertMillisecondsToString(long ms) {
        long givenSecs = ms / 1000;
        long days = givenSecs / 60 / 60 / 24;
        long daysInSec = days * 60 * 60 * 24;
        long hours = ((givenSecs - daysInSec) / 60 / 60);
        long hoursInSec = hours * 60 * 60;
        long mins = ((givenSecs - daysInSec - hoursInSec) / 60);
        long minsInSec = mins * 60;
        long secs = givenSecs - daysInSec - hoursInSec - minsInSec;
        
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(String.valueOf(days) + "d ");
        }
        if (hours > 0) {
            sb.append(String.valueOf(hours) + "h ");
        }
        if (mins > 0) {
            sb.append(String.valueOf(mins) + "m ");
        }
        sb.append(String.valueOf(secs) + "s");
        
        return sb.toString();
    }
    
}
