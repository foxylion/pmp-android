package de.unistuttgart.ipvs.pmp.model.context.time;

import java.util.ArrayList;
import java.util.List;

/**
 * Type to identify the intervals in a {@link TimeContextCondition}.
 * 
 * @author Tobias Kuhn
 * 
 */
public enum TimeContextConditionIntervalType {
    REPEAT_DAILY('D'),
    REPEAT_WEEKLY('W'),
    REPEAT_MONTHLY('M'),
    REPEAT_YEARLY('Y');
    
    private static final String DAY_SEPARATOR = ",";
    
    /**
     * The character that identifies this type in a {@link TimeContextCondition} string.
     */
    private Character identifier;
    
    
    private TimeContextConditionIntervalType(Character identifier) {
        this.identifier = identifier;
    }
    
    
    public Character getIdentifier() {
        return this.identifier;
    }
    
    
    /**
     * Converts a {@link TimeContextConditionDay} list into a string for this interval
     * 
     * @param days
     * @return
     */
    public String makeList(List<Integer> days) {
        StringBuffer sb = new StringBuffer();
        
        for (Integer tccd : days) {
            sb.append(tccd);
            sb.append(DAY_SEPARATOR);
        }
        
        return sb.toString();
    }
    
    
    /**
     * Converts a string into a {@link TimeContextConditionDay} list for this interval
     * 
     * @param list
     * @return
     */
    public List<Integer> makeDays(String list) {
        List<Integer> result = new ArrayList<Integer>();
        
        for (String day : list.split(DAY_SEPARATOR)) {
            result.add(Integer.parseInt(day), 0);
        }
        
        return result;
    }
    
    
    public static TimeContextConditionIntervalType getForIdentifier(Character identifier) {
        for (TimeContextConditionIntervalType tccit : values()) {
            if (tccit.getIdentifier().equals(identifier)) {
                return tccit;
            }
        }
        
        return null;
    }
}
