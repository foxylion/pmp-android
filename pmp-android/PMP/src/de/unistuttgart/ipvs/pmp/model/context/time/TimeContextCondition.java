package de.unistuttgart.ipvs.pmp.model.context.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The parsed condition for a {@link TimeContext}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class TimeContextCondition {
    
    private static Map<String, TimeContextCondition> cache = new HashMap<String, TimeContextCondition>();
    
    
    /**
     * Parses a {@link TimeContextCondition} from a string.
     * 
     * @param condition
     * @return
     */
    public static TimeContextCondition parse(String condition) {
        TimeContextCondition result = cache.get(condition);
        
        if (result == null) {
            result = new TimeContextCondition(false, new TimeContextConditionTime(0, 0, 0),
                    new TimeContextConditionTime(0, 0, 0), TimeContextConditionIntervalType.REPEAT_DAILY,
                    new ArrayList<TimeContextConditionDay>());
            cache.put(condition, result);
        }
        
        return result;
    }
    
    /**
     * Whether the time is fixed at a point, i.e. 08:00 always at this time zone,
     * then the time is converted to UTC and the information is in UTC.
     * In this case begin and end could wrap at 00:00:00.
     * If this is false the time is always relative to the local time zone of the user.
     */
    private boolean isUTC;
    
    /**
     * Begin and end during a 24-hrs period. May wrap, if isUTC.
     */
    private TimeContextConditionTime begin, end;
    
    /**
     * The interval to repeat the time, i.e. which days
     */
    private TimeContextConditionIntervalType interval;
    
    /**
     * The specific days in the interval
     */
    private List<TimeContextConditionDay> days;
    
    
    public TimeContextCondition(boolean isUTC, TimeContextConditionTime begin, TimeContextConditionTime end,
            TimeContextConditionIntervalType interval, List<TimeContextConditionDay> days) {
        this.isUTC = isUTC;
        this.begin = begin;
        this.end = end;
        this.interval = interval;
        this.days = days;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s%2d:%2d:%2d-%2d:%2d:%2d-%s%s", isUTC ? "utc" : "", this.begin.getHour(),
                this.begin.getMinute(), this.begin.getSecond(), this.end.getHour(), this.end.getMinute(),
                this.end.getSecond(), this.interval.getIdentifier(), this.interval.makeList(this.days));
    }
    
    
    /**
     * Checks whether the condition is satisfied in the state
     * 
     * @param state
     * @return
     */
    public boolean satisfiedIn(long state) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
