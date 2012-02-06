package de.unistuttgart.ipvs.pmp.model.context.time;

/**
 * Class to store a specific time for {@link TimeContextCondition}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class TimeContextConditionTime implements Comparable<TimeContextConditionTime> {
    
    private int hour, minute, second;
    
    
    public TimeContextConditionTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    
    public int getHour() {
        return hour;
    }
    
    
    public void setHour(int hour) {
        this.hour = hour;
    }
    
    
    public int getMinute() {
        return minute;
    }
    
    
    public void setMinute(int minute) {
        this.minute = minute;
    }
    
    
    public int getSecond() {
        return second;
    }
    
    
    public void setSecond(int second) {
        this.second = second;
    }
    
    
    @Override
    public int compareTo(TimeContextConditionTime another) {
        if (another == null) {
            return 0;
        }
        
        return (this.second - another.second) + (this.minute - another.minute) + (this.hour - another.hour);
    }
}
