package de.unistuttgart.ipvs.pmp.model.context.time;

/**
 * Class to identify the days in an interval for {@link TimeContextCondition}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class TimeContextConditionDay {
    
    /**
     * may be Day of Week or Day of Month
     */
    private int day;
    
    /**
     * Used only, when {@link TimeContextConditionIntervalType} == REPEAT_YEARLY
     */
    private int month;
    
    
    public TimeContextConditionDay(int day, int month) {
        this.day = day;
        this.month = month;
    }
    
    
    public int getDay() {
        return day;
    }
    
    
    public void setDay(int day) {
        this.day = day;
    }
    
    
    public int getMonth() {
        return month;
    }
    
    
    public void setMonth(int month) {
        this.month = month;
    }
}
