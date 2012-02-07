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
        return this.hour;
    }
    
    
    public void setHour(int hour) {
        this.hour = hour;
    }
    
    
    public int getMinute() {
        return this.minute;
    }
    
    
    public void setMinute(int minute) {
        this.minute = minute;
    }
    
    
    public int getSecond() {
        return this.second;
    }
    
    
    public void setSecond(int second) {
        this.second = second;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if ((o == null) || (!(o instanceof TimeContextConditionTime))) {
            return false;
        }
        TimeContextConditionTime another = (TimeContextConditionTime) o;
        return (this.second == another.second) && (this.minute == another.minute) && (this.hour == another.hour);
    }
    
    
    @Override
    public int hashCode() {
        return this.second ^ this.minute ^ this.hour;
    }
    
    
    @Override
    public int compareTo(TimeContextConditionTime another) {
        return (this.second - another.second) + (this.minute - another.minute) + (this.hour - another.hour);
    }
}
