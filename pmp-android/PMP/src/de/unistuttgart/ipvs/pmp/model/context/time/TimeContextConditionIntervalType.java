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
    
    /**
     * The character that identifies this type in a {@link TimeContextCondition} string.
     */
    private Character identifier;
    
    
    private TimeContextConditionIntervalType(Character identifier) {
        this.identifier = identifier;
    }
    
    
    public Character getIdentifier() {
        return identifier;
    }
    
    
    public String makeList(List<TimeContextConditionDay> days) {
        StringBuffer sb = new StringBuffer();
        
        for (TimeContextConditionDay tccd : days) {
            sb.append(tccd.getDay());
            if (this == REPEAT_YEARLY) {
                sb.append("~");
                sb.append(tccd.getMonth());
            }
            sb.append(",");
        }
        
        return sb.toString();
    }
    
    
    public List<TimeContextConditionDay> makeDays(String list) {
        List<TimeContextConditionDay> result = new ArrayList<TimeContextConditionDay>();
        
        return result;
    }
}
